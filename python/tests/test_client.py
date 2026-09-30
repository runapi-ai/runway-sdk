import pytest

from runapi.core import config
from runapi.core.errors import AuthenticationError
from runapi.runway import RunwayClient
from runapi.runway.resources.extend_video import ExtendVideo
from runapi.runway.resources.text_to_video import TextToVideo
from runapi.runway.types import CompletedTaskResponse, TaskCreateResponse


class FakeHttp:
    def __init__(self, *responses):
        self._responses = list(responses)
        self.calls = []

    def request(self, method, path, body=None, options=None):
        self.calls.append((method, path, body))
        if self._responses:
            return self._responses.pop(0)
        return {"id": "task_1", "status": "pending"}


@pytest.fixture(autouse=True)
def reset_config(monkeypatch):
    monkeypatch.delenv("RUNAPI_API_KEY", raising=False)
    monkeypatch.setattr(config, "api_key", None)
    yield


# --- auth -----------------------------------------------------------------


def test_accepts_api_key_parameter():
    assert isinstance(RunwayClient(api_key="k", http_client=FakeHttp()), RunwayClient)


def test_falls_back_to_global(monkeypatch):
    monkeypatch.setattr(config, "api_key", "global-key")
    assert isinstance(RunwayClient(http_client=FakeHttp()), RunwayClient)


def test_falls_back_to_env(monkeypatch):
    monkeypatch.setenv("RUNAPI_API_KEY", "env-key")
    assert isinstance(RunwayClient(http_client=FakeHttp()), RunwayClient)


def test_raises_without_api_key():
    with pytest.raises(AuthenticationError, match="API key is required"):
        RunwayClient()


# --- injection / accessors ------------------------------------------------


def test_uses_injected_http_client():
    fake = FakeHttp()
    client = RunwayClient(api_key="k", http_client=fake)
    assert client.text_to_video._http is fake
    assert client.extend_video._http is fake


def test_exposes_resource_accessors():
    client = RunwayClient(api_key="k", http_client=FakeHttp())
    assert isinstance(client.text_to_video, TextToVideo)
    assert isinstance(client.extend_video, ExtendVideo)


# --- request shapes -------------------------------------------------------


def test_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = RunwayClient(api_key="k", http_client=fake)
    result = client.text_to_video.create(
        prompt="hello world",
        duration_seconds=5,
        output_resolution="720p",
        aspect_ratio="16:9",
        seed=None,
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/runway/text_to_video",
            {
                "prompt": "hello world",
                "duration_seconds": 5,
                "output_resolution": "720p",
                "aspect_ratio": "16:9"},
        )]
    _, _, body = fake.calls[0]
    assert "model" not in body
    assert isinstance(result, TaskCreateResponse)


def test_get_fetches_by_id():
    fake = FakeHttp({"id": "t1", "status": "processing"})
    client = RunwayClient(api_key="k", http_client=fake)
    client.text_to_video.get("t1")
    assert fake.calls == [("get", "/api/v1/runway/text_to_video/t1", None)]


def test_extend_video_create_posts_compacted_body():
    fake = FakeHttp({"id": "t2", "status": "pending"})
    client = RunwayClient(api_key="k", http_client=fake)
    client.extend_video.create(
        source_task_id="t1",
        prompt="keep going",
        output_resolution="1080p",
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/runway/extend_video",
            {
                "source_task_id": "t1",
                "prompt": "keep going",
                "output_resolution": "1080p"},
        )]
    _, _, body = fake.calls[0]
    assert "model" not in body


def test_run_narrows_completed_type():
    fake = FakeHttp(
        {"id": "t1", "status": "pending"},
        {"id": "t1", "status": "completed", "usage": {"cost": 0.05}, "videos": [{"url": "https://x/y.mp4"}]},
    )
    client = RunwayClient(api_key="k", http_client=fake)
    result = client.text_to_video.run(
        model="runway",
        prompt="a serene lake",
        duration_seconds=5,
        output_resolution="720p",
        aspect_ratio="16:9",
    )
    assert isinstance(result, CompletedTaskResponse)
    assert result.videos[0].url == "https://x/y.mp4"

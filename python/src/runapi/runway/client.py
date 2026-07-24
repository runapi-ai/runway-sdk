"""Runway client."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import ProviderClient

from .resources.extend_video import ExtendVideo
from .resources.text_to_video import TextToVideo


class RunwayClient(ProviderClient):
    """Runway text-to-video and extend-video client.

    Example::

        client = RunwayClient(api_key="sk-...")
        result = client.text_to_video.run(
            model="...",
            prompt="A drone shot over a coastal city at sunset",
            duration_seconds=5,
            output_resolution="720p",
        )
    """

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        super().__init__(api_key, **options)
        http = self._http
        self.text_to_video = TextToVideo(http)
        self.extend_video = ExtendVideo(http)

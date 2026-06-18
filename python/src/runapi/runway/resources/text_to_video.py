"""Runway text-to-video resource."""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..types import (
    ASPECT_RATIOS,
    OUTPUT_RESOLUTIONS,
    CompletedTaskResponse,
    TaskCreateResponse,
)


class TextToVideo(Resource):
    """Generate videos from text prompts with Runway models."""

    ENDPOINT = "/api/v1/runway/text_to_video"

    RESPONSE_CLASS = TaskCreateResponse
    COMPLETED_RESPONSE_CLASS = CompletedTaskResponse

    def run(self, **params: Any) -> Any:
        """Create a text-to-video task and poll until it completes.

        Args:
            **params: text-to-video parameters (model, ...).

        Returns:
            The completed (narrowed) text-to-video response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create a text-to-video task and return immediately with an id.

        Args:
            **params: text-to-video parameters (model, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of a text-to-video task.

        Args:
            id: The task id returned by ``create``.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        if not params.get("prompt"):
            raise ValidationError("prompt is required")
        if not params.get("duration_seconds"):
            raise ValidationError("duration_seconds is required")
        if not params.get("output_resolution"):
            raise ValidationError("output_resolution is required")
        self._validate_optional(params, "output_resolution", OUTPUT_RESOLUTIONS)
        self._validate_optional(params, "aspect_ratio", ASPECT_RATIOS)

"""Runway extend-video resource."""

from __future__ import annotations

from typing import Any

from runapi.core import Resource

from ..contract_gen import CONTRACT
from ..types import (
    CompletedTaskResponse,
    TaskCreateResponse,
)


class ExtendVideo(Resource):
    """Extend an existing Runway video task."""

    ENDPOINT = "/api/v1/runway/extend_video"

    RESPONSE_CLASS = TaskCreateResponse
    COMPLETED_RESPONSE_CLASS = CompletedTaskResponse

    MODEL = "runway"

    def run(self, **params: Any) -> Any:
        """Append footage to a previous video and poll until it completes.

        Args:
            **params: extend-video parameters (model, ...).

        Returns:
            The completed (narrowed) extend-video response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create an extend-video task and return immediately with an id.

        Args:
            **params: extend-video parameters (model, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_contract(CONTRACT["extend-video"], {**compacted, "model": self.MODEL})
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of an extend-video task.

        Args:
            id: The task id returned by ``create``.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

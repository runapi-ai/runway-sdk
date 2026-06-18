"""Runway enums and response models."""

from __future__ import annotations

from runapi.core import BaseModel, TaskResponse, optional, required

OUTPUT_RESOLUTIONS = ["720p", "1080p"]
ASPECT_RATIOS = ["16:9", "9:16", "1:1", "4:3", "3:4"]


class Video(BaseModel):
    id = optional(str)
    url = required(str)


class Image(BaseModel):
    url = required(str)


class TaskResponseModel(TaskResponse):
    """Runway task status response."""

    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)
    videos = optional([lambda: Video])
    images = optional([lambda: Image])
    source_task_id = optional(str)
    error = optional(str)


class TaskCreateResponse(TaskResponseModel):
    """Runway task creation response with an id."""


class CompletedTaskResponse(TaskResponseModel):
    """Narrowed response from ``run()`` once polling observes completion."""

    videos = required([lambda: Video])

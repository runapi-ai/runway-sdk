# Runway Python SDK for RunAPI

The Runway Python SDK is the language-specific package for Runway on RunAPI. Use this package for video generation, animation, and video editing workflows when your application needs request bodies, task status lookup, and consistent RunAPI errors in Python.

This README is the Python package guide inside the public `runway-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/runway; for API reference, use https://runapi.ai/docs/api/runway/text-to-video; for SDK docs, use https://runapi.ai/docs/resources/sdks.

## Install

```bash
pip install runapi-runway
```

## Quick start

```python
from runapi.runway import RunwayClient

client = RunwayClient()  # reads RUNAPI_API_KEY, or pass api_key="sk-..."

task = client.text_to_video.create(
    model="...",
    prompt="A drone shot over a coastal city at sunset, cinematic",
    duration_seconds=5,
    output_resolution="720p",
    aspect_ratio="16:9",
)
status = client.text_to_video.get(task.id)

extended = client.extend_video.create(
    model="...",
    source_task_id=task.id,
    prompt="Continue the camera push toward the harbor",
    output_resolution="720p",
)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion:

```python
result = client.text_to_video.run(
    model="...",
    prompt="A serene mountain lake at dawn, slow pan",
    duration_seconds=5,
    output_resolution="720p",
)
print(result.videos[0].url)
```

In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Pass parameters as keyword arguments and catch the `runapi.runway` error classes when building video jobs, workers, or scripts. The available resources are `text_to_video` and `extend_video`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

## Links

- Model page: https://runapi.ai/models/runway
- SDK docs: https://runapi.ai/docs/resources/sdks
- Product docs: https://runapi.ai/docs/api/runway/text-to-video
- Pricing and rate limits: https://runapi.ai/models/runway
- Provider comparison: https://runapi.ai/providers/runway
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/runway-sdk

## License

Licensed under the Apache License, Version 2.0.

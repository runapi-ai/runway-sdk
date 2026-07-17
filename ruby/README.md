# Runway Ruby SDK for RunAPI

The Runway Ruby SDK is the language-specific package for Runway on RunAPI. Use this package for video generation, animation, and video editing workflows when your application needs request bodies, task status lookup, and consistent RunAPI errors in Ruby.

This README is the Ruby package guide inside the public `runway-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/runway; for API reference, use https://runapi.ai/docs#runway; for SDK docs, use https://runapi.ai/docs#sdk-runway.

## Install

```bash
gem install runapi-runway
```

## Quick start

```ruby
require "runapi/runway"

client = RunApi::Runway::Client.new
task = client.text_to_video.create(
  # Pass the Runway JSON request body from https://runapi.ai/docs#runway.
)
status = client.text_to_video.get(task.id)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Use Ruby keyword arguments and the `RunApi::Runway` error classes when building video jobs, Rails workers, or scripts. The available resources are `text_to_video` and `extend_video`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

## Links

- Model page: https://runapi.ai/models/runway
- SDK docs: https://runapi.ai/docs#sdk-runway
- Product docs: https://runapi.ai/docs#runway
- Pricing and rate limits: https://runapi.ai/models/runway
- Provider comparison: https://runapi.ai/providers/runway
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/runway-sdk

## License

Licensed under the Apache License, Version 2.0.

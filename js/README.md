# Runway API JavaScript SDK for RunAPI

The runway api JavaScript SDK is the language-specific package for Runway on RunAPI. Use this runway api package for text-to-video, image-to-video, video editing, and animation flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in JavaScript.

This runway api README is the JavaScript package guide inside the public `runway-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/runway; for API reference, use https://runapi.ai/docs#runway; for SDK docs, use https://runapi.ai/docs#sdk-runway.

## Install

```bash
npm install @runapi.ai/runway
```

## Quick start

```typescript
import { RunwayClient } from '@runapi.ai/runway';

const client = new RunwayClient();
const task = await client.textToVideo.create({
  // Pass the Runway JSON request body from https://runapi.ai/docs#runway.
});
const status = await client.textToVideo.get(task.id);
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Use the TypeScript types in `src/types.ts` and the resource classes under `src/resources` when building video applications. The available resources are `textToVideo` and `extendVideo`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

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

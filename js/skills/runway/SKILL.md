---
name: runway
description: Generate and extend videos, plus run Aleph video-to-video transforms, through RunAPI.ai using the @runapi.ai/runway Node/TypeScript SDK. Use when the user asks to add Runway video generation, video extension, Aleph transforms, or writes against @runapi.ai/runway. Triggers on "runway", "video generation", "video extension", "Aleph", "@runapi.ai/runway".
documentation: https://runapi.ai/models/runway
provider_page: https://runapi.ai/providers/runway
catalog: https://runapi.ai/models
---
# @runapi.ai/runway - RunAPI.ai Runway video generation

Build Node / TypeScript integrations that generate videos, extend existing Runway tasks, or transform videos with Aleph through RunAPI.ai.

## Setup

Requires **Node 18+** (global `fetch`).

```bash
npm install @runapi.ai/runway
```

```dotenv
# .env
RUNAPI_API_KEY=runapi_xxx   # get one at https://runapi.ai/settings/api_keys
```

```ts
import { RunwayClient } from '@runapi.ai/runway';

const client = new RunwayClient();
```

Pass `{ apiKey }` explicitly if you manage secrets differently. `baseUrl` defaults to `https://runapi.ai`; override only for local development.

## Resources

All resources use the async task contract:

```ts
const { id } = await client.generations.create({ ... });
const status = await client.generations.get(id);
const result = await client.generations.run({ ... });
```

Available resources:

| Resource | Endpoint | Use for |
|---|---|---|
| `client.generations` | `/api/v1/runway/generations` | Text-to-video and image-to-video |
| `client.extensions` | `/api/v1/runway/extensions` | Continue an existing Runway task |
| `client.alephGenerations` | `/api/v1/runway/aleph_generations` | Video-to-video Aleph transforms |

## Generate video

```ts
const result = await client.generations.run({
  prompt: 'A handheld shot of a red fox crossing a snowy road at dusk',
  duration: 5,
  quality: '720p',
  aspect_ratio: '16:9',
});

const url = result.videos[0].url;
```

For image-to-video, add `image_url`.

## Extend video

```ts
const result = await client.extensions.run({
  task_id: 'task-id',
  prompt: 'Continue the camera move into a wide landscape reveal',
  image_url: 'https://raw.githubusercontent.com/github/explore/main/topics/python/python.png',
  quality: '720p',
});
```

## Aleph transform

```ts
const result = await client.alephGenerations.run({
  prompt: 'Turn the scene into a warm watercolor animation',
  video_url: 'https://raw.githubusercontent.com/mediaelement/mediaelement-files/master/big_buck_bunny.mp4',
  aspect_ratio: '16:9',
});
```

## Key params

- `duration`: `5` or `10` for generations.
- `quality`: `720p` or `1080p` for generations and extensions.
- `aspect_ratio`: `16:9`, `9:16`, `1:1`, `4:3`, or `3:4`; Aleph also supports `21:9`.
- `watermark`: Optional watermark text.
- `callback_url`: Optional webhook URL for async completion.

## Errors

Runway methods throw the standard RunAPI error classes. For long-running tasks, prefer `create()` plus webhook or `get(id)` in request handlers, and reserve `run()` for jobs / CLI.

## RunAPI public routing

runway api public links use the API-379 catalog route map. The main runway api page is https://runapi.ai/models/runway. SDK docs live at https://runapi.ai/docs#sdk-runway and product docs live at https://runapi.ai/docs#runway.

Pricing, rate limits, and commercial usage for runway api should point to the most specific variant page:
- [Runway tools](https://runapi.ai/models/runway)

Compare Runway with other Runway models at https://runapi.ai/providers/runway. Browse every RunAPI model and skill at https://runapi.ai/models. SDK repository: https://github.com/runapi-ai/runway-sdk. Skill repository: https://github.com/runapi-ai/runway.

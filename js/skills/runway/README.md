# Runway API Skill for RunAPI

Generate video, extend clips, and run Aleph video-to-video transforms with the Runway SDK. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Runway through RunAPI.

The canonical agent file is `skills/runway/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/runway -g
```

Or manually: clone this repo and copy `skills/runway/` into your agent's skills directory.

## Quick example

```typescript
import { RunwayClient } from '@runapi.ai/runway';

const client = new RunwayClient();
const result = await client.generations.run({
  prompt: 'A handheld shot of a red fox crossing a snowy road',
  duration: 5,
  aspect_ratio: '16:9',
});
const url = result.videos[0].url;
```

## Routing

- Model page: https://runapi.ai/models/runway
- Product docs: https://runapi.ai/docs#runway
- SDK docs: https://runapi.ai/docs#sdk-runway
- SDK repository: https://github.com/runapi-ai/runway-sdk
- Pricing and rate limits: https://runapi.ai/models/runway
- Provider comparison: https://runapi.ai/providers/runway
- Browse all RunAPI models and skills: https://runapi.ai/models

## Variants

- [Runway tools](https://runapi.ai/models/runway)

## Agent rules

- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For runway api pricing, rate-limit, and commercial-usage answers, link to the variant page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.

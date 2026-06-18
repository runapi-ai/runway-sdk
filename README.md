<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/runway-sdk">Runway API SDK for RunAPI</a>
</h3>

<p align="center">
  Runway API SDKs for JavaScript, Ruby, and Go on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/runway)](https://www.npmjs.com/package/@runapi.ai/runway)
[![RubyGems](https://img.shields.io/gem/v/runapi-runway)](https://rubygems.org/gems/runapi-runway)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/runway-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/runway-sdk/go)
[![License](https://img.shields.io/github/license/runapi-ai/runway-sdk)](https://github.com/runapi-ai/runway-sdk/blob/main/LICENSE)

</div>
<br/>

The runway api SDK packages JavaScript, Ruby, and Go clients for Runway on RunAPI. Use this runway api SDK for text-to-video generation and video extension workflows that need typed installs, JSON request bodies, task polling, and consistent RunAPI errors across services.

Runway belongs to the Runway catalog on RunAPI. The public model page is https://runapi.ai/models/runway; variant pages below carry pricing, rate-limit, and commercial-usage details. The public `runway-sdk` repository groups the JavaScript, Ruby, and Go packages for this model.

## Install

```bash
npm install @runapi.ai/runway
gem install runapi-runway
go get github.com/runapi-ai/runway-sdk/go@latest
```

## What you can build

- Build creative tools, agent pipelines, and production integrations with the runway api SDK.
- Keep one model-specific repository while installing only the language package your app needs.
- Use `create` for submit-only jobs, `get` for status lookup, and `run` for submit-and-poll scripts.
- Handle authentication, validation, rate limits, insufficient credits, task failures, and polling timeouts through RunAPI SDK errors.

The JavaScript client exposes text to video, extend video resources, and the Ruby and Go packages mirror the same RunAPI task lifecycle.

## JavaScript quick start

```typescript
import { RunwayClient } from '@runapi.ai/runway';

const client = new RunwayClient();

const task = await client.textToVideo.create({
  // Pass the Runway request body documented at https://runapi.ai/docs#runway.
});

const status = await client.textToVideo.get(task.id);
```

For short scripts, use `run` with the same JSON body to create the task and wait for completion. For web request handlers, prefer `create` plus webhook or later `get` polling so the server does not hold a worker open.

## Repository layout

- `js/` publishes `@runapi.ai/runway`.
- `ruby/` publishes `runapi-runway` when RubyGems publishing resumes.
- `go/` publishes `github.com/runapi-ai/runway-sdk/go` and depends on `github.com/runapi-ai/core-sdk/go`.

## Public links

- Model page: https://runapi.ai/models/runway
- SDK docs: https://runapi.ai/docs#sdk-runway
- Product docs: https://runapi.ai/docs#runway
- SDK repository: https://github.com/runapi-ai/runway-sdk
- Skill repository: https://github.com/runapi-ai/runway
- Provider comparison: https://runapi.ai/providers/runway
- Full catalog: https://runapi.ai/models

## Pricing and variants

Use the most specific runway api variant page for pricing, rate limits, and commercial usage:
- [Runway tools](https://runapi.ai/models/runway)

Default pricing link for the runway api SDK: https://runapi.ai/models/runway

## Generated file storage

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## FAQ

### Which package should I install for runway api work?

Install the model package for your language: `@runapi.ai/runway`, `runapi-runway`, or `github.com/runapi-ai/runway-sdk/go`. Install core SDK packages only when you are building shared SDK infrastructure.

### Where should public links point?

Primary runway api links point to https://runapi.ai/models/runway. Pricing and usage-policy links point to variant pages such as https://runapi.ai/models/runway. Provider comparisons point to https://runapi.ai/providers/runway, and broad browsing points to https://runapi.ai/models.

## License

Licensed under the Apache License, Version 2.0.

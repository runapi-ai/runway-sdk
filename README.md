<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/runway-sdk">Runway API SDK for RunAPI</a>
</h3>

<p align="center">
  Runway API SDKs for JavaScript, Python, Ruby, Go, Java, and PHP on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/runway)](https://www.npmjs.com/package/@runapi.ai/runway)
[![PyPI](https://img.shields.io/pypi/v/runapi-runway)](https://pypi.org/project/runapi-runway/)
[![RubyGems](https://img.shields.io/gem/v/runapi-runway)](https://rubygems.org/gems/runapi-runway)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/runway-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/runway-sdk/go)
[![Maven Central](https://img.shields.io/maven-central/v/ai.runapi/runapi-runway)](https://central.sonatype.com/artifact/ai.runapi/runapi-runway)
[![License](https://img.shields.io/github/license/runapi-ai/runway-sdk)](https://github.com/runapi-ai/runway-sdk/blob/main/LICENSE)

</div>
<br/>

The Runway API SDK packages JavaScript, Python, Ruby, Go, Java, and PHP clients for Runway on RunAPI. Use it for text-to-video and video extension workflows when your app needs typed request builders, predictable task polling, file upload helpers, account helpers, and consistent RunAPI errors.

Runway is listed in the RunAPI model catalog at https://runapi.ai/models/runway. Variant pages below carry pricing, rate-limit, and commercial-usage details. The public `runway-sdk` repository groups the non-PHP language packages, examples, CI, and release tags for this model. The PHP package is released from a split Composer repository.

## Install

```bash
npm install @runapi.ai/runway
pip install runapi-runway
gem install runapi-runway
go get github.com/runapi-ai/runway-sdk/go@latest
```

Gradle:

```kotlin
dependencies {
  implementation("ai.runapi:runapi-runway:0.1.1")
}
```

Maven:

```xml
<dependency>
  <groupId>ai.runapi</groupId>
  <artifactId>runapi-runway</artifactId>
  <version>0.1.1</version>
</dependency>
```

Use the Java BOM when installing multiple RunAPI Java modules:

```kotlin
dependencies {
  implementation(platform("ai.runapi:runapi-bom:0.4.1"))
  implementation("ai.runapi:runapi-runway")
}
```

The PHP package is published from the split Composer repository as `runapi-ai/runway`; see https://github.com/runapi-ai/runway-php for PHP install and examples.

## What you can build

- Build apps, agent workflows, batch jobs, and production services around Runway requests.
- Install only the language package your app needs while keeping one model-specific repository for docs and releases.
- Use `create` for submit-only jobs, `get` for status lookup, and `run` for submit-and-poll scripts.
- Upload local files, URL files, or base64 files through shared RunAPI file helpers.
- Handle validation, authentication, rate limits, insufficient credits, task failures, and polling timeouts through RunAPI SDK errors.

## Java quick start

```java
import ai.runapi.runway.RunwayClient;
import ai.runapi.runway.types.TextToVideoParams;
import ai.runapi.runway.types.CompletedTextToVideoResponse;
import ai.runapi.runway.types.TextToVideoModel;

RunwayClient client = RunwayClient.builder()
    .apiKey(System.getenv("RUNAPI_API_KEY"))
    .build();

CompletedTextToVideoResponse result = client.textToVideo().run(
    TextToVideoParams.builder()
        .model(TextToVideoModel.RUNWAY)
        .prompt("A cinematic shot of clouds rolling over a city")
        .durationSeconds(5)
        .outputResolution("720p")
        .aspectRatio("16:9")
        .build()
);
```

Java packages target Java 8 bytecode and are tested on Java 8, 11, 17, and 21. Each model artifact depends on `ai.runapi:runapi-core`, so application code normally installs only `ai.runapi:runapi-runway`.

## Task lifecycle

Most media endpoints are asynchronous. `create()` submits a task and returns its id, `get(id)` fetches the latest task state, and `run(params)` creates the task and polls until it reaches a terminal state. In web request handlers, prefer `create()` plus webhook or later `get()` polling so the server does not hold a worker open.

## Repository layout

- `js/` publishes `@runapi.ai/runway`.
- `python/` publishes `runapi-runway`.
- `ruby/` publishes `runapi-runway`.
- `go/` publishes `github.com/runapi-ai/runway-sdk/go`.
- `java/` publishes `ai.runapi:runapi-runway` and uses `ai.runapi:runapi-core`.

## Public links

- Model page: https://runapi.ai/models/runway
- SDK docs: https://runapi.ai/docs/resources/sdks
- Product docs: https://runapi.ai/docs/api/runway/text-to-video
- SDK repository: https://github.com/runapi-ai/runway-sdk
- PHP package repository: https://github.com/runapi-ai/runway-php
- Skill repository: https://github.com/runapi-ai/runway
- Provider comparison: https://runapi.ai/providers/runway
- Full catalog: https://runapi.ai/models

## Pricing and variants

Use the most specific Runway variant page for pricing, rate limits, and commercial usage:
- [Runway tools](https://runapi.ai/models/runway)

Default pricing link for the Runway SDK: https://runapi.ai/models/runway

## File storage

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## FAQ

### Which package should I install for Runway work?

Install the model package for your language: `@runapi.ai/runway` on npm, `runapi-runway` on PyPI, `runapi-runway` on RubyGems, `github.com/runapi-ai/runway-sdk/go`, `ai.runapi:runapi-runway` on Maven Central, or `runapi-ai/runway` on Packagist. Install core SDK packages only when you are building shared SDK infrastructure.

### Where should public links point?

Primary Runway links point to https://runapi.ai/models/runway. Pricing and usage-policy links point to variant pages such as https://runapi.ai/models/runway. Provider comparisons point to https://runapi.ai/providers/runway, and broad browsing points to https://runapi.ai/models.

## License

Licensed under the Apache License, Version 2.0.

import { BaseClient, type ClientOptions } from '@runapi.ai/core';
import { TextToVideo } from './resources/text-to-video';
import { ExtendVideo } from './resources/extend-video';

/**
 * Runway Gen-4 video generation API client.
 *
 * @example
 * ```typescript
 * const client = new RunwayClient({ apiKey: 'your-api-key' });
 *
 * const result = await client.textToVideo.run({
 *   prompt: 'A timelapse of a city skyline at sunset',
 *   duration_seconds: 10,
 *   output_resolution: '720p',
 * });
 * ```
 */
export class RunwayClient extends BaseClient {
  /** Generate video from a text prompt, optionally using a first-frame image for image-to-video generation. */
  public readonly textToVideo: TextToVideo;
  /** Append additional footage to a previously generated video, continuing from where the source task left off. */
  public readonly extendVideo: ExtendVideo;

  constructor(options: ClientOptions = {}) {
    super(options);
    this.textToVideo = new TextToVideo(this.http);
    this.extendVideo = new ExtendVideo(this.http);
  }
}

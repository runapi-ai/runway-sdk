import { createHttpClient, type ClientOptions } from '@runapi.ai/core';
import { TextToVideo } from './resources/text-to-video';
import { ExtendVideo } from './resources/extend-video';

export class RunwayClient {
  public readonly textToVideo: TextToVideo;
  public readonly extendVideo: ExtendVideo;

  constructor(options: ClientOptions = {}) {
    const http = createHttpClient(options);
    this.textToVideo = new TextToVideo(http);
    this.extendVideo = new ExtendVideo(http);
  }
}

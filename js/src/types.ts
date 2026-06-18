import type { AsyncTaskStatus } from '@runapi.ai/core';

/** Output resolution. 720p (1280x720) is faster and lower cost; 1080p (1920x1080) produces higher detail at higher cost. */
export type RunwayOutputResolution = '720p' | '1080p';
/** Aspect ratio for pure text-to-video generation. Ignored when first_frame_image_url is provided (aspect ratio is inferred from the image). */
export type RunwayAspectRatio = '16:9' | '9:16' | '1:1' | '4:3' | '3:4';
/** Video duration in seconds. */
export type RunwayDuration = 5 | 10;

/** A generated video file with a download URL. */
export interface Video {
  id?: string;
  url: string;
}

/** A generated image file with a download URL. */
export interface Image {
  url: string;
}

/** Response returned when a task is first created, before polling begins. */
export interface TaskCreateResponse {
  id: string;
  status?: AsyncTaskStatus;
}

/** Full task response returned by polling. Contains output media once the task completes. */
export interface RunwayTaskResponse {
  id: string;
  status: AsyncTaskStatus;
  /** Generated video files, present when the task completes successfully. */
  videos?: Video[];
  /** Generated image files, if the task produces image output. */
  images?: Image[];
  /** ID of the source task when this is an extend-video task. */
  source_task_id?: string;
  /** Error description if the task failed. */
  error?: string;
  [key: string]: unknown;
}

/** Narrowed task response returned by `run()` once polling observes completion. `videos` is guaranteed present. */
export type CompletedRunwayTaskResponse = RunwayTaskResponse & {
  status: 'completed';
  videos: Video[];
};

/**
 * Text-to-video generation parameters. Set first_frame_image_url to use an image
 * as the opening frame, turning this into image-to-video generation. When an image
 * is provided, aspect_ratio is ignored (inferred from the image).
 */
export interface TextToVideoParams {
  /** Video description prompt. */
  prompt: string;
  /** Video length in seconds. */
  duration_seconds: RunwayDuration;
  /** 720p is faster and cheaper; 1080p produces higher detail. */
  output_resolution: RunwayOutputResolution;
  /** Opening frame image URL; when set, the model animates from this image instead of generating the first frame. */
  first_frame_image_url?: string;
  /** Only used for pure text-to-video (no first-frame image). */
  aspect_ratio?: RunwayAspectRatio;
  /** Watermark text to burn into the output video. */
  watermark?: string;
  /** URL to receive a webhook notification when the task completes. */
  callback_url?: string;
}

/**
 * Extend-video parameters. Appends footage to a completed TextToVideo or ExtendVideo task.
 * output_resolution must match the resolution of the source task.
 */
export interface ExtendVideoParams {
  /** Task ID of the completed TextToVideo or ExtendVideo task to continue from. */
  source_task_id: string;
  /** Prompt describing the continuation footage. */
  prompt: string;
  /** Must match the output_resolution of the source task. */
  output_resolution: RunwayOutputResolution;
  /** Watermark text to burn into the output video. */
  watermark?: string;
  /** URL to receive a webhook notification when the task completes. */
  callback_url?: string;
}

import type { AsyncTaskStatus } from '@runapi.ai/core';

export type RunwayOutputResolution = '720p' | '1080p';
export type RunwayAspectRatio = '16:9' | '9:16' | '1:1' | '4:3' | '3:4';
export type RunwayDuration = 5 | 10;

export interface Video {
  id?: string;
  url: string;
}

export interface Image {
  url: string;
}

export interface TaskCreateResponse {
  id: string;
  status?: AsyncTaskStatus;
}

export interface RunwayTaskResponse {
  id: string;
  status: AsyncTaskStatus;
  videos?: Video[];
  images?: Image[];
  source_task_id?: string;
  error?: string;
  [key: string]: unknown;
}

export type CompletedRunwayTaskResponse = RunwayTaskResponse & {
  status: 'completed';
  videos: Video[];
};

export interface TextToVideoParams {
  prompt: string;
  duration_seconds: RunwayDuration;
  output_resolution: RunwayOutputResolution;
  first_frame_image_url?: string;
  aspect_ratio?: RunwayAspectRatio;
  watermark?: string;
  callback_url?: string;
}

export interface ExtendVideoParams {
  source_task_id: string;
  prompt: string;
  output_resolution: RunwayOutputResolution;
  watermark?: string;
  callback_url?: string;
}

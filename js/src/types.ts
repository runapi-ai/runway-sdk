import type { AsyncTaskStatus } from '@runapi.ai/core';

export type RunwayQuality = '720p' | '1080p';
export type RunwayAspectRatio = '16:9' | '9:16' | '1:1' | '4:3' | '3:4';
export type RunwayDuration = 5 | 10;

export interface Video {
  id?: string;
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
  image_url?: string;
  parent_task_id?: string;
  error?: string;
  [key: string]: unknown;
}

export type CompletedRunwayTaskResponse = RunwayTaskResponse & {
  status: 'completed';
  videos: Video[];
};

export interface TextToVideoParams {
  prompt: string;
  duration: RunwayDuration;
  quality: RunwayQuality;
  image_url?: string;
  aspect_ratio?: RunwayAspectRatio;
  watermark?: string;
  callback_url?: string;
}

export interface ExtendVideoParams {
  task_id: string;
  prompt: string;
  image_url: string;
  quality: RunwayQuality;
  watermark?: string;
  callback_url?: string;
}

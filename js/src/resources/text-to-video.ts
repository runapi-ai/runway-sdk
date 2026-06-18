import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { CompletedRunwayTaskResponse, RunwayTaskResponse, TaskCreateResponse, TextToVideoParams } from '../types';

const ENDPOINT = '/api/v1/runway/text_to_video';

/** Generate video from a text prompt, optionally using a first-frame image for image-to-video generation. */
export class TextToVideo {
  constructor(private readonly http: HttpClient) {}

  /**
   * Create a text to video task and wait until complete.
   * @param params Text to video parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed text to video response.
   */
  async run(params: TextToVideoParams, options?: RequestOptions & PollingOptions): Promise<CompletedRunwayTaskResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<RunwayTaskResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedRunwayTaskResponse;
  }

  /**
   * Create a text to video task; returns immediately with a task id.
   * @param params Text to video parameters.
   * @param options Per-request overrides.
   * @returns The task creation result.
   */
  async create(params: TextToVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, { body: compactParams(params), ...options });
  }

  /**
   * Fetch the current status of a text to video task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current text to video task status.
   */
  async get(id: string, options?: RequestOptions): Promise<RunwayTaskResponse> {
    return this.http.request<RunwayTaskResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}

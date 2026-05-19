import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { CompletedRunwayTaskResponse, RunwayTaskResponse, TaskCreateResponse, TextToVideoParams } from '../types';

const ENDPOINT = '/api/v1/runway/text_to_video';

export class TextToVideo {
  constructor(private readonly http: HttpClient) {}

  async run(params: TextToVideoParams, options?: RequestOptions & PollingOptions): Promise<CompletedRunwayTaskResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<RunwayTaskResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedRunwayTaskResponse;
  }

  async create(params: TextToVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, { body: compactParams(params), ...options });
  }

  async get(id: string, options?: RequestOptions): Promise<RunwayTaskResponse> {
    return this.http.request<RunwayTaskResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}

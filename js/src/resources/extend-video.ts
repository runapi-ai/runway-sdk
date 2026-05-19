import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { CompletedRunwayTaskResponse, ExtendVideoParams, RunwayTaskResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/runway/extend_video';

export class ExtendVideo {
  constructor(private readonly http: HttpClient) {}

  async run(params: ExtendVideoParams, options?: RequestOptions & PollingOptions): Promise<CompletedRunwayTaskResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<RunwayTaskResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedRunwayTaskResponse;
  }

  async create(params: ExtendVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, { body: compactParams(params), ...options });
  }

  async get(id: string, options?: RequestOptions): Promise<RunwayTaskResponse> {
    return this.http.request<RunwayTaskResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}

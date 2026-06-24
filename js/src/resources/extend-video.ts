import type { ActionSchema, HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type { CompletedRunwayTaskResponse, ExtendVideoParams, RunwayTaskResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/runway/extend_video';

// Fixed endpoint model, injected only for contract validation (never sent on the wire).
const MODEL = 'runway';

/** Append additional footage to a previously generated video, continuing from where the source task left off. */
export class ExtendVideo {
  constructor(private readonly http: HttpClient) {}

  /**
   * Create an extend video task and wait until complete.
   * @param params Extend video parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed extend video response.
   */
  async run(params: ExtendVideoParams, options?: RequestOptions & PollingOptions): Promise<CompletedRunwayTaskResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<RunwayTaskResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedRunwayTaskResponse;
  }

  /**
   * Create an extend video task; returns immediately with a task id.
   * @param params Extend video parameters.
   * @param options Per-request overrides.
   * @returns The task creation result.
   */
  async create(params: ExtendVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['extend-video'] as ActionSchema, { ...body, model: MODEL } as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, { body, ...options });
  }

  /**
   * Fetch the current status of an extend video task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current extend video task status.
   */
  async get(id: string, options?: RequestOptions): Promise<RunwayTaskResponse> {
    return this.http.request<RunwayTaskResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}

import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { TextToVideo } from '../../src/resources/text-to-video';
import { ExtendVideo } from '../../src/resources/extend-video';

describe('Runway resources', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('creates text-to-video with flat params', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-1' });
    const textToVideo = new TextToVideo(mockHttp);

    await textToVideo.create({
      prompt: 'Ocean spray',
      duration_seconds: 5,
      output_resolution: '720p',
      first_frame_image_url: 'https://cdn.runapi.ai/public/samples/first-frame.png',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/runway/text_to_video', {
      body: {
        prompt: 'Ocean spray',
        duration_seconds: 5,
        output_resolution: '720p',
        first_frame_image_url: 'https://cdn.runapi.ai/public/samples/first-frame.png',
      },
    });
  });

  it('gets text-to-video by id', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({
      id: 'task-1',
      status: 'completed',
      videos: [{ url: 'https://file.runapi.ai/video.mp4' }],
      images: [{ url: 'https://file.runapi.ai/cover.png' }],
    });
    const textToVideo = new TextToVideo(mockHttp);

    const result = await textToVideo.get('task-1');

    expect(mockHttp.request).toHaveBeenCalledWith('GET', '/api/v1/runway/text_to_video/task-1', {});
    expect(result.videos?.[0]?.url).toBe('https://file.runapi.ai/video.mp4');
    expect(result.images?.[0]?.url).toBe('https://file.runapi.ai/cover.png');
  });

  it('creates extend-video with source_task_id', async () => {
    vi.mocked(mockHttp.request).mockResolvedValueOnce({ id: 'task-2' });
    const extendVideo = new ExtendVideo(mockHttp);

    await extendVideo.create({ source_task_id: 'src-1', prompt: 'Keep moving', output_resolution: '720p' });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/runway/extend_video', {
      body: { source_task_id: 'src-1', prompt: 'Keep moving', output_resolution: '720p' },
    });
  });
});

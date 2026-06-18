# frozen_string_literal: true

module RunApi
  module Runway
    # Type definitions and constants for Runway Gen-4 video generation.
    module Types
      # Output resolution. 720p (1280x720) is faster and lower cost;
      # 1080p (1920x1080) produces higher detail at higher cost.
      OUTPUT_RESOLUTIONS = %w[720p 1080p].freeze

      # Aspect ratio for pure text-to-video. Ignored when a first-frame image is provided.
      ASPECT_RATIOS = %w[16:9 9:16 1:1 4:3 3:4].freeze

      # A generated video file with a download URL.
      class Video < RunApi::Core::BaseModel
        optional :id, String
        required :url, String
      end

      # A generated image file with a download URL.
      class Image < RunApi::Core::BaseModel
        required :url, String
      end

      # Full task response returned by polling. Contains output media once the task completes.
      class TaskResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :videos, [-> { Video }]
        optional :images, [-> { Image }]
        optional :source_task_id, String
        optional :error, String
      end

      # Response returned when a task is first created, before polling begins.
      class TaskCreateResponse < TaskResponse; end

      # Narrowed response returned by +run()+ once polling observes completion.
      # +videos+ is guaranteed present so consumers never need to null-check.
      class CompletedTaskResponse < TaskResponse
        required :videos, [-> { Video }]
      end
    end
  end
end

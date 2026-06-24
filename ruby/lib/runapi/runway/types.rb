# frozen_string_literal: true

module RunApi
  module Runway
    # Type definitions and constants for Runway Gen-4 video generation.
    module Types
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

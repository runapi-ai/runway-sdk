# frozen_string_literal: true

module RunApi
  module Runway
    module Types
      QUALITIES = %w[720p 1080p].freeze
      ASPECT_RATIOS = %w[16:9 9:16 1:1 4:3 3:4].freeze

      class Video < RunApi::Core::BaseModel
        optional :id, String
        required :url, String
      end

      class TaskResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :videos, [ -> { Video } ]
        optional :image_url, String
        optional :parent_task_id, String
        optional :error, String
      end

      class TaskCreateResponse < TaskResponse; end

      class CompletedTaskResponse < TaskResponse
        required :videos, [ -> { Video } ]
      end
    end
  end
end

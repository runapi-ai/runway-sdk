# frozen_string_literal: true

module RunApi
  module Runway
    module Resources
      # Runway extend-video resource.
      # Append additional footage to a previously generated video, continuing from where the source task left off.
      class ExtendVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/runway/extend_video"
        RESPONSE_CLASS = Types::TaskCreateResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedTaskResponse

        def initialize(http)
          @http = http
        end

        # Extend a video and wait until complete.
        #
        # @param source_task_id [String] ID of the completed TextToVideo or ExtendVideo task to continue from
        # @param prompt [String] prompt describing the continuation footage
        # @param output_resolution [String] must match the resolution of the source task ("720p" or "1080p")
        # @param watermark [String, nil] watermark text burned into the output
        # @param callback_url [String, nil] webhook URL for completion notification
        # @return [RunApi::Runway::Types::CompletedTaskResponse] completed task with videos
        def run(**params)
          task = create(**params)
          poll_until_complete { get(task.id) }
        end

        # Create an extend-video task without waiting for completion.
        #
        # @param params [Hash] extend-video parameters (see {#run} for details)
        # @return [RunApi::Runway::Types::TaskCreateResponse] task creation result with id
        def create(**params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params)
        end

        # Get extend-video task status by task ID.
        #
        # @param id [String] task ID
        # @return [RunApi::Runway::Types::TaskResponse] current task status
        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end

        private

        def validate_params!(params)
          raise Core::ValidationError, "source_task_id is required" unless param(params, :source_task_id)
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)
          raise Core::ValidationError, "output_resolution is required" unless param(params, :output_resolution)
          validate_optional!(params, :output_resolution, Types::OUTPUT_RESOLUTIONS)
        end
      end
    end
  end
end

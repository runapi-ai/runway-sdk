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
        MODEL = "runway"

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
        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        # Create an extend-video task without waiting for completion.
        #
        # @param params [Hash] extend-video parameters (see {#run} for details)
        # @return [RunApi::Runway::Types::TaskCreateResponse] task creation result with id
        def create(options: nil, **params)
          params = compact_params(params)
          validate_contract!(CONTRACT["extend-video"], params.merge(model: MODEL))
          request(:post, ENDPOINT, body: params, options: options)
        end

        # Get extend-video task status by task ID.
        #
        # @param id [String] task ID
        # @return [RunApi::Runway::Types::TaskResponse] current task status
        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end
      end
    end
  end
end

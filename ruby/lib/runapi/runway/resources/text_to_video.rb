# frozen_string_literal: true

module RunApi
  module Runway
    module Resources
      # Runway text-to-video resource.
      # Generate video from a text prompt, optionally using a first-frame image for image-to-video generation.
      class TextToVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/runway/text_to_video"
        RESPONSE_CLASS = Types::TaskCreateResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedTaskResponse
        MODEL = "runway"

        def initialize(http)
          @http = http
        end

        # Create a text-to-video task and wait until complete.
        #
        # @param prompt [String] video description prompt
        # @param duration_seconds [Integer] video length: 5 or 10
        # @param output_resolution [String] "720p" or "1080p"
        # @param first_frame_image_url [String, nil] opening frame image URL for image-to-video
        # @param aspect_ratio [String, nil] only for pure text-to-video (no first-frame image)
        # @param watermark [String, nil] watermark text burned into the output
        # @param callback_url [String, nil] webhook URL for completion notification
        # @return [RunApi::Runway::Types::CompletedTaskResponse] completed task with videos
        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        # Create a text-to-video task without waiting for completion.
        #
        # @param params [Hash] text-to-video parameters (see {#run} for details)
        # @return [RunApi::Runway::Types::TaskCreateResponse] task creation result with id
        def create(options: nil, **params)
          params = compact_params(params)
          validate_contract!(CONTRACT["text-to-video"], params.merge(model: MODEL))
          request(:post, ENDPOINT, body: params, options: options)
        end

        # Get text-to-video task status by task ID.
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

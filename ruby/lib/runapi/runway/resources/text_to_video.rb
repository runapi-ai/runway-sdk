# frozen_string_literal: true

module RunApi
  module Runway
    module Resources
      class TextToVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/runway/text_to_video"
        RESPONSE_CLASS = Types::TaskCreateResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedTaskResponse

        def initialize(http)
          @http = http
        end

        def run(**params)
          task = create(**params)
          poll_until_complete { get(task.id) }
        end

        def create(**params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params)
        end

        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end

        private

        def validate_params!(params)
          raise Core::ValidationError, "prompt is required" unless param(params, :prompt)
          raise Core::ValidationError, "duration is required" unless param(params, :duration)
          raise Core::ValidationError, "quality is required" unless param(params, :quality)
          validate_optional!(params, :quality, Types::QUALITIES)
          validate_optional!(params, :aspect_ratio, Types::ASPECT_RATIOS)
        end
      end
    end
  end
end

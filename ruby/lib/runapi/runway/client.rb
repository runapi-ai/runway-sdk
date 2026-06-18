# frozen_string_literal: true

module RunApi
  module Runway
    # Runway Gen-4 video generation API client.
    #
    # @example
    #   client = RunApi::Runway::Client.new(api_key: "your-api-key")
    #   result = client.text_to_video.run(
    #     prompt: "A timelapse of a city skyline at sunset",
    #     duration_seconds: 10,
    #     output_resolution: "720p"
    #   )
    class Client < RunApi::Core::Client
      # @return [Resources::TextToVideo] Text-to-video and image-to-video operations.
      attr_reader :text_to_video
      # @return [Resources::ExtendVideo] Extend-video operations.
      attr_reader :extend_video

      def initialize(api_key: nil, **options)
        super
        @text_to_video = Resources::TextToVideo.new(http)
        @extend_video = Resources::ExtendVideo.new(http)
      end
    end
  end
end

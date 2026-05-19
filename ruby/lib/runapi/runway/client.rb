# frozen_string_literal: true

module RunApi
  module Runway
    class Client
      attr_reader :text_to_video, :extend_video

      def initialize(api_key: nil, **options)
        @api_key = Core::Auth.resolve_api_key(api_key)

        client_options = Core::ClientOptions.new(api_key: @api_key, **options)
        http = client_options.http_client || Core::HttpClient.new(client_options)
        @text_to_video = Resources::TextToVideo.new(http)
        @extend_video = Resources::ExtendVideo.new(http)
      end
    end
  end
end

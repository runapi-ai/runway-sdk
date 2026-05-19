# frozen_string_literal: true

require "runapi/core"
require_relative "runway/types"
require_relative "runway/resources/text_to_video"
require_relative "runway/resources/extend_video"
require_relative "runway/client"

module RunApi
  module Runway
    AuthenticationError = RunApi::Core::AuthenticationError
    RateLimitError = RunApi::Core::RateLimitError
    InsufficientCreditsError = RunApi::Core::InsufficientCreditsError
    NotFoundError = RunApi::Core::NotFoundError
    ValidationError = RunApi::Core::ValidationError
    TaskFailedError = RunApi::Core::TaskFailedError
    TaskTimeoutError = RunApi::Core::TaskTimeoutError
  end
end

# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Runway::Client do
  after { RunApi.api_key = nil }

  it "accepts api_key as parameter" do
    expect(described_class.new(api_key: "param-key")).to be_a(described_class)
  end

  it "falls back to global RunApi.api_key" do
    RunApi.api_key = "global-key"
    expect(described_class.new).to be_a(described_class)
  end

  it "exposes canonical resources" do
    client = described_class.new(api_key: "test-key")
    expect(client.text_to_video).to be_a(RunApi::Runway::Resources::TextToVideo)
    expect(client.extend_video).to be_a(RunApi::Runway::Resources::ExtendVideo)
  end
end

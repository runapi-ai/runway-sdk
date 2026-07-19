# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Runway::Resources::ExtendVideo do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/runway/extend_video" }

  it "POSTs to the correct endpoint without a model param" do
    params = {source_task_id: "src-1", prompt: "Keep moving", output_resolution: "720p"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "task-2")
    result = resource.create(**params)
    expect(result.id).to eq("task-2")
  end
end

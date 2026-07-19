# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Runway::Resources::TextToVideo do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:resource) { described_class.new(http) }
  let(:endpoint) { "/api/v1/runway/text_to_video" }

  it "POSTs to the correct endpoint without a model param" do
    body = {
      prompt: "Ocean spray",
      duration_seconds: 5,
      output_resolution: "720p",
      first_frame_image_url: "https://cdn.runapi.ai/public/samples/first-frame.png"
    }
    expect(http).to receive(:request).with(:post, endpoint, body: body).and_return("id" => "task-1")
    result = resource.create(**body)
    expect(result.id).to eq("task-1")
  end

  it "GETs the correct endpoint" do
    expect(http).to receive(:request).with(:get, "#{endpoint}/task-1").and_return(
      "id" => "task-1",
      "status" => "completed",
      "videos" => [{"url" => "https://file.runapi.ai/video.mp4"}],
      "images" => [{"url" => "https://file.runapi.ai/cover.png"}]
    )
    result = resource.get("task-1")
    expect(result.status).to eq("completed")
    expect(result.videos.first.url).to eq("https://file.runapi.ai/video.mp4")
    expect(result.images.first.url).to eq("https://file.runapi.ai/cover.png")
  end
end

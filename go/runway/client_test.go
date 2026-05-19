package runway

import (
	"context"
	"encoding/json"
	"testing"

	"github.com/runapi-ai/core-sdk/go/core"
)

type stubHTTPClient struct {
	method   string
	path     string
	body     any
	response json.RawMessage
}

func (s *stubHTTPClient) Request(_ context.Context, method, path string, opts *core.HTTPRequestOptions) (json.RawMessage, error) {
	s.method = method
	s.path = path
	if opts != nil {
		s.body = opts.Body
	}
	return s.response, nil
}

func TestTextToVideoCreate(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"task_txt_123","status":"processing"}`)}
	client := NewClientWithHTTP(stub)
	resp, err := client.TextToVideo.Create(context.Background(), TextToVideoParams{Prompt: "ocean spray", Duration: 5, Quality: Quality720p})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != textToVideoPath {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["prompt"] != "ocean spray" {
		t.Fatalf("unexpected body: %v", body)
	}
	if resp.ID != "task_txt_123" {
		t.Fatalf("unexpected id: %v", resp.ID)
	}
}

func TestExtendVideoCreate(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"task_ext_123","status":"processing"}`)}
	client := NewClientWithHTTP(stub)
	_, err := client.ExtendVideo.Create(context.Background(), ExtendVideoParams{TaskID: "src-1", Prompt: "keep moving", ImageURL: "https://example.com/cover.png", Quality: Quality720p})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != extendVideoPath {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["image_url"] != "https://example.com/cover.png" {
		t.Fatalf("unexpected body: %v", body)
	}
}

func TestTextToVideoGet(t *testing.T) {
	stub := &stubHTTPClient{response: json.RawMessage(`{"id":"task_txt_456","status":"completed","videos":[{"url":"https://file.runapi.ai/video.mp4"}]}`)}
	client := NewClientWithHTTP(stub)
	resp, err := client.TextToVideo.Get(context.Background(), "task_txt_456")
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "GET" || stub.path != textToVideoPath+"/task_txt_456" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	if len(resp.Videos) != 1 || resp.Videos[0].URL != "https://file.runapi.ai/video.mp4" {
		t.Fatalf("unexpected response: %v", resp.Videos)
	}
}

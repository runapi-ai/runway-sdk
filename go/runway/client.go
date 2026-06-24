// Package runway provides the Runway Gen-4 video generation API client.
//
//	client, err := runway.NewClient(option.WithAPIKey("sk-your-api-key"))
//	result, err := client.TextToVideo.Run(ctx, runway.TextToVideoParams{
//	    Prompt: "A timelapse of a city skyline at sunset",
//	    DurationSeconds: 10, OutputResolution: runway.OutputResolution720p,
//	})
package runway

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/base"
	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	textToVideoPath = "/api/v1/runway/text_to_video"
	extendVideoPath = "/api/v1/runway/extend_video"
)

// Each endpoint targets a fixed model that is not sent on the wire, so the
// model is injected only into a validation copy of the request body.
const (
	textToVideoModel = "runway"
	extendVideoModel = "runway"
)

// validateAction validates a compacted request body against one contract
// action, injecting the endpoint's fixed model (never posted) so contract
// model-membership and per-field checks apply.
func validateAction(action, model string, body map[string]any) error {
	withModel := make(map[string]any, len(body)+1)
	for key, value := range body {
		withModel[key] = value
	}
	withModel["model"] = model
	return core.ValidateParams(contractSchema[action], withModel)
}

// Client provides Runway video generation and extension operations.
type Client struct {
	base.Base
	TextToVideo *TextToVideo
	ExtendVideo *ExtendVideo
}

// NewClient creates a Runway client with the given options.
func NewClient(opts ...option.ClientOption) (*Client, error) {
	resolved, err := option.ResolveClientOptions(opts...)
	if err != nil {
		return nil, err
	}
	httpClient, err := core.NewHTTPClient(resolved)
	if err != nil {
		return nil, err
	}
	return NewClientWithHTTP(httpClient), nil
}

// NewClientWithHTTP creates a Runway client with a pre-configured HTTP transport.
func NewClientWithHTTP(httpClient core.HTTPClient) *Client {
	return &Client{
		Base:        base.New(httpClient),
		TextToVideo: &TextToVideo{http: httpClient},
		ExtendVideo: &ExtendVideo{http: httpClient},
	}
}

// TextToVideo generates video from a text prompt. Optionally set FirstFrameImageURL
// to use an image as the opening frame, turning this into image-to-video generation.
type TextToVideo struct{ http core.HTTPClient }

// ExtendVideo appends additional footage to a previously generated video,
// continuing from where the source task left off. Requires the SourceTaskID
// of a completed TextToVideo or ExtendVideo task.
type ExtendVideo struct{ http core.HTTPClient }

// Create submits a text-to-video task and returns immediately with a task id.
func (r *TextToVideo) Create(ctx context.Context, params TextToVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := validateAction("text-to-video", textToVideoModel, body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, textToVideoPath, body, requestOptions)
}

// Get fetches the current status of a text-to-video task by id.
func (r *TextToVideo) Get(ctx context.Context, id string, opts ...option.RequestOption) (*TaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[TaskResponse](ctx, r.http, core.ResourcePath(textToVideoPath, id), requestOptions)
}

// Run submits a text-to-video task and polls until it completes.
func (r *TextToVideo) Run(ctx context.Context, params TextToVideoParams, opts ...option.RequestOption) (*TaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) {
		return r.Create(ctx, params, opts...)
	}, func(ctx context.Context, id string) (*TaskResponse, error) {
		return r.Get(ctx, id, opts...)
	}, pollingOptions)
}

// Create submits a video-extension task and returns immediately with a task id.
func (r *ExtendVideo) Create(ctx context.Context, params ExtendVideoParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	body := core.CompactParams(params)
	if err := validateAction("extend-video", extendVideoModel, body); err != nil {
		return nil, err
	}
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, extendVideoPath, body, requestOptions)
}

// Get fetches the current status of a video-extension task by id.
func (r *ExtendVideo) Get(ctx context.Context, id string, opts ...option.RequestOption) (*TaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[TaskResponse](ctx, r.http, core.ResourcePath(extendVideoPath, id), requestOptions)
}

// Run submits a video-extension task and polls until it completes.
func (r *ExtendVideo) Run(ctx context.Context, params ExtendVideoParams, opts ...option.RequestOption) (*TaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) {
		return r.Create(ctx, params, opts...)
	}, func(ctx context.Context, id string) (*TaskResponse, error) {
		return r.Get(ctx, id, opts...)
	}, pollingOptions)
}

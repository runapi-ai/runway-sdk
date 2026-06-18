// Package runway provides the Runway Gen-4 video generation API client.
package runway

// RunwayOutputResolution controls the output video resolution. Required for both TextToVideo and ExtendVideo.
type RunwayOutputResolution string

// RunwayAspectRatio controls the output video aspect ratio.
type RunwayAspectRatio string

// TaskStatus is the async task lifecycle state (e.g. "processing", "completed", "failed").
type TaskStatus string

const (
	// OutputResolution720p generates video at 720p (1280x720). Faster and lower cost.
	OutputResolution720p RunwayOutputResolution = "720p"
	// OutputResolution1080p generates video at 1080p (1920x1080). Higher detail, higher cost.
	OutputResolution1080p RunwayOutputResolution = "1080p"
)

const (
	Aspect16x9 RunwayAspectRatio = "16:9"
	Aspect9x16 RunwayAspectRatio = "9:16"
	Aspect1x1  RunwayAspectRatio = "1:1"
	Aspect4x3  RunwayAspectRatio = "4:3"
	Aspect3x4  RunwayAspectRatio = "3:4"
)

// TextToVideoParams configures video generation from a text prompt.
// Set FirstFrameImageURL to use an image as the opening frame (image-to-video mode).
// OutputResolution is required; AspectRatio is only used for pure text-to-video (no first frame image).
type TextToVideoParams struct {
	Prompt             string                 `json:"prompt" help:"required; video description"`
	DurationSeconds    int                    `json:"duration_seconds" help:"required; duration in seconds"`
	OutputResolution   RunwayOutputResolution `json:"output_resolution" help:"required; output resolution"`
	FirstFrameImageURL string                 `json:"first_frame_image_url,omitempty" help:"optional; first frame image URL for image-to-video"`
	AspectRatio        RunwayAspectRatio      `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	Watermark          string                 `json:"watermark,omitempty" help:"optional; watermark text"`
	CallbackURL        string                 `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// ExtendVideoParams configures video extension from a prior task.
// SourceTaskID must reference a completed TextToVideo or ExtendVideo task.
// OutputResolution must match the resolution of the source task.
type ExtendVideoParams struct {
	SourceTaskID     string                 `json:"source_task_id" help:"required; source task ID to extend"`
	Prompt           string                 `json:"prompt" help:"required; extension description"`
	OutputResolution RunwayOutputResolution `json:"output_resolution" help:"required; output resolution"`
	Watermark        string                 `json:"watermark,omitempty" help:"optional; watermark text"`
	CallbackURL      string                 `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// AsyncTaskResponse carries the task ID, lifecycle status, and error for all Runway async operations.
type AsyncTaskResponse struct {
	ID     string     `json:"id"`
	Status TaskStatus `json:"status"`
	Error  string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

// Video holds a URL to a generated video file.
type Video struct {
	ID  string `json:"id,omitempty"`
	URL string `json:"url"`
}

// Image holds a URL to an image (e.g. a first-frame preview).
type Image struct {
	URL string `json:"url"`
}

// TaskResponse is the result of a completed TextToVideo or ExtendVideo task.
// Videos contains the generated output. SourceTaskID is set on ExtendVideo results
// and references the task that was extended.
type TaskResponse struct {
	AsyncTaskResponse
	Videos       []Video `json:"videos,omitempty"`
	Images       []Image `json:"images,omitempty"`
	SourceTaskID string  `json:"source_task_id,omitempty"`
}

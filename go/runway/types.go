package runway

type RunwayOutputResolution string

type RunwayAspectRatio string

type TaskStatus string

const (
	OutputResolution720p  RunwayOutputResolution = "720p"
	OutputResolution1080p RunwayOutputResolution = "1080p"
)

const (
	Aspect16x9 RunwayAspectRatio = "16:9"
	Aspect9x16 RunwayAspectRatio = "9:16"
	Aspect1x1  RunwayAspectRatio = "1:1"
	Aspect4x3  RunwayAspectRatio = "4:3"
	Aspect3x4  RunwayAspectRatio = "3:4"
)

type TextToVideoParams struct {
	Prompt             string                 `json:"prompt" help:"required; video description"`
	DurationSeconds    int                    `json:"duration_seconds" help:"required; duration in seconds"`
	OutputResolution   RunwayOutputResolution `json:"output_resolution" help:"required; output resolution"`
	FirstFrameImageURL string                 `json:"first_frame_image_url,omitempty" help:"optional; first frame image URL for image-to-video"`
	AspectRatio        RunwayAspectRatio      `json:"aspect_ratio,omitempty" help:"optional; output aspect ratio"`
	Watermark          string                 `json:"watermark,omitempty" help:"optional; watermark text"`
	CallbackURL        string                 `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

type ExtendVideoParams struct {
	SourceTaskID     string                 `json:"source_task_id" help:"required; source task ID to extend"`
	Prompt           string                 `json:"prompt" help:"required; extension description"`
	OutputResolution RunwayOutputResolution `json:"output_resolution" help:"required; output resolution"`
	Watermark        string                 `json:"watermark,omitempty" help:"optional; watermark text"`
	CallbackURL      string                 `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

type AsyncTaskResponse struct {
	ID     string     `json:"id"`
	Status TaskStatus `json:"status"`
	Error  string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

type Video struct {
	ID  string `json:"id,omitempty"`
	URL string `json:"url"`
}

type Image struct {
	URL string `json:"url"`
}

type TaskResponse struct {
	AsyncTaskResponse
	Videos       []Video `json:"videos,omitempty"`
	Images       []Image `json:"images,omitempty"`
	SourceTaskID string  `json:"source_task_id,omitempty"`
}

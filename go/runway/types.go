package runway

type RunwayQuality string

type RunwayAspectRatio string

type TaskStatus string

const (
	Quality720p  RunwayQuality = "720p"
	Quality1080p RunwayQuality = "1080p"
)

const (
	Aspect16x9 RunwayAspectRatio = "16:9"
	Aspect9x16 RunwayAspectRatio = "9:16"
	Aspect1x1  RunwayAspectRatio = "1:1"
	Aspect4x3  RunwayAspectRatio = "4:3"
	Aspect3x4  RunwayAspectRatio = "3:4"
)

type TextToVideoParams struct {
	Prompt      string            `json:"prompt" help:"required; video description"`
	Duration    int               `json:"duration" help:"required; 5 or 10 seconds"`
	Quality     RunwayQuality     `json:"quality" help:"required; 720p or 1080p"`
	ImageURL    string            `json:"image_url,omitempty" help:"optional; image-to-video source URL"`
	AspectRatio RunwayAspectRatio `json:"aspect_ratio,omitempty" help:"optional; required for text-to-video; 16:9, 9:16, 1:1, 4:3, 3:4"`
	Watermark   string            `json:"watermark,omitempty" help:"optional; watermark text"`
	CallbackURL string            `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

type ExtendVideoParams struct {
	TaskID      string        `json:"task_id" help:"required; source task ID to extend"`
	Prompt      string        `json:"prompt" help:"required; extension description"`
	ImageURL    string        `json:"image_url" help:"required; image URL from the source task cover frame"`
	Quality     RunwayQuality `json:"quality" help:"required; 720p or 1080p"`
	Watermark   string        `json:"watermark,omitempty" help:"optional; watermark text"`
	CallbackURL string        `json:"callback_url,omitempty" help:"optional; webhook URL"`
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

type TaskResponse struct {
	AsyncTaskResponse
	Videos       []Video `json:"videos,omitempty"`
	ImageURL     string  `json:"image_url,omitempty"`
	ParentTaskID string  `json:"parent_task_id,omitempty"`
}

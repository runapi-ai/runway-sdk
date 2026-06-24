package ai.runapi.runway.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for text to video operations. */
public final class TextToVideoParams {
  private final String model;
  private final String prompt;
  private final Integer durationSeconds;
  private final String outputResolution;
  private final String firstFrameImageUrl;
  private final String aspectRatio;
  private final Boolean watermark;
  private final String callbackUrl;

  private TextToVideoParams(Builder builder) {
    this.model = builder.model;
    this.prompt = RunwayParamUtils.requireNonBlank(builder.prompt, "prompt");
    this.durationSeconds = java.util.Objects.requireNonNull(builder.durationSeconds, "durationSeconds");
    this.outputResolution = RunwayParamUtils.requireNonBlank(builder.outputResolution, "outputResolution");
    this.firstFrameImageUrl = builder.firstFrameImageUrl;
    this.aspectRatio = builder.aspectRatio;
    this.watermark = builder.watermark;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new TextToVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "runway/text-to-video";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", RunwayParamUtils.wireValue(model));
    raw.put("prompt", RunwayParamUtils.wireValue(prompt));
    raw.put("duration_seconds", RunwayParamUtils.wireValue(durationSeconds));
    raw.put("output_resolution", RunwayParamUtils.wireValue(outputResolution));
    raw.put("first_frame_image_url", RunwayParamUtils.wireValue(firstFrameImageUrl));
    raw.put("aspect_ratio", RunwayParamUtils.wireValue(aspectRatio));
    raw.put("watermark", RunwayParamUtils.wireValue(watermark));
    raw.put("callback_url", RunwayParamUtils.wireValue(callbackUrl));
    return RunwayParamUtils.compact(raw);
  }



  /** Builder for {@link TextToVideoParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private Integer durationSeconds;
    private String outputResolution;
    private String firstFrameImageUrl;
    private String aspectRatio;
    private Boolean watermark;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(TextToVideoModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = RunwayParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = RunwayParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the duration in seconds. */
    public Builder durationSeconds(int value) {
      this.durationSeconds = value;
      return this;
    }

    /** Sets the output resolution. */
    public Builder outputResolution(String value) {
      this.outputResolution = RunwayParamUtils.requireNonBlank(value, "outputResolution");
      return this;
    }

    /** Sets the first frame image URL. */
    public Builder firstFrameImageUrl(String value) {
      this.firstFrameImageUrl = RunwayParamUtils.requireNonBlank(value, "firstFrameImageUrl");
      return this;
    }

    /** Sets the output aspect ratio. */
    public Builder aspectRatio(String value) {
      this.aspectRatio = RunwayParamUtils.requireNonBlank(value, "aspectRatio");
      return this;
    }

    /** Sets the watermark toggle. */
    public Builder watermark(boolean value) {
      this.watermark = value;
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = RunwayParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Builds immutable text to video parameters. */
    public TextToVideoParams build() {
      return new TextToVideoParams(this);
    }
  }
}

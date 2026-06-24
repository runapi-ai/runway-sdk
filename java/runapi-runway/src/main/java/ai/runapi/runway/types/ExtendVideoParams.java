package ai.runapi.runway.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for extend video operations. */
public final class ExtendVideoParams {
  private final String model;
  private final String sourceTaskId;
  private final String prompt;
  private final String outputResolution;
  private final Boolean watermark;
  private final String callbackUrl;

  private ExtendVideoParams(Builder builder) {
    this.model = builder.model;
    this.sourceTaskId = RunwayParamUtils.requireNonBlank(builder.sourceTaskId, "sourceTaskId");
    this.prompt = RunwayParamUtils.requireNonBlank(builder.prompt, "prompt");
    this.outputResolution = RunwayParamUtils.requireNonBlank(builder.outputResolution, "outputResolution");
    this.watermark = builder.watermark;
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new ExtendVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "runway/extend-video";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", RunwayParamUtils.wireValue(model));
    raw.put("source_task_id", RunwayParamUtils.wireValue(sourceTaskId));
    raw.put("prompt", RunwayParamUtils.wireValue(prompt));
    raw.put("output_resolution", RunwayParamUtils.wireValue(outputResolution));
    raw.put("watermark", RunwayParamUtils.wireValue(watermark));
    raw.put("callback_url", RunwayParamUtils.wireValue(callbackUrl));
    return RunwayParamUtils.compact(raw);
  }



  /** Builder for {@link ExtendVideoParams}. */
  public static final class Builder {
    private String model;
    private String sourceTaskId;
    private String prompt;
    private String outputResolution;
    private Boolean watermark;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(ExtendVideoModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = RunwayParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the source task ID. */
    public Builder sourceTaskId(String value) {
      this.sourceTaskId = RunwayParamUtils.requireNonBlank(value, "sourceTaskId");
      return this;
    }

    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = RunwayParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the output resolution. */
    public Builder outputResolution(String value) {
      this.outputResolution = RunwayParamUtils.requireNonBlank(value, "outputResolution");
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

    /** Builds immutable extend video parameters. */
    public ExtendVideoParams build() {
      return new ExtendVideoParams(this);
    }
  }
}

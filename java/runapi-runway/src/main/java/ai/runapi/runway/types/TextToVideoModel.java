package ai.runapi.runway.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for text to video operations. */
public final class TextToVideoModel extends RunwayValue {
  /** runway model slug. */
  public static final TextToVideoModel RUNWAY = new TextToVideoModel("runway");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public TextToVideoModel(String value) {
    super(value);
  }
}

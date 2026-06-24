package ai.runapi.runway.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for extend video operations. */
public final class ExtendVideoModel extends RunwayValue {
  /** runway model slug. */
  public static final ExtendVideoModel RUNWAY = new ExtendVideoModel("runway");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public ExtendVideoModel(String value) {
    super(value);
  }
}

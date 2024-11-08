import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MapItem {
  private static final Logger LOGGER = Logger.getLogger(FertilizerPartOne.class.getName());
  Long destStart;
  Long srcStart;
  Long step;
  Long upperBoundExclusive;

  public MapItem(Long destStart, Long srcStart, Long step) {
    this.destStart = destStart;
    this.srcStart = srcStart;
    this.step = step;
    this.upperBoundExclusive = this.srcStart + this.step;
    // initLogger(Level.FINE, Level.FINE);
  }

  public String toString() {
    return String.format(
        "{destStart=%s, srcStart=%s, step=%s}", this.destStart, this.srcStart, this.step);
  }

  public Long mapValue(Long val) {
    if (val < this.srcStart) return val;
    else {
      if (val >= this.upperBoundExclusive) return val;
      else return this.destStart + (val - this.srcStart);
    }
  }

  // start: 50, step: 10, seed: 56.
  // destStart: 40.
  // result: 44

  public static void initLogger(Level loggerLevel, Level handlerLevel) {
    LOGGER.setLevel(loggerLevel);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(handlerLevel);
    handler.setFormatter(new CustomFormatter());
    LOGGER.addHandler(handler);
  }

  static class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[%s] %s%n", record.getLevel(), record.getMessage());
    }
  }
}

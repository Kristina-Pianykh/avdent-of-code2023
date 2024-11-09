import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BoatRacePartOne {
  private static final Logger LOGGER = Logger.getLogger(BoatRacePartOne.class.getName());

  public static void initLogger(Level logLevel) {
    LOGGER.setLevel(logLevel);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(logLevel);
    handler.setFormatter(new CustomFormatter());
    LOGGER.addHandler(handler);
  }

  public static Integer waysToBeatRecord(Map<String, Integer> race) {
    int res = 0;
    for (int i = 1; i < race.get("Time"); i++) {
      int leftTime = race.get("Time") - i;
      int coveredDist = leftTime * i;
      if (race.get("Distance") < coveredDist) res++;
    }
    return res;
  }

  public static void main(String[] args) {
    initLogger(Level.FINE);

    List<Map<String, Integer>> races = new ArrayList<>();
    List<Integer> times = Arrays.asList(49, 97, 94, 94);
    List<Integer> distances = Arrays.asList(263, 1532, 1378, 1851);

    for (int i = 0; i < times.size(); i++) {
      Map<String, Integer> race = Map.of("Time", times.get(i), "Distance", distances.get(i));
      races.add(race);
    }
    // LOGGER.fine(timeDistanceList.toString());
    Long totalRes = 1L;
    for (var race : races) {
      Integer raceRes = waysToBeatRecord(race);
      LOGGER.fine(
          String.format(
              "Race: %s, number of ways to beat the record: %d", race.toString(), raceRes));
      totalRes *= Long.valueOf(raceRes);
    }
    System.out.println("result: " + totalRes);
  }

  static class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[%s] %s%n", record.getLevel(), record.getMessage());
    }
  }
}

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class BoatRacePartTwo {
  public static long waysToBeatRecord(long time, long distance) {
    long res = 0l;
    for (long i = 1; i < time; i++) {
      long leftTime = time - i;
      long coveredDist = leftTime * i;
      if (distance < coveredDist) res++;
    }
    return res;
  }

  public static void main(String[] args) {
    long time = 49979494l;
    long distance = 263153213781851l;
    long raceRes = waysToBeatRecord(time, distance);
    System.out.println("result: " + raceRes);
  }

  static class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[%s] %s%n", record.getLevel(), record.getMessage());
    }
  }
}

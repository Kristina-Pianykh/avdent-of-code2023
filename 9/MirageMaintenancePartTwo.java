import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class MirageMaintenancePartTwo {
  private static final Logger LOG = Logger.getLogger(MirageMaintenancePartTwo.class.getName());

  public static void initLogger(Level consoleLevel, Level fileLevel) {
    LOG.setLevel(consoleLevel);
    ConsoleHandler cHandler = new ConsoleHandler();
    try {
      FileHandler fHandler = new FileHandler("./log");
      fHandler.setLevel(fileLevel);
      fHandler.setFormatter(new CustomFormatter());
      LOG.addHandler(fHandler);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
    cHandler.setLevel(consoleLevel);
    cHandler.setFormatter(new CustomFormatter());
    LOG.addHandler(cHandler);
  }

  public static void main(String[] args) {
    initLogger(Level.INFO, Level.INFO);

    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);

    long totalSum = 0l;

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;

      while ((line = reader.readLine()) != null) {
        totalSum += predictMeasurement(line);
      }
      System.out.println("result: " + totalSum);

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }

  static long predictMeasurement(String line) {
    LOG.fine(String.format("line: %s", line));
    ArrayList<Long> vals =
        Arrays.asList(line.split("\s+")).stream()
            .map(e -> Long.valueOf(e.trim()))
            .collect(Collectors.toCollection(ArrayList::new));
    LOG.fine(vals.toString());
    ArrayList<ArrayList<Long>> diffPyramid = new ArrayList<>();
    ArrayList<Long> criticalVals = new ArrayList<>();
    long criticalVal = vals.get(0);
    criticalVals.add(criticalVal);
    LOG.fine(String.format("criticalVal=%s", criticalVal));

    ArrayList<Long> diffs;
    while (true) {
      boolean allZeros = vals.stream().allMatch(e -> e.equals(0l));
      if (allZeros) break;

      diffs = getDiff(vals);
      assert !diffs.isEmpty();
      assert diffs.size() == vals.size() - 1;
      diffPyramid.add(diffs);
      LOG.fine(String.format("diffs: %s", diffs.toString()));

      criticalVal = diffs.get(0);
      LOG.fine(String.format("criticalVal=%s", criticalVal));
      criticalVals.add(criticalVal);
      vals = diffPyramid.get(diffPyramid.size() - 1);
    }

    LOG.fine(String.format("criticalValues=%s", criticalVals.toString()));
    return newPrediction(criticalVals);
  }

  static ArrayList<Long> getDiff(ArrayList<Long> vals) {
    ArrayList<Long> diffs = new ArrayList<>();
    LOG.fine(String.format("getDiff(): vals=%s", vals));
    for (int i = 1; i < vals.size(); i++) {
      long diff = vals.get(i) - vals.get(i - 1);
      diffs.add(diff);
    }
    assert vals.size() - 1 == diffs.size();
    return diffs;
  }

  static long newPrediction(ArrayList<Long> vals) {
    long res = 0l;
    for (int i = vals.size() - 2; i >= 0; i--) {
      res = vals.get(i) - res;
      LOG.fine(String.format("new prediction val: %d", res));
    }
    return res;
  }

  static class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[%s] %s%n", record.getLevel(), record.getMessage());
    }
  }
}

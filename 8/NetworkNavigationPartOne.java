import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkNavigationPartOne {
  private static final Logger LOGGER = Logger.getLogger(NetworkNavigationPartOne.class.getName());
  private static HashMap<String, List<String>> map = new HashMap<>();
  private static String instructions;

  public static void initLogger(Level logLevel) {
    LOGGER.setLevel(logLevel);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(logLevel);
    handler.setFormatter(new CustomFormatter());
    LOGGER.addHandler(handler);
  }

  public static void main(String[] args) {
    initLogger(Level.INFO);

    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);
    final Pattern p = Pattern.compile("^\\(([A-Z]+), ([A-Z]+)\\)$");

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;

      while ((line = reader.readLine()) != null) {
        if (!line.contains("=") && !line.isEmpty()) instructions = line.trim();
        else if (line.contains("=")) {
          String key = line.split("=")[0].trim();
          Matcher m = p.matcher(line.split("=")[1].trim());
          if (!m.find()) {
            System.err.println("failed to match a pattern");
            System.exit(1);
          }
          map.put(key, Arrays.asList(m.group(1), m.group(2)));
          LOGGER.fine(String.format("%s=%s", key, Arrays.asList(m.group(1), m.group(2))));
        }
      }

      int steps = 0;
      int instructionIdx = 0;
      String key = "AAA";

      while (true) {
        if (key.equals("ZZZ")) break;

        char instruction = instructions.charAt(instructionIdx);

        switch (instruction) {
          case 'L':
            key = map.get(key).get(0);
            break;
          case 'R':
            key = map.get(key).get(1);
            break;
          default:
            System.err.println("wrong instruction, expected 'L' or 'R'");
            System.exit(1);
        }
        steps++;

        if (instructionIdx == instructions.length() - 1) instructionIdx = 0;
        else instructionIdx++;

        LOGGER.fine(
            String.format(
                "steps=%d, instructionIdx=%d, nextInstructionIdx=%d, nextLookupKey=%s",
                steps, instructionIdx - 1, instructionIdx, key));
      }
      System.out.println("result: " + steps);

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }

  static class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[%s] %s%n", record.getLevel(), record.getMessage());
    }
  }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NetworkNavigationPartTwo {
  private static final Logger LOGGER = Logger.getLogger(NetworkNavigationPartTwo.class.getName());
  private static HashMap<String, List<String>> map = new HashMap<>();
  private static List<Integer> instructions;
  private static Map<Character, Integer> translation = Map.of('L', 0, 'R', 1);

  public static void initLogger(Level consoleLevel, Level fileLevel) {
    LOGGER.setLevel(fileLevel);
    ConsoleHandler cHandler = new ConsoleHandler();
    // try {
    //   FileHandler fHandler = new FileHandler("./logall");
    //   fHandler.setLevel(fileLevel);
    //   fHandler.setFormatter(new CustomFormatter());
    //   LOGGER.addHandler(fHandler);
    // } catch (IOException e) {
    //   System.err.println(e.getMessage());
    //   System.exit(1);
    // }
    cHandler.setLevel(consoleLevel);
    cHandler.setFormatter(new CustomFormatter());
    LOGGER.addHandler(cHandler);
  }

  public static void main(String[] args) {
    initLogger(Level.INFO, Level.INFO);

    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);
    final Pattern p = Pattern.compile("^\\(([A-Z]+), ([A-Z]+)\\)$");

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;

      while ((line = reader.readLine()) != null) {
        if (!line.contains("=") && !line.isEmpty())
          instructions =
              line.trim()
                  .chars()
                  .mapToObj(i -> (char) i)
                  .mapToInt(e -> Integer.valueOf(translation.get(e)))
                  .boxed()
                  .collect(Collectors.toList());
        else if (line.contains("=")) {
          String key = line.split("=")[0].trim();
          Matcher m = p.matcher(line.split("=")[1].trim());
          if (!m.find()) {
            System.err.println("failed to match a pattern");
            System.exit(1);
          }
          map.put(key, Arrays.asList(m.group(1), m.group(2)));
        }
      }

      ArrayList<Node> nodes =
          map.keySet().stream()
              .filter(e -> e.endsWith("A"))
              .map(Node::new)
              .collect(Collectors.toCollection(ArrayList::new));

      LOGGER.fine(String.format("startNodes: %s", nodes.toString()));
      List<Long> indices =
          List.of(
              11653L, 12737L, 14363L, 16531L, 19241L,
              19783L); // first indices for each of the nodes

      long i = 2;
      long res = 0;

      outerLoop:
      while (true) {
        res = i * indices.get(0);

        for (int j = 1; j < indices.size(); j++) {
          if (res % indices.get(j) != 0) break;
          if (j == indices.size() - 1) break outerLoop;
        }

        i++;
      }
      System.out.println("res: " + res);

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

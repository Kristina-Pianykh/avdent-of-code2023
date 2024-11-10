import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CamelCardsPartTwo {
  private static final Logger LOGGER = Logger.getLogger(CamelCardsPartTwo.class.getName());
  static List<Character> labels =
      Arrays.asList('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed();

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
    List<Hand> hands = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;

      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\s+");
        Hand hand = new Hand(parts[0], Integer.valueOf(parts[1]));
        hand.computeRankJ();
        assert hand != null;
        LOGGER.fine(String.format("%s\n", hand.toString()));
        hands.add(hand);
      }

      Comparator<Hand> customComparator =
          ((Hand o1, Hand o2) -> {
            if (o1.rank.compareTo(o2.rank) != 0) {
              return o1.rank.compareTo(o2.rank);
            } else {
              char[] char1 = o1.hand.toCharArray();
              char[] char2 = o2.hand.toCharArray();

              for (int i = 0; i < o1.hand.length(); i++) {
                if (labels.indexOf(char1[i]) > labels.indexOf(char2[i])) {
                  return 1;
                } else if (labels.indexOf(char1[i]) < labels.indexOf(char2[i])) return -1;
                else continue;
              }
            }
            return 0;
          });
      hands.sort(customComparator);

      long res = hands.stream().mapToLong(e -> ((hands.indexOf(e) + 1) * e.bid)).sum();
      System.out.println("result: " + res);

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

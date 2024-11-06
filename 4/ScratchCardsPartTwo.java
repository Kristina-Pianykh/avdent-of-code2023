import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.IntStream;

public class ScratchCardsPartTwo {
  private static final Logger LOGGER = Logger.getLogger(ScratchCardsPartOne.class.getName());

  public static void initLogger(Level loggerLevel, Level handlerLevel) {
    LOGGER.setLevel(loggerLevel);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(handlerLevel);
    handler.setFormatter(new CustomFormatter());
    LOGGER.addHandler(handler);
  }

  public static List<Integer> intersect(List<Integer> winningCard, List<Integer> handCard) {
    List<Integer> wonNumbers = new ArrayList<>();
    for (Integer num : handCard) {
      if (winningCard.contains(num)) wonNumbers.add(num);
    }
    return wonNumbers;
  }

  public static int getInstanceNumber(List<Integer> totalCardCopies, int cardId) {
    Collections.sort(totalCardCopies);
    int lowerIdx = totalCardCopies.indexOf(cardId);
    int upperIdx = totalCardCopies.lastIndexOf(cardId);
    return upperIdx - lowerIdx + 1;
  }

  public static void main(String[] args) {
    initLogger(Level.INFO, Level.INFO);

    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      List<Integer> totalCardCopies = new ArrayList<>();

      while ((line = reader.readLine()) != null) {
        LOGGER.fine(line);
        int cardId = Integer.valueOf(line.split(":")[0].split("\s+")[1]);
        totalCardCopies.add(cardId);

        List<String> cards =
            Arrays.asList(line.split(":")[1].split("\\|")).stream().map(e -> e.trim()).toList();
        List<Integer> winningCard =
            Arrays.asList(cards.get(0).split("\s+")).stream()
                .map(e -> Integer.valueOf(e.trim()))
                .toList();
        List<Integer> handCard =
            Arrays.asList(cards.get(1).split("\s+")).stream()
                .map(e -> Integer.valueOf(e.trim()))
                .toList();

        List<Integer> wonNumbers = intersect(winningCard, handCard);

        if (!wonNumbers.isEmpty()) {
          List<Integer> copies =
              IntStream.range(cardId + 1, cardId + wonNumbers.size() + 1)
                  .mapToObj(e -> Integer.valueOf(e))
                  .toList();
          int instanceNumber = getInstanceNumber(totalCardCopies, cardId);
          for (int i = 0; i < instanceNumber; i++) {
            totalCardCopies.addAll(copies);
          }
        }
      }
      System.out.println(String.format("total won scratch cards: %d", totalCardCopies.size()));

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

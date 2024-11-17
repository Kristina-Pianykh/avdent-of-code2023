import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PipesPartOne {
  private static final Logger LOG = Logger.getLogger(PipesPartOne.class.getName());

  public static void initLogger(Level consoleLevel, Level fileLevel) {
    LOG.setLevel(consoleLevel);
    LOG.setUseParentHandlers(false);
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
    initLogger(Level.FINE, Level.FINE);

    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);
    List<Character> validStartPipes = Arrays.asList('|', '-', 'J', 'L', 'F', '7');
    ArrayList<String> input = new ArrayList<>();
    Position startPos = null;

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      int lineIdx = 0;

      while ((line = reader.readLine()) != null) {
        if (line.contains("S")) {

          startPos = new Position(line.indexOf('S'), lineIdx);
          LOG.fine(String.format("found 'S' at %s", startPos));
        }
        input.add(line);
        lineIdx++;
      }
    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
    assert startPos != null;
    assert !input.isEmpty();

    long steps = 0;

    outerLoop:
    for (Character possibleStartPipe : validStartPipes) {
      LOG.fine(String.format("Assuming 'S' to be %c", possibleStartPipe));
      char ch = possibleStartPipe;
      Pipe prevPipe = null;
      Pipe pipe = null;
      String line = null;
      steps = 0;

      try {
        prevPipe = new Pipe(ch, startPos);
        prevPipe.setFrom(0);
        prevPipe.setTo(1);

        line = input.get(prevPipe.to.y);
        ch = line.charAt(prevPipe.to.x);

        while (true) {

          if (ch == 'S') {
            break outerLoop;
          }

          try {
            pipe = new Pipe(ch, prevPipe.to);

            if (!pipe.conns.contains(prevPipe.pos)) {
              LOG.fine(
                  String.format(
                      "pipe %s doesn't match with with the previous pipe", pipe.toString()));
              break;
            }

            for (int j = 0; j < pipe.conns.size(); j++) {
              if (pipe.conns.get(j).equals(prevPipe.pos)) {
                pipe.setFrom(j);
                pipe.setTo(pipe.conns.size() - j - 1);
              }
            }
            assert pipe.from != null;
            assert pipe.to != null;

            line = input.get(pipe.to.y);
            ch = line.charAt(pipe.to.x);
            steps++;

            LOG.fine(String.format("prevPipe: %s", prevPipe.toString()));
            LOG.fine(String.format("step: %d, currentPipe: %s", steps, pipe.toString()));
            LOG.fine(String.format("next char=%c\n", ch));

            prevPipe = pipe;
          } catch (ReachedGroundException e) {
            LOG.fine(e.getMessage());
            break;
          } catch (InvalidPipeCharException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
          }
        }

      } catch (ReachedGroundException e) {
        LOG.fine(e.getMessage());
        break;
      } catch (InvalidPipeCharException e) {
        LOG.severe(e.getMessage());
        System.exit(1);
      }
    }
    LOG.info("res: " + ((steps / 2) + 1));
  }

  static class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[%s] %s%n", record.getLevel(), record.getMessage());
    }
  }
}

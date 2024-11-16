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
    // try {
    //   FileHandler fHandler = new FileHandler("./log");
    //   fHandler.setLevel(fileLevel);
    //   fHandler.setFormatter(new CustomFormatter());
    //   LOG.addHandler(fHandler);
    // } catch (IOException e) {
    //   System.err.println(e.getMessage());
    //   System.exit(1);
    // }
    cHandler.setLevel(consoleLevel);
    cHandler.setFormatter(new CustomFormatter());
    LOG.addHandler(cHandler);
  }

  public static void main(String[] args) {
    initLogger(Level.INFO, Level.INFO);

    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);
    List<Character> validStartPipes = Arrays.asList('|', '-', 'J', 'L', 'F', '7');
    ArrayList<String> input = new ArrayList<>();
    List<Integer> startPos = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      int lineIdx = 0;

      while ((line = reader.readLine()) != null) {
        if (line.contains("S")) {
          startPos = Arrays.asList(line.indexOf('S'), lineIdx);
          LOG.fine(String.format("found 'S' at x=%d, y=%d", startPos.get(0), startPos.get(1)));
        }
        input.add(line);
        lineIdx++;
      }

      long steps = 0;
      outerLoop:
      for (Character possibleStartPipe : validStartPipes) {
        LOG.fine(String.format("Assuming 'S' to be %c", possibleStartPipe));
        Pipe pipe;
        char ch = possibleStartPipe;
        List<Integer> from = new ArrayList<>();
        List<Integer> to = new ArrayList<>();
        List<Integer> pos = new ArrayList<>();

        try {
          for (int i = 0; i < 2; i++) {
            steps = 0;
            pos = startPos;
            ch = possibleStartPipe;
            pipe = new Pipe(ch, pos);
            from = pipe.connections.get(i);
            to = pipe.connections.get(pipe.connections.size() - 1 - i);
            LOG.fine(
                String.format(
                    "ch=%c, pos=%s, from=%s, to=%s",
                    ch, pipe.pos.toString(), from.toString(), to.toString()));

            line = input.get(to.get(1));
            ch = line.charAt(to.get(0));

            while (true) {

              if (ch == 'S') {
                break outerLoop;
              }

              try {
                pipe = new Pipe(ch, to);
                from =
                    pipe.connections.get(0).equals(pos)
                        ? pipe.connections.get(0)
                        : pipe.connections.get(1);
                to =
                    pipe.connections.get(0).equals(from)
                        ? pipe.connections.get(1)
                        : pipe.connections.get(0);

                if (!pipe.connections.contains(pos)) {
                  LOG.fine(
                      String.format(
                          "pipe %s doesn't match with with the previous pipe", pipe.toString()));
                  break;
                }

                line = input.get(to.get(1));
                ch = line.charAt(to.get(0));
                pos = pipe.pos;
                steps++;
                LOG.fine(String.format("step: %d, %s", steps, pipe.toString()));
              } catch (Exception e) {
                LOG.fine(e.getMessage());
              }
            }
          }

        } catch (Exception e) {
          LOG.severe(e.getMessage());
        }
      }
      LOG.info("res: " + ((steps / 2) + 1));

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

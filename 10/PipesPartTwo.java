import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class PipesPartTwo {
  private static final Logger LOG = Logger.getLogger(PipesPartTwo.class.getName());

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
    ArrayList<Pipe> loop = new ArrayList<>();
    ArrayList<Position> lCanvas = new ArrayList<>();
    ArrayList<Position> rCanvas = new ArrayList<>();

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

            loop.add(prevPipe);
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
    System.out.println("number of pipes in the loop: " + loop.size());
    // LOG.info("res: " + ((steps / 2) + 1));

    Pipe prevPipe = loop.get(loop.size() - 1);
    for (Pipe pipe : loop) {
      LOG.fine(pipe.toString());
      // determine the direction for creating left and right canvas
      // vertical move
      if (pipe.pos.x == prevPipe.pos.x) {
        // move down
        if (pipe.pos.y == prevPipe.pos.y + 1) {
          addToCanvasIfNotPipe(pipe.pos.x + 1, pipe.pos.y, loop, lCanvas);
          addToCanvasIfNotPipe(pipe.pos.x - 1, pipe.pos.y, loop, rCanvas);

          if (pipe.tag == 'L') {
            addToCanvasIfNotPipe(pipe.pos.x, pipe.pos.y + 1, loop, rCanvas);
            addToCanvasIfNotPipe(pipe.pos.x - 1, pipe.pos.y + 1, loop, rCanvas);

          } else if (pipe.tag == 'J') {
            addToCanvasIfNotPipe(pipe.pos.x, pipe.pos.y + 1, loop, lCanvas);
            addToCanvasIfNotPipe(pipe.pos.x + 1, pipe.pos.y + 1, loop, lCanvas);
          }
          // move up
        } else if (pipe.pos.y == prevPipe.pos.y - 1) {
          addToCanvasIfNotPipe(pipe.pos.x - 1, pipe.pos.y, loop, lCanvas);
          addToCanvasIfNotPipe(pipe.pos.x + 1, pipe.pos.y, loop, rCanvas);

          if (pipe.tag == '7') {
            addToCanvasIfNotPipe(pipe.pos.x, pipe.pos.y - 1, loop, rCanvas);
            addToCanvasIfNotPipe(pipe.pos.x + 1, pipe.pos.y - 1, loop, rCanvas);

          } else if (pipe.tag == 'F') {
            addToCanvasIfNotPipe(pipe.pos.x, pipe.pos.y - 1, loop, lCanvas);
            addToCanvasIfNotPipe(pipe.pos.x - 1, pipe.pos.y - 1, loop, lCanvas);
          }
        }
        // horizontal move
      } else if (pipe.pos.y == prevPipe.pos.y) {
        // move left
        if (pipe.pos.x == prevPipe.pos.x - 1) {
          addToCanvasIfNotPipe(pipe.pos.x, pipe.pos.y + 1, loop, lCanvas);
          addToCanvasIfNotPipe(pipe.pos.x, pipe.pos.y - 1, loop, rCanvas);

          if (pipe.tag == 'L') {
            addToCanvasIfNotPipe(pipe.pos.x - 1, pipe.pos.y, loop, lCanvas);
            addToCanvasIfNotPipe(pipe.pos.x - 1, pipe.pos.y + 1, loop, lCanvas);
          } else if (pipe.tag == 'F') {
            addToCanvasIfNotPipe(pipe.pos.x - 1, pipe.pos.y, loop, rCanvas);
            addToCanvasIfNotPipe(pipe.pos.x - 1, pipe.pos.y - 1, loop, rCanvas);
          }
          // move right
        } else if (pipe.pos.x == prevPipe.pos.x + 1) {
          addToCanvasIfNotPipe(pipe.pos.x, pipe.pos.y - 1, loop, lCanvas);
          addToCanvasIfNotPipe(pipe.pos.x, pipe.pos.y + 1, loop, rCanvas);

          if (pipe.tag == '7') {
            addToCanvasIfNotPipe(pipe.pos.x + 1, pipe.pos.y, loop, lCanvas);
            addToCanvasIfNotPipe(pipe.pos.x + 1, pipe.pos.y - 1, loop, lCanvas);

          } else if (pipe.tag == 'J') {
            addToCanvasIfNotPipe(pipe.pos.x + 1, pipe.pos.y, loop, rCanvas);
            addToCanvasIfNotPipe(pipe.pos.x + 1, pipe.pos.y + 1, loop, rCanvas);
          }
        }
      }
      prevPipe = pipe;
    }
    assert rCanvas.size() > 0;
    assert lCanvas.size() > 0;
    LOG.fine("size of rCanvas: " + rCanvas.size());
    LOG.fine("size of lCanvas: " + lCanvas.size());

    ArrayList<String> matrix = createEmptyMatrix();

    // writeToFile("output.txt", input);

    for (Position pos : lCanvas) {
      String line = matrix.get(pos.y);
      char[] arr = line.toCharArray();
      arr[pos.x] = 'L';
      LOG.fine(new String(arr));
      matrix.set(pos.y, new String(arr));
    }
    writeToFile("outputCanvasL.txt", matrix);

    for (Position pos : rCanvas) {
      String line = matrix.get(pos.y);
      char[] arr = line.toCharArray();
      arr[pos.x] = 'R';
      LOG.fine(new String(arr));
      matrix.set(pos.y, new String(arr));
    }
    writeToFile("outputCanvasLR.txt", matrix);

    for (Pipe pipe : loop) {
      String line = matrix.get(pipe.pos.y);
      char[] arr = line.toCharArray();
      arr[pipe.pos.x] = 'X';
      LOG.fine(new String(arr));
      matrix.set(pipe.pos.y, new String(arr));
    }
    writeToFile("outputCanvas.txt", matrix);

    for (Pipe pipe : loop) {
      String line = matrix.get(pipe.pos.y);
      char[] arr = line.toCharArray();
      if (arr[pipe.pos.x] != 'L' || arr[pipe.pos.x] != 'R') {
        arr[pipe.pos.x] = '*';
        matrix.set(pipe.pos.y, new String(arr));
      }
    }
    writeToFile("outputCanvasOnly.txt", matrix);

    int Rcount = 0;
    for (String line : matrix) {
      for (char ch : line.toCharArray()) {
        if (ch == 'R') Rcount++;
      }
    }

    int innerDotCnt = 0;
    for (int i = 62; i < 84; i++) {
      String line = matrix.get(i);
      char[] arr = line.toCharArray();
      for (int j = 58; j < 84; j++) {
        if (arr[j] == '.') innerDotCnt++;
      }
    }

    ArrayList<String> newMatrx = createEmptyMatrix();
    for (Pipe pipe : loop) {
      String line = matrix.get(pipe.pos.y);
      char[] arr = line.toCharArray();
      arr[pipe.pos.x] = 'X';
      matrix.set(pipe.pos.y, new String(arr));
    }
    for (int j = 0; j < matrix.size(); j++) {
      char[] arr = matrix.get(j).toCharArray();
      char[] newArr = newMatrx.get(j).toCharArray();
      for (int i = 0; i < arr.length; i++) {
        if (arr[i] == 'R') assert newArr[i] == '.';
      }
    }

    System.out.println("length of the loop: " + loop.size());
    System.out.println(
        "R count (without enclosed points, not part of the inner canvas): " + Rcount);
    System.out.println("Inner dot count: " + innerDotCnt);
    System.out.println("result: " + (Rcount + innerDotCnt));
  }

  public static void addToCanvasIfNotPipe(
      int x, int y, ArrayList<Pipe> loop, ArrayList<Position> canvas) {
    if (x >= 140 || y >= 140) return;
    if (isPipe(x, y, loop)) return;
    LOG.fine("not a pipe, adding to canvas");
    canvas.add(new Position(x, y));
  }

  public static boolean isPipe(int x, int y, ArrayList<Pipe> loop) {
    for (Pipe pipe : loop) {
      if (pipe.pos.x == x && pipe.pos.y == y) return true;
    }
    return false;
  }

  public static ArrayList<String> createEmptyMatrix() {
    int size = 140;
    ArrayList<String> matrix = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      matrix.add(".".repeat(140));
    }
    assert matrix.size() == size;
    assert matrix.get(0).length() == size;
    return matrix;
  }

  public static void writeToFile(String path, ArrayList<String> matrix) {
    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
      for (String line : matrix) {
        out.println(line);
      }
    } catch (IOException e) {
      LOG.severe(e.getMessage());
      System.exit(1);
    }
  }

  static class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[%s] %s%n", record.getLevel(), record.getMessage());
    }
  }
}

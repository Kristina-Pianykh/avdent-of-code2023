import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SchematicPartTwo {
  static Integer TOTAL_SUM = 0;

  public static boolean isDigit(char ch) {
    if (Integer.valueOf(ch) >= 48 && Integer.valueOf(ch) <= 57) return true;
    return false;
  }

  public static boolean isStar(char ch) {
    if (Integer.valueOf(ch) == 42) return true;
    return false;
  }

  public static Integer extractNum(char[] numArr, int lowerBoundIdx, int upperBoundIdx) {
    String number = "";
    for (int i = lowerBoundIdx; i <= upperBoundIdx; i++) {
      number += numArr[i];
    }
    return Integer.valueOf(number);
  }

  public static void removeUsedNumber(char[] numArr, int lowerBoundIdx, int upperBoundIdx) {
    for (int i = lowerBoundIdx; i <= upperBoundIdx; i++) {
      numArr[i] = 46;
    }
  }

  public static ArrayList<Integer> findAdjacentNum(String line, int starIdx) {
    ArrayList<Integer> nums = new ArrayList<>();
    char[] charArr = line.toCharArray();
    for (int i = starIdx - 1; i <= starIdx + 1; i++) {

      if (isDigit(charArr[i])) {
        int j = i;

        while (j >= 0 && isDigit(charArr[j])) {
          j = j - 1;
        }
        int lowerBoundIdx = j + 1;

        j = i;
        while (j < charArr.length && isDigit(charArr[j])) {
          j++;
        }
        int upperBoundIdx = j - 1;
        Integer number = extractNum(charArr, lowerBoundIdx, upperBoundIdx);
        nums.add(number);
        removeUsedNumber(charArr, lowerBoundIdx, upperBoundIdx);
      }
    }
    return nums;
  }

  public static void printBuffer(ArrayList<String> buffer) {
    System.out.println("\n===================");
    System.out.println("BUFFER");
    for (var i : buffer) System.out.println(i);
    System.out.println("===================");
  }

  public static void findAndAddExactlyTwoAdjacentNums(ArrayList<String> buffer, String currLine) {
    ArrayList<Integer> lineNums = new ArrayList<>();
    char[] numsCurrLine = currLine.toCharArray();

    for (int i = 0; i < currLine.length(); i++) {
      lineNums.clear();

      char ch = numsCurrLine[i];
      if (isStar(ch)) {
        for (int j = 0; j < buffer.size(); j++) {
          lineNums.addAll(findAdjacentNum(buffer.get(j), i));
        }
        if (lineNums.size() == 2) {
          TOTAL_SUM +=
              lineNums.stream().mapToInt(e -> e).reduce(1, (acc, element) -> acc * element);
        }
        lineNums.clear();
      }
    }
  }

  public static void main(String[] args) {
    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);
    ArrayList<String> buffer = new ArrayList<>();
    String currLine = null;

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      int lineIdx = 0;

      while ((line = reader.readLine()) != null) {
        // keep a sliding window of 3 lines
        if (buffer.size() == 3) buffer.removeFirst();
        buffer.add(line);

        // special case in the beginning
        switch (buffer.size()) {
          case 1:
            currLine = buffer.get(0);
            break;
          case 2:
            lineIdx += 1;
            continue;
          case 3:
            currLine = buffer.get(1);
            break;
          default:
            System.err.println("buffer is contains too many lines: " + buffer.size());
        }
        findAndAddExactlyTwoAdjacentNums(buffer, currLine);
        lineIdx += 1;
      }

      // special case: last line should be considered
      currLine = buffer.get(2);
      findAndAddExactlyTwoAdjacentNums(buffer, currLine);

      System.out.println("Final sum: " + TOTAL_SUM);

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }
}

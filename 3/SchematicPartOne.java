import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SchematicPartOne {
  static Integer TOTAL_SUM = 0;

  public static void resetArray(char[] arr) {
    for (int i = 0; i < arr.length; i++) {
      arr[i] = 0;
    }
  }

  public static boolean isDigit(char ch) {
    if (Integer.valueOf(ch) >= 48 && Integer.valueOf(ch) <= 57) return true;
    return false;
  }

  public static boolean isDot(char ch) {
    if (Integer.valueOf(ch) == 46) return true;
    return false;
  }

  public static boolean isSpChar(char ch) {
    if (Integer.valueOf(ch) >= 33 && Integer.valueOf(ch) <= 47 && !isDot(ch)) return true;
    if (Integer.valueOf(ch) >= 58 && Integer.valueOf(ch) <= 64) return true;
    if (Integer.valueOf(ch) >= 91 && Integer.valueOf(ch) <= 96) return true;
    if (Integer.valueOf(ch) >= 123 && Integer.valueOf(ch) <= 126) return true;
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

  public static String printArr(char[] arr) {
    String s = "";
    for (int i = 0; i < arr.length; i++) {
      s += arr[i];
    }
    return s;
  }

  public static boolean arrEmpty(char[] arr) {
    for (int i = 0; i < arr.length; i++) {
      if (Integer.valueOf(arr[i]) != 0) return false;
    }
    return true;
  }

  public static void compare(
      char[] numArr, ArrayList<Integer> spCharIdxArr, String line, String prevLine) {
    if (spCharIdxArr.isEmpty()) {
      return;
    }
    if (arrEmpty(numArr)) {
      return;
    }

    for (Integer spCharIdx : spCharIdxArr) {
      for (int i = spCharIdx - 1; i <= spCharIdx + 1; i++) {

        if (isDigit(numArr[i])) {
          int j = i;

          while (j >= 0 && isDigit(numArr[j])) {
            j = j - 1;
          }
          int lowerBoundIdx = j + 1;

          j = i;
          while (j < numArr.length && isDigit(numArr[j])) {
            j++;
          }
          int upperBoundIdx = j - 1;
          Integer number = extractNum(numArr, lowerBoundIdx, upperBoundIdx);
          TOTAL_SUM += number;
          removeUsedNumber(numArr, lowerBoundIdx, upperBoundIdx);
        }
      }
    }
  }

  public static ArrayList<Integer> deepCopyArr(ArrayList<Integer> arr) {
    ArrayList<Integer> newArr = new ArrayList<>();
    for (var i : arr) {
      newArr.add(i);
    }
    return newArr;
  }

  public static void main(String[] args) {
    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);
    int lineLength = 140; // because I know
    char[] numsPrevLine = new char[lineLength];
    ArrayList<Integer> spCharCurrLine = new ArrayList<>();
    ArrayList<Integer> spCharPrevLine = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      String prevLine = null;

      while ((line = reader.readLine()) != null) {
        char[] numsCurrLine = line.toCharArray();
        for (int i = 0; i < line.length(); i++) {
          char ch = numsCurrLine[i];
          if (isDot(ch)) {
            continue;
          } else if (isSpChar(ch)) {
            spCharCurrLine.add(i);
          } else if (isDigit(ch)) {
          } else {
            System.err.println("Unknown character " + ch + " at idx " + i + " on line: " + line);
          }
        }
        compare(numsCurrLine, spCharCurrLine, line, prevLine);
        compare(numsCurrLine, spCharPrevLine, line, prevLine);
        compare(numsPrevLine, spCharCurrLine, line, prevLine);
        numsPrevLine = numsCurrLine;
        spCharPrevLine = deepCopyArr(spCharCurrLine);
        spCharCurrLine.clear();
        prevLine = line;
      }

      System.out.println("Final sum: " + TOTAL_SUM);

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }
}

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

public class CalibrationPartTwo {

  public static String joinCharArr(ArrayList<Integer> resDigitArr) throws Exception {
    if (resDigitArr.size() == 2) {
      String val = String.valueOf(resDigitArr.get(0)) + "" + String.valueOf(resDigitArr.get(1));
      System.out.println("calibration value: " + val);
      return val;
    }
    throw new Exception("wrong charArr length: " + resDigitArr.toString());
  }

  public static String reverse(String s) {
    char[] arr = new char[s.length()];
    // System.out.println(s.length());
    for (int i = 0; i < s.length(); i++) {
      int idx = s.length() - i - 1;
      // System.out.println(idx);
      arr[i] = s.toCharArray()[idx];
    }

    String res = "";
    for (Character ch : arr) {
      res += ch;
    }

    return res;
  }

  public static boolean isDigit(char ch) {
    if (ch >= 48 && ch <= 57) {
      return true;
    }
    return false;
  }

  public static void main(String[] args) {
    List<String> sample =
        Arrays.asList(
            "two1nine",
            "eightwothree",
            "abcone2threexyz",
            "xtwone3four",
            "4nineeightseven2",
            "zoneight234",
            "7pqrstsixteen",
            "mjljctdbg8",
            "jqk39fivetwo",
            "fourmktnjskljqvqf");
    List<String> numbers =
        Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    Map<String, Integer> numMap =
        Map.of(
            "one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6, "seven", 7, "eight", 8,
            "nine", 9);
    System.out.println(numMap);
    ArrayList<String> numbersRev = new ArrayList<>();
    for (String num : numbers) {
      System.out.println(reverse(num));
      numbersRev.add(reverse(num));
    }

    Map<String, Integer> numMapRev = new HashMap<>();
    for (var entry : numMap.entrySet()) {
      numMapRev.put(reverse(entry.getKey()), entry.getValue());
    }
    System.out.println(numMapRev);
    ArrayList<Integer> resDigitArr = new ArrayList<>();
    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);
    int totalSum = 0;

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;

      while ((line = reader.readLine()) != null) {
        int firstIdx = -1; // TODO: check for negative number
        // System.out.println(line);
        char[] charArr = line.toCharArray();
        for (int i = 0; i < line.length(); i++) {
          // System.out.println(charArr[i]);

          if (isDigit(charArr[i])) {
            firstIdx = charArr[i] - '0';
            resDigitArr.add(firstIdx);
            break;
          } else {
            String choppedLine = line.substring(i, line.length());
            for (String num : numbers) {
              if (choppedLine.startsWith(num)) {
                firstIdx = numMap.get(num);
                resDigitArr.add(firstIdx);
                break;
              }
            }
            if (firstIdx > 0) break;
          }
        }
        // System.out.println(line + " : first digit = " + firstIdx);

        char[] charArrRev = reverse(line).toCharArray();
        String lineRev = new String(charArrRev);
        int lastIdx = -1; // TODO: check for negative number
        for (int i = 0; i < line.length(); i++) {
          // System.out.println(charArrRev[i]);

          if (isDigit(charArrRev[i])) {
            lastIdx = charArrRev[i] - '0';
            resDigitArr.add(lastIdx);
            break;
          } else {
            String choppedLine = lineRev.substring(i, line.length());
            for (String num : numbersRev) {
              if (choppedLine.startsWith(num)) {
                lastIdx = numMapRev.get(num);
                resDigitArr.add(lastIdx);
                break;
              }
            }
            if (lastIdx > 0) break;
          }
        }
        // System.out.println(line + " : last digit = " + lastIdx);
        try {
          System.out.println("calibration value: " + joinCharArr(resDigitArr));
          totalSum += Integer.valueOf(joinCharArr(resDigitArr));
          resDigitArr.clear();
        } catch (Exception x) {
          System.err.println("error: " + x);
        }
      }

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }

    System.out.println("total sum: " + totalSum);
  }
}

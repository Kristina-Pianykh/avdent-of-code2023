import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class CalibrationPartTwo {

  public static String joinCharArr(ArrayList<Character> charArr) throws Exception {
    if (charArr.size() == 1) {
      String val = String.valueOf(charArr.get(0)) + "" + String.valueOf(charArr.get(0));
      System.out.println("calibration value: " + val);
      return val;
    }
    if (charArr.size() == 2) {
      String val = String.valueOf(charArr.get(0)) + "" + String.valueOf(charArr.get(1));
      System.out.println("calibration value: " + val);
      return val;
    }
    throw new Exception("wrong charArr length: " + charArr.toString());
  }

  public static void main(String[] args) {
    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);
    int totalSum = 0;

    try (BufferedReader reader = Files.newBufferedReader(file)) {
      String calibrationVal = null;
      String line = null;
      ArrayList<Character> charArr = new ArrayList<>();
      char lastChar = 0;
      boolean firstSeen = false;

      while ((line = reader.readLine()) != null) {
        System.out.println(line);

        for (char ch : line.toCharArray()) {

          if (ch >= 48 && ch <= 57) {
            System.out.println("detected a digit: " + String.valueOf(ch));
            // int val = ch - '0';
            if (!firstSeen) {
              System.out.println(Character.valueOf(ch));
              charArr.add(Character.valueOf(ch));
              firstSeen = true;
            }
            lastChar = ch;
          }
        }

        firstSeen = false;
        charArr.add(Character.valueOf(lastChar));
        System.out.println(charArr.toString());

        try {
          calibrationVal = joinCharArr(charArr);
        } catch (Exception x) {
          System.out.println("error: " + x);
          System.exit(1);
        }

        assert calibrationVal != null;
        totalSum += Integer.valueOf(calibrationVal);

        System.out.println("resulting totalSum: " + totalSum);
        System.out.println(totalSum + "\n");

        charArr.clear();
      }

      System.out.println("total calibration value: " + totalSum);

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }
}

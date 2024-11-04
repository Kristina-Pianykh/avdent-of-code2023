import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

public class CubesPartOne {

  public static void main(String[] args) {
    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);

    Map<String, Integer> config = Map.of("red", 12, "green", 13, "blue", 14);

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      int totalSum = 0;

      while ((line = reader.readLine()) != null) {
        boolean impossibleGame = false;
        System.out.println(line);
        Integer id = Integer.valueOf(line.split(":")[0].split(" ")[1]);
        String sets = line.split(":")[1];

        game:
        for (String set : sets.split(";")) {
          String[] oneColorSets =
              Arrays.stream(set.trim().split(","))
                  .peek(e -> System.out.println(e))
                  .map(e -> e.trim())
                  .toArray(n -> new String[n]);

          for (var oneColorSet : oneColorSets) {
            Integer quantity = Integer.valueOf(oneColorSet.split(" ")[0]);
            String color = oneColorSet.split(" ")[1];
            // System.out.println("ID: " + id + ", found " + quantity + " of color " + color);
            if (config.get(color) < quantity) {
              System.out.println("impossible game for config: " + config.toString() + "; " + line);
              impossibleGame = true;
              break game;
            }
          }
        }
        if (!impossibleGame) totalSum += id;
      }
      System.out.println("totalSum: " + totalSum);

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }
}

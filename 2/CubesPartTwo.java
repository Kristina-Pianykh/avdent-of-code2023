import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CubesPartTwo {

  public static void main(String[] args) {
    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;
      ArrayList<Integer> powers = new ArrayList<>();

      while ((line = reader.readLine()) != null) {
        HashMap<String, Integer> config = new HashMap<>(Map.of("red", 0, "green", 0, "blue", 0));
        boolean impossibleGame = false;
        // System.out.println(line);
        Integer id = Integer.valueOf(line.split(":")[0].split(" ")[1]);
        String sets = line.split(":")[1];

        for (String set : sets.split(";")) {
          String[] oneColorSets =
              Arrays.stream(set.trim().split(","))
                  // .peek(e -> System.out.println(e))
                  .map(e -> e.trim())
                  .toArray(n -> new String[n]);

          for (var oneColorSet : oneColorSets) {
            Integer quantity = Integer.valueOf(oneColorSet.split(" ")[0]);
            String color = oneColorSet.split(" ")[1];

            if (quantity > config.get(color)) {
              config.put(color, quantity);
            }
          }
        }
        if (!config.values().stream().allMatch(e -> e > 0)) {
          System.out.println("error: " + line);
          break;
        }
        powers.add(config.values().stream().reduce(1, (acc, element) -> acc * element));
      }
      // System.out.println(powers.toString());
      int totalSum = powers.stream().mapToInt(e -> e).sum();
      System.out.println("totalSum: " + totalSum);

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }
}

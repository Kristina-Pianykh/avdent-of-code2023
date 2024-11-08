import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FertilizerPartOne {
  private static final Logger LOGGER = Logger.getLogger(FertilizerPartOne.class.getName());

  public static void initLogger(Level loggerLevel, Level handlerLevel) {
    LOGGER.setLevel(loggerLevel);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(handlerLevel);
    handler.setFormatter(new CustomFormatter());
    LOGGER.addHandler(handler);
  }

  public static void readCategory(List<MapItem> map, String CategoryBlock, String category) {
    List<String> lines = Arrays.asList(CategoryBlock.split(":")[1].trim().split("\\n"));
    LOGGER.fine("\n" + category);

    for (String line : lines) {
      List<Long> item =
          Arrays.asList(line.split("\s+")).stream()
              .filter(e -> e.length() > 0)
              .map(e -> Long.valueOf(e.trim()))
              .toList();

      LOGGER.fine(item.toString());
      assert item.size() == 3;
      map.add(new MapItem(item.get(0), item.get(1), item.get(2)));
    }
  }

  public static String readInput(Path file, Charset charset) {
    String input = "";

    StringBuilder sb = new StringBuilder();

    try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
      String line = null;

      while ((line = reader.readLine()) != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
      }
      input = sb.toString();

    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
    return input;
  }

  public static Long computeLocation(
      Long val,
      List<MapItem> seedToSoil,
      List<MapItem> soilToFertilizer,
      List<MapItem> fertilizerToWater,
      List<MapItem> waterToLight,
      List<MapItem> lightToTemp,
      List<MapItem> tempToHumidity,
      List<MapItem> humidityToLocation) {

    for (List<MapItem> map :
        Arrays.asList(
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemp,
            tempToHumidity,
            humidityToLocation)) {
      // LOGGER.fine(String.format("Mapping %s", map));
      val = mapSrcToDest(val, map);
    }
    return val;
  }

  public static Long mapSrcToDest(Long seed, List<MapItem> map) {
    for (MapItem item : map) {
      Long mapValue = item.mapValue(seed);
      if (!mapValue.equals(seed)) return mapValue;
    }
    return seed;
  }

  public static void main(String[] args) {
    initLogger(Level.INFO, Level.INFO);

    String filePath = "./input.txt";
    Charset charset = Charset.forName("US-ASCII");
    Path file = FileSystems.getDefault().getPath(filePath);

    List<Long> seeds = new ArrayList<>();
    List<MapItem> seedToSoil = new ArrayList<>();
    List<MapItem> soilToFertilizer = new ArrayList<>();
    List<MapItem> fertilizerToWater = new ArrayList<>();
    List<MapItem> waterToLight = new ArrayList<>();
    List<MapItem> lightToTemp = new ArrayList<>();
    List<MapItem> tempToHumidity = new ArrayList<>();
    List<MapItem> humidityToLocation = new ArrayList<>();

    String input = readInput(file, charset);
    List<String> inputBlocks = Arrays.asList(input.split("\\n\\n"));

    for (String block : inputBlocks) {
      String category = block.split(":")[0].trim();
      switch (category) {
        case "seeds":
          List<String> lines = Arrays.asList(block.split(":")[1].trim().split("\\n"));
          assert lines.size() == 1;
          seeds =
              Arrays.asList(lines.get(0).split("\s+")).stream()
                  .filter(e -> e.length() > 0)
                  .map(e -> Long.valueOf(e.trim()))
                  .toList();
          break;
        case "seed-to-soil map":
          readCategory(seedToSoil, block, "seed-to-soil map");
          break;
        case "soil-to-fertilizer map":
          readCategory(soilToFertilizer, block, "soil-to-fertilizer map");
          break;
        case "fertilizer-to-water map":
          readCategory(fertilizerToWater, block, "fertilizer-to-water map");
          break;
        case "water-to-light map":
          readCategory(waterToLight, block, "water-to-light map");
          break;
        case "light-to-temperature map":
          readCategory(lightToTemp, block, "light-to-temperature map");
          break;
        case "temperature-to-humidity map":
          readCategory(tempToHumidity, block, "temperature-to-humidity map");
          break;
        case "humidity-to-location map":
          readCategory(humidityToLocation, block, "humidity-to-location map");
          break;
        default:
          LOGGER.severe(String.format("unexpected category %s in block:\n", category, block));
          System.exit(1);
      }
    }

    List<Long> locations = new ArrayList<>();
    for (Long seed : seeds) {
      Long location =
          computeLocation(
              seed,
              seedToSoil,
              soilToFertilizer,
              fertilizerToWater,
              waterToLight,
              lightToTemp,
              tempToHumidity,
              humidityToLocation);
      locations.add(location);
    }
    OptionalLong closestLocation = locations.stream().mapToLong(e -> e).min();
    System.out.println("The closest location: " + closestLocation.getAsLong());
  }

  static class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
      return String.format("[%s] %s%n", record.getLevel(), record.getMessage());
    }
  }
  // lakjfda
}

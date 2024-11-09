import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

enum typeRank {
  ERROR,
  HIGH_CARD,
  ONE_PAIR,
  TWO_PAIR,
  THREE_OF_A_KIND,
  FULL_HOUSE,
  FOUR_OF_A_KIND,
  FIVE_OF_A_KIND,
}

class Hand {
  String hand;
  int bid;
  Map<Character, Integer> charCount;
  typeRank rank;

  public Hand(String hand, int bid) {
    this.hand = hand;
    this.bid = bid;
    List<Character> labels =
        Arrays.asList('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');
    this.charCount = labels.stream().collect(Collectors.toMap(e -> e, e -> 0));
    this.hand
        .chars()
        .mapToObj(e -> (char) e)
        .forEach(e -> this.charCount.put((char) e, this.charCount.get(e) + 1));
    this.rank = getTypeRank(this.charCount);
    assert this.rank != typeRank.ERROR;
  }

  public String toString() {
    return String.format("{hand=%s, bid=%d, type=%s}", this.hand, this.bid, this.rank);
  }

  public static typeRank getTypeRank(Map<Character, Integer> charCount) {
    List<Character> keys;
    keys = getKeyByVal(charCount, 5);
    if (!keys.isEmpty()) return typeRank.FIVE_OF_A_KIND;

    keys = getKeyByVal(charCount, 4);
    if (!keys.isEmpty()) return typeRank.FOUR_OF_A_KIND;

    keys = getKeyByVal(charCount, 3);
    List<Character> keys1 = getKeyByVal(charCount, 2);
    if (!keys.isEmpty() && !keys1.isEmpty()) return typeRank.FULL_HOUSE;
    else if (!keys.isEmpty() && keys1.isEmpty()) return typeRank.THREE_OF_A_KIND;

    keys = getKeyByVal(charCount, 2);
    if (!keys.isEmpty() && keys.size() == 2) return typeRank.TWO_PAIR;
    else if (!keys.isEmpty() && keys.size() == 1) return typeRank.ONE_PAIR;

    List<Map.Entry> nonZeroLabels =
        charCount.entrySet().stream().filter(e -> e.getValue() == 1).collect(Collectors.toList());
    if (nonZeroLabels.size() == 5) return typeRank.HIGH_CARD;

    return typeRank.ERROR;
  }

  public static List<Character> getKeyByVal(Map<Character, Integer> charCount, int val) {
    List<Character> keys = new ArrayList<>();
    for (var ch : charCount.keySet()) {
      if (charCount.get(ch) == val) keys.add(ch);
    }
    return keys;
  }
}

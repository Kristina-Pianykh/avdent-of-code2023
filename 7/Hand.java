import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

enum typeRank {
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
        Arrays.asList('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J');
    this.charCount = labels.stream().collect(Collectors.toMap(e -> e, e -> 0));
    this.hand
        .chars()
        .mapToObj(e -> (char) e)
        .forEach(e -> this.charCount.put((char) e, this.charCount.get(e) + 1));
    // this.rank = computeRank(this.charCount, this.hand);
  }

  public String toString() {
    return String.format(
        "{hand=%s, bid=%d, type=%s, charCount=%s}", this.hand, this.bid, this.rank, this.charCount);
  }

  public void computeRankJ() {
    char[] charArr = this.hand.toCharArray();
    Character ch;
    Character ch2;

    // edge case
    if (this.charCount.get('J') == 5) {
      this.rank = typeRank.FIVE_OF_A_KIND;
      return;
    }

    for (int i = 0; i < charArr.length; i++) {
      ch = Character.valueOf(charArr[i]);
      if (ch.equals('J')) continue;
      if (this.charCount.get(ch) + this.charCount.get('J') == 5) {
        this.rank = typeRank.FIVE_OF_A_KIND;
        return;
      }
    }

    for (int i = 0; i < charArr.length; i++) {
      ch = Character.valueOf(charArr[i]);
      if (ch.equals('J')) continue;
      if (this.charCount.get(ch) + this.charCount.get('J') == 4) {
        this.rank = typeRank.FOUR_OF_A_KIND;
        return;
      }
    }

    for (int i = 0; i < charArr.length; i++) {
      ch = Character.valueOf(charArr[i]);

      if (ch.equals('J')) continue;

      if (this.charCount.get(ch) + this.charCount.get('J') == 3) {

        for (int j = 0; j < charArr.length; j++) {
          ch2 = Character.valueOf(charArr[j]);
          if (!ch2.equals('J') && !ch2.equals(ch) && this.charCount.get(ch2) == 2) {
            this.rank = typeRank.FULL_HOUSE;
            return;
          }
        }

        this.rank = typeRank.THREE_OF_A_KIND;
        return;
      }
    }

    for (int i = 0; i < charArr.length; i++) {
      ch = Character.valueOf(charArr[i]);

      if (ch.equals('J')) continue;

      if (this.charCount.get(ch) + this.charCount.get('J') == 2) {

        for (int j = 0; j < charArr.length; j++) {
          ch2 = Character.valueOf(charArr[j]);
          if (!ch.equals(ch2) && this.charCount.get(ch2) == 2) {
            this.rank = typeRank.TWO_PAIR;
            return;
          }
        }

        this.rank = typeRank.ONE_PAIR;
        return;
      }
    }

    this.rank = typeRank.HIGH_CARD;
    return;
  }

  public void computeRank() {
    List<Character> keys;

    keys = getKeyByVal(5);
    if (!keys.isEmpty()) {
      this.rank = typeRank.FIVE_OF_A_KIND;
      return;
    }

    keys = getKeyByVal(4);
    if (!keys.isEmpty()) {
      this.rank = typeRank.FOUR_OF_A_KIND;
      return;
    }

    keys = getKeyByVal(3);
    List<Character> keys1 = getKeyByVal(2);
    if (!keys.isEmpty() && !keys1.isEmpty()) {
      this.rank = typeRank.FULL_HOUSE;
      return;
    } else if (!keys.isEmpty() && keys1.isEmpty()) {
      this.rank = typeRank.THREE_OF_A_KIND;
      return;
    }

    keys = getKeyByVal(2);
    if (!keys.isEmpty() && keys.size() == 2) {
      this.rank = typeRank.TWO_PAIR;
      return;
    } else if (!keys.isEmpty() && keys.size() == 1) {
      this.rank = typeRank.ONE_PAIR;
      return;
    }

    List<Map.Entry> nonZeroLabels =
        this.charCount.entrySet().stream()
            .filter(e -> e.getValue() == 1)
            .collect(Collectors.toList());
    if (nonZeroLabels.size() == 5) {
      this.rank = typeRank.HIGH_CARD;
      return;
    }
  }

  public List<Character> getKeyByVal(int val) {
    List<Character> keys = new ArrayList<>();
    for (var ch : this.charCount.keySet()) {
      if (this.charCount.get(ch) == val) keys.add(ch);
    }
    return keys;
  }
}

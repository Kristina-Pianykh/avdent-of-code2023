import java.util.ArrayList;

public class Testies {

  public static String reverse(String s) throws Exception {
    ArrayList<Character> arr = new ArrayList<>();
    // System.out.println(s.length());
    for (int i = s.length(); i > 0; i--) {
      int idx = i - 1;
      // System.out.println(idx);
      arr.add(s.toCharArray()[idx]);
    }

    String res = "";
    for (Character ch : arr) {
      res += ch;
    }

    return res;
  }

  public static void main(String[] args) {
    String v = "sixonetqddfmone1seven7";
    try {
      String s = reverse(v);
      System.out.println(s);
    } catch (Exception x) {
      System.out.println("error: " + x);
    }
  }
}

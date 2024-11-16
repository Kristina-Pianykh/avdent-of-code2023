import java.util.Arrays;
import java.util.List;

class Pipe {
  char tag;
  List<List<Integer>> connections;
  List<Integer> pos;

  // pos: x, y
  public Pipe(char ch, List<Integer> pos) throws Exception {
    List<Integer> conn1;
    List<Integer> conn2;

    switch (ch) {
      case '|':
        this.tag = '|';
        this.pos = Arrays.asList(pos.get(0), pos.get(1));
        conn1 = Arrays.asList(this.pos.get(0), this.pos.get(1) - 1);
        conn2 = Arrays.asList(this.pos.get(0), this.pos.get(1) + 1);
        this.connections = Arrays.asList(conn1, conn2);
        break;
      case '-':
        this.tag = '-';
        this.pos = Arrays.asList(pos.get(0), pos.get(1));
        conn1 = Arrays.asList(this.pos.get(0) - 1, this.pos.get(1));
        conn2 = Arrays.asList(this.pos.get(0) + 1, this.pos.get(1));
        this.connections = Arrays.asList(conn1, conn2);
        break;
      case 'L':
        this.tag = 'L';
        this.pos = Arrays.asList(pos.get(0), pos.get(1));
        conn1 = Arrays.asList(this.pos.get(0), this.pos.get(1) - 1);
        conn2 = Arrays.asList(this.pos.get(0) + 1, this.pos.get(1));
        this.connections = Arrays.asList(conn1, conn2);
        break;
      case 'J':
        this.tag = 'J';
        this.pos = Arrays.asList(pos.get(0), pos.get(1));
        conn1 = Arrays.asList(this.pos.get(0), this.pos.get(1) - 1);
        conn2 = Arrays.asList(this.pos.get(0) - 1, this.pos.get(1));
        this.connections = Arrays.asList(conn1, conn2);
        break;
      case '7':
        this.tag = '7';
        this.pos = Arrays.asList(pos.get(0), pos.get(1));
        conn1 = Arrays.asList(this.pos.get(0) - 1, this.pos.get(1));
        conn2 = Arrays.asList(this.pos.get(0), this.pos.get(1) + 1);
        this.connections = Arrays.asList(conn1, conn2);
        break;
      case 'F':
        this.tag = 'F';
        this.pos = Arrays.asList(pos.get(0), pos.get(1));
        conn1 = Arrays.asList(this.pos.get(0), this.pos.get(1) + 1);
        conn2 = Arrays.asList(this.pos.get(0) + 1, this.pos.get(1));
        this.connections = Arrays.asList(conn1, conn2);
        break;
      case '.':
        throw new Exception("Reach the ground");
      default:
        throw new Exception("Unrecognized character");
    }
  }

  public String toString() {
    return String.format(
        "{tag=%s, pos=%s, connections=[%s, %s]}",
        this.tag,
        this.pos.toString(),
        this.connections.get(0).toString(),
        this.connections.get(1).toString());
  }
}

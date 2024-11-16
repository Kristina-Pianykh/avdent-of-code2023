import java.util.Arrays;
import java.util.List;

class Pipe {
  char tag;
  List<Position> conns;
  Position pos;
  Position from;
  Position to;

  // pos: x, y
  public Pipe(char ch, Position pos) throws ReachedGroundException, InvalidPipeCharException {
    Position conn1;
    Position conn2;
    this.pos = new Position(pos.x, pos.y);
    assert this.pos != null;

    switch (ch) {
      case '|':
        this.tag = '|';
        conn1 = new Position(this.pos.x, this.pos.y - 1);
        conn2 = new Position(this.pos.x, this.pos.y + 1);
        this.conns = Arrays.asList(conn1, conn2);
        break;
      case '-':
        this.tag = '-';
        conn1 = new Position(this.pos.x - 1, this.pos.y);
        conn2 = new Position(this.pos.x + 1, this.pos.y);
        this.conns = Arrays.asList(conn1, conn2);
        break;
      case 'L':
        this.tag = 'L';
        conn1 = new Position(this.pos.x, this.pos.y - 1);
        conn2 = new Position(this.pos.x + 1, this.pos.y);
        this.conns = Arrays.asList(conn1, conn2);
        break;
      case 'J':
        this.tag = 'J';
        conn1 = new Position(this.pos.x, this.pos.y - 1);
        conn2 = new Position(this.pos.x - 1, this.pos.y);
        this.conns = Arrays.asList(conn1, conn2);
        break;
      case '7':
        this.tag = '7';
        conn1 = new Position(this.pos.x - 1, this.pos.y);
        conn2 = new Position(this.pos.x, this.pos.y + 1);
        this.conns = Arrays.asList(conn1, conn2);
        break;
      case 'F':
        this.tag = 'F';
        conn1 = new Position(this.pos.x, this.pos.y + 1);
        conn2 = new Position(this.pos.x + 1, this.pos.y);
        this.conns = Arrays.asList(conn1, conn2);
        break;
      case '.':
        throw new ReachedGroundException();
      default:
        throw new InvalidPipeCharException(ch);
    }
  }

  public void setFrom(Integer connsIdx) {
    this.from = this.conns.get(connsIdx);
  }

  public void setTo(Integer connsIdx) {
    this.to = this.conns.get(connsIdx);
  }

  public String toString() {
    String from = null;
    if (this.from != null) from = this.from.toString();
    else from = "null";

    String to = null;
    if (this.to != null) to = this.to.toString();
    else to = "null";

    return String.format(
        "{tag=%s, pos=%s, from=%s, to=%s, connections=[%s, %s]}",
        this.tag,
        this.pos.toString(),
        from,
        to,
        this.conns.get(0).toString(),
        this.conns.get(1).toString());
  }
}

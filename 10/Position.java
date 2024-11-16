class Position {
  static int size = 2;
  int x;
  int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;
    Position other = (Position) o;
    if (this.x != other.x || this.y != other.y) return false;
    return true;
  }

  public String toString() {
    return String.format("[x=%d, y=%d]", this.x, this.y);
  }
}

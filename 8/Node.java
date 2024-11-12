
class Node {
  String label;
  String endNode;
  long startStepIdx;

  public Node(String label) {
    this.label = label;
  }

  public String toString() {
    return String.format(
        "{startNode=%s, endNode=%s, startStepIdx=%d}", this.label, this.endNode, this.startStepIdx);
  }
}

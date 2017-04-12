import java.util.HashMap;

// represents an edge in a graph
class Edge {
  String from;
  String to;
  int weight;

  // constructor
  Edge(String from, String to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  // returns the difference in weights between two edges
  int compareWeights(Edge other) {
    int difference = this.weight - other.weight;
    if (difference > 0) {
      return 1;
    } else if (difference < 0) {
      return -1;
    } else {
      return difference;
    }
  }

  // returns the comparison of either the from vertices or to vertices of two edges
  int compareVertices(Edge other, boolean from) {
    if (from) {
      return this.from.compareTo(other.from);
    } else {
      return this.to.compareTo(other.to);
    }
  }

  @Override
  // returns a string representation of an edge
  public String toString() {
    return "(" + this.from + " " + this.to + " " + this.weight + ")";
  }

  // checks if this edge causes a cycle
  boolean causesCycle(HashMap<String, String> representatives) {
    return causesCycleHelp(representatives, this.from, this.to);
  }

  // helper to the causesCycle method
  // checks if the from and to are connected, and if so returns true
  // otherwise, replaces representatives accordingly
  boolean causesCycleHelp(HashMap<String, String> representatives, String from, String to) {
    if (representatives.get(from) == representatives.get(to)) {
      // they are already connected
      return true;
    }
    else {
      // they are separate
      if (representatives.get(to) == to) {
        // if to's representative is itself, replace to's representative with from's
        representatives.replace(to, representatives.get(from));
        return false;
      }
      else {
        // recurse again with from and to's representative
        return causesCycleHelp(representatives, from, representatives.get(to));
      }
    }
  }
}

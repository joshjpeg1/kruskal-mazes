// represents a comparator for edges
class EdgeComparator implements IComparator<Edge>{
  // compares two edges
  public int compare(Edge e1, Edge e2) {
    int difference = e1.compareWeights(e2);
    if (difference == 0) {
      difference = e1.compareVertices(e2, true);
      if (difference == 0) {
        return e1.compareVertices(e2, false);
      } else {
        return difference;
      }
    } else {
      return difference;
    }
  }
}

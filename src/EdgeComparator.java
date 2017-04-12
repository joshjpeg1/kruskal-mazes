// represents a comparator for edges
class EdgeComparator implements IComparator<Edge>{
  // compares two edges
  public int compare(Edge e1, Edge e2) {
    return e1.compareWeights(e2);
  }
}

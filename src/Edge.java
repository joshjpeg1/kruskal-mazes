import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

// represents an edge in a graph
class Edge {
  Vertex from;
  Vertex to;
  int weight;
  Utils utils;

  Edge(Vertex from, Vertex to, int weight) {
    utils = new Utils();
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

  @Override
  public boolean equals(Object other) {
    if (other instanceof Edge) {
      return this.sameEdge((Edge) other);
    } else {
      return false;
    }
  }

  boolean sameEdge(Edge other) {
    return (this.from.sameVertex(other.from) && this.to.sameVertex(other.to))
      || (this.from.sameVertex(other.to) && this.to.sameVertex(other.from));
  }

  @Override
  // returns a string representation of an edge
  public String toString() {
    return "(" + this.from + " " + this.to + " " + this.weight + ")";
  }

  // checks if this edge causes a cycle
  boolean causesCycle(HashMap<Vertex, Vertex> reps) {
    return this.utils.cycle(reps, this.from, this.to);
  }

  void addVertices(ArrayList<Vertex> vertices) {
    this.utils.addNoDupes(vertices, this.from);
    this.utils.addNoDupes(vertices, this.to);
  }

  void drawEdge(WorldScene ws) {
    int direction = this.from.direction(this.to);
    if (direction == Vertex.NORTH) {
      ws.placeImageXY(new RectangleImage(Vertex.CELL_SIZE - (Vertex.EDGE_SIZE * 2),
          (Vertex.EDGE_SIZE * 3), "solid", Color.gray),
          (from.x * Vertex.CELL_SIZE) + (Vertex.CELL_SIZE / 2),
          (from.y * Vertex.CELL_SIZE) + (Vertex.EDGE_SIZE / 2));
    } else if (direction == Vertex.SOUTH) {
      ws.placeImageXY(new RectangleImage(Vertex.CELL_SIZE - (Vertex.EDGE_SIZE * 2),
          (Vertex.EDGE_SIZE * 3), "solid", Color.gray),
          (from.x * Vertex.CELL_SIZE) + (Vertex.CELL_SIZE / 2),
          (from.y * Vertex.CELL_SIZE) + (Vertex.EDGE_SIZE / 2) + (Vertex.CELL_SIZE - Vertex.EDGE_SIZE));
    } else if (direction == Vertex.WEST) {
      ws.placeImageXY(new RectangleImage((Vertex.EDGE_SIZE * 3),
          Vertex.CELL_SIZE - (Vertex.EDGE_SIZE * 2), "solid", Color.gray),
          (from.x * Vertex.CELL_SIZE) + (Vertex.EDGE_SIZE / 2),
          (from.y * Vertex.CELL_SIZE) + (Vertex.CELL_SIZE / 2));
    } else {
      ws.placeImageXY(new RectangleImage((Vertex.EDGE_SIZE * 3),
          Vertex.CELL_SIZE - (Vertex.EDGE_SIZE * 2), "solid", Color.gray),
          (from.x * Vertex.CELL_SIZE) + (Vertex.EDGE_SIZE / 2) + (Vertex.CELL_SIZE - Vertex.EDGE_SIZE),
          (from.y * Vertex.CELL_SIZE) + (Vertex.CELL_SIZE / 2));
    }
  }
}

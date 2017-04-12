import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javalib.impworld.WorldScene;
import javalib.worldimages.RectangleImage;

import java.awt.*;
import java.util.HashMap;

// represents an edge in a graph
class Edge {
  Vertex from;
  Vertex to;
  int weight;


  Edge(Vertex from, Vertex to, int weight) {
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

  void setVertex(Vertex v, boolean from) {
    if (from) {
      this.from = v;
    } else {
      this.to = v;
    }
  }

  boolean sameEdge(Edge other) {
    return (this.from.sameVertex(other.from) && this.to.sameVertex(other.to))
      || (this.from.sameVertex(other.to) && this.to.sameVertex(other.from));
  }

  // returns the comparison of either the from vertices or to vertices of two edges
  int compareVertices(Edge other, boolean from) {
    // FIX
    return 1;
  }

  @Override
  // returns a string representation of an edge
  public String toString() {
    return "(" + this.from + " " + this.to + " " + this.weight + ")";
  }

  // checks if this edge causes a cycle
  boolean causesCycle(HashMap<Vertex, Vertex> reps) {
    return causesCycleHelp(reps, this.from, this.to);
  }

  // helper to the causesCycle method
  // checks if the from and to are connected, and if so returns true
  // otherwise, replaces representatives accordingly
  boolean causesCycleHelp(HashMap<Vertex, Vertex> reps, Vertex from, Vertex to) {
    /*boolean cycle = false;
    boolean stop = false;
    while(!stop) {
      if (reps.get(from).equals(reps.get(to))) {
        cycle = true;
        stop = true;
      } else {
        if (reps.get(to).equals(to)) {
          reps.replace(to, reps.get(from));
          cycle = false;
          stop = true;
        } else {
          to = reps.get(to);
        }
      }
    }
    return cycle;*/
    if (reps.get(from).equals(reps.get(to))) {
      // they are already connected
      return true;
    }
    else {
      // they are separate
      if (reps.get(to).equals(to)) {
        // if to's representative is itself, replace to's representative with from's
        reps.replace(to, reps.get(from));
        return false;
      } else {
        // recurse again with from and to's representative
        return causesCycleHelp(reps, from, reps.get(to));
      }
    }
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

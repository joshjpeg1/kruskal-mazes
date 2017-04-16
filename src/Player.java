import javalib.impworld.WorldScene;

import java.util.ArrayList;

/**
 * Created by josh_jpeg on 4/16/17.
 */
public class Player {
  Vertex currentPosition;
  ArrayList<Edge> visited;
  Utils utils;

  Player(Vertex start) {
    this.currentPosition = start;
    this.utils = new Utils();
    this.visited = new ArrayList<>();
  }

  void movePlayer(Vertex v) {
    this.currentPosition = v;
  }

  void visitEdge(Edge e) {
    this.visited.add(e);
    e.userVisited = true;
  }

  void drawPlayer(WorldScene ws, int cellSize, int width, int height) {
    for (Edge e : this.visited) {
      e.drawEdge(ws, cellSize);
    }
    ArrayList<Vertex> vertices = this.utils.collectVertices(this.visited);
    for (Vertex v : vertices) {
      v.drawVertex(ws, cellSize, width, height, false);
    }
    this.currentPosition.drawVertex(ws, cellSize, width, height, true);
  }
}

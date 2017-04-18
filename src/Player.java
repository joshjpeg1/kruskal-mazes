import javalib.impworld.WorldScene;

import java.lang.reflect.Array;
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

  // moves the player's currentPosition by one depending on the input dx/dy
  void movePlayer(int dx, int dy, ArrayList<Edge> arr) {
    // the vertex will be one of 4 options, depending on the key press
    // "up" will result in v = new Vertex(0, -1)
    // "down" will result in v = new Vertex(0, 1)
    // "left" will result in v = new Vertex(-1, 0)
    // "right" will result in v = new Vertex(1, 0)
    Vertex dest = new Vertex(dx, dy);

    for (Edge e : arr)
    if (e.containsVertex(currentPosition) && e.containsVertex(dest)) {
      this.currentPosition = currentPosition.addVertices(dest);
    }
  }

  // 
  void visitEdge(Edge e) {
    this.visited.add(e);
    e.userVisited = true;
  }

  // draws the player
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

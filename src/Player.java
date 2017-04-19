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

  @Override
  public String toString() {
    return this.currentPosition.toString();
  }

  // moves the player's currentPosition by one depending on the input dx/dy, and records the
  // player's move in the ArrayList visited
  int movePlayer(int dx, int dy, ArrayList<Edge> arr, int width, int height) {
    // the vertex will be one of 4 options, depending on the key press
    // "up" will result in v = new Vertex(0, -1)
    // "down" will result in v = new Vertex(0, 1)
    // "left" will result in v = new Vertex(-1, 0)
    // "right" will result in v = new Vertex(1, 0)

    Vertex transform = new Vertex(dx, dy);
    Vertex dest = transform.addVertices(this.currentPosition);

    if (dest.x < 0 || dest.y < 0 || dest.x >= width
        || dest.y >= height) {
      dest = this.currentPosition;
    }

    System.out.println(currentPosition.toString() + ", " + transform.toString() + ", " + dest.toString());


    for (int i = 0; i < arr.size(); i++) {
      if (arr.get(i).containsVertex(this.currentPosition) && arr.get(i).containsVertex(dest)) {
        this.currentPosition = dest;
        visited.add(arr.get(i));
        return i;
      }
    }
    return -1;
  }


  // draws the player
  void drawPlayer(WorldScene ws, int cellSize, int width, int height) {
    for (Edge e : this.visited) {
      e.drawEdge(ws, cellSize);
    }
    ArrayList<Vertex> vertices = this.utils.collectVertices(this.visited);
    for (Vertex v : vertices) {
      v.drawVertex(ws, cellSize, width, height, true);
    }
    this.currentPosition.drawVertex(ws, cellSize, width, height, true);
  }
}

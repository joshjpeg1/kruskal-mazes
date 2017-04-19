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
    Vertex transform = new Vertex(dx, dy);
    Vertex dest = transform.addVertices(this.currentPosition);

    if (dest.x < 0 || dest.y < 0 || dest.x >= width
        || dest.y >= height) {
      dest = this.currentPosition;
    }

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
    this.currentPosition.drawVertex(ws, cellSize, width, height, true);
  }

  
  boolean samePosition(Vertex v) {
    if (this.currentPosition.equals(v)) {
      return true;
    }
    return false;
  }
}

import javalib.impworld.WorldScene;
import java.util.ArrayList;

// represents a Player
public class Player {
  Vertex currentPosition;
  ArrayList<Edge> visited;
  Utils utils;

  // returns true if the given
  Player(Vertex start) {
    this.currentPosition = start;
    this.utils = new Utils();
    this.visited = new ArrayList<>();
  }

  // moves the player's currentPosition by one depending on the input dx/dy, and records the
  // player's move in the ArrayList visited
  int movePlayer(int dx, int dy, ArrayList<Edge> arr, int width, int height) {
    Vertex dest = this.currentPosition.addVertices(dx, dy);
    if (!dest.inBounds(width, height)) {
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

  // returns true if the player is at the same position of the given vertex
  boolean samePosition(Vertex v) {
    return this.currentPosition.equals(v);
  }
}

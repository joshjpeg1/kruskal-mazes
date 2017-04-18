import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import java.awt.Color;

// represents a Vertex in a graph
// represents a Cell in a maze
public class Vertex {
  int x;
  int y;
  boolean userVisited;
  boolean compVisited;
  boolean correct;
  static final int CELL_SIZE = 32;
  static final int NORTH = 0;
  static final int EAST = 1;
  static final int SOUTH = 2;
  static final int WEST = 3;

  // constructor
  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.userVisited = false;
    this.compVisited = false;
    this.correct = false;
  }

  @Override
  // overrides the toString method and returns a string representation of a vertex
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  @Override
  // overrides the hashCode method for equality purposes in HashMaps
  public int hashCode() {
    return (this.x * 1000) + this.y;
  }

  @Override
  // overrides the equals method for generic purposes
  public boolean equals(Object other) {
    if (other instanceof Vertex) {
      Vertex that = (Vertex) other;
      return this.x == that.x && this.y == that.y;
    } else {
      return false;
    }
  }

  // returns the direction that the given Vertex is in relative to this one
  int direction(Vertex other) {
    if (this.x - other.x < 0) {
      return EAST;
    } else if (this.x - other.x > 0) {
      return WEST;
    } else if (this.y - other.y < 0) {
      return SOUTH;
    } else {
      return NORTH;
    }
  }

  // draws a cell onto the given WorldScene
  void drawVertex(WorldScene ws, int cellSize, int width, int height, boolean playerOn) {
    ws.placeImageXY(new RectangleImage(cellSize, cellSize, "solid",
        this.cellColor(width, height, playerOn)),
        (this.x * cellSize) + (cellSize / 2), (this.y * cellSize) + (cellSize / 2));
    ws.placeImageXY(new RectangleImage(cellSize, cellSize, "outline", Color.black),
        (this.x * cellSize) + (cellSize / 2), (this.y * cellSize) + (cellSize / 2));
  }

  // returns the correct cell color based on the cell's position
  Color cellColor(int width, int height, boolean playerOn) {
    if (this.compVisited && this.correct) {
      return new Color(62, 118, 204);
    } else if (this.compVisited) {
      return new Color(144, 184, 242);
    } else if (playerOn) {
      return new Color(255, 200, 10);
    } else if (this.userVisited || (this.x == 0 && this.y == 0)) {
      return new Color(33, 127, 70);
    } else if (this.x == width - 1 && this.y == height - 1) {
      return new Color(108, 32, 128);
    } else {
      return new Color(192, 192, 192);
    }
  }

  void userVertex() {
    this.userVisited = true;
  }

  void visitVertex() {
    this.compVisited = true;
  }

  void correctVertex() {
    this.correct = true;
    this.visitVertex();
  }

  void resetVertex() {
    this.correct = false;
    this.compVisited = false;
    this.userVisited = false;
  }

  Vertex addVertices(Vertex other) {
    return new Vertex(this.x + other.x, this.y + other.y);
  }
}

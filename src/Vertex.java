import javalib.impworld.WorldScene;
import javalib.worldimages.RectangleImage;

import java.awt.*;

/**
 * Created by josh_jpeg on 4/11/17.
 */
public class Vertex {
  int x;
  int y;
  static final int CELL_SIZE = 50;
  static final int EDGE_SIZE = 4;
  static final int NORTH = 0;
  static final int EAST = 1;
  static final int SOUTH = 2;
  static final int WEST = 3;

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  boolean sameVertex(Vertex other) {
    return this.x == other.x && this.y == other.y;
  }

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

  @Override
  public boolean equals(Object other) {
    if (other instanceof Vertex) {
      return this.sameVertex((Vertex) other);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return (this.x * 1000) + this.y;
  }

  void drawVertex(WorldScene ws) {
    // draws box
    ws.placeImageXY(new RectangleImage(this.CELL_SIZE, this.CELL_SIZE, "solid", Color.gray),
      (this.x * this.CELL_SIZE) + (CELL_SIZE / 2), (this.y * this.CELL_SIZE) + (CELL_SIZE / 2));
    //draw northern border
    ws.placeImageXY(new RectangleImage(this.CELL_SIZE, this.EDGE_SIZE, "solid", Color.black),
      (this.x * this.CELL_SIZE) + (CELL_SIZE / 2),
      (this.y * this.CELL_SIZE) + (EDGE_SIZE / 2));
    //draw southern border
    ws.placeImageXY(new RectangleImage(this.CELL_SIZE, this.EDGE_SIZE, "solid", Color.black),
      (this.x * this.CELL_SIZE) + (CELL_SIZE / 2),
      (this.y * this.CELL_SIZE) + (EDGE_SIZE / 2) + (this.CELL_SIZE - this.EDGE_SIZE));
    //draw western border
    ws.placeImageXY(new RectangleImage(this.EDGE_SIZE, this.CELL_SIZE, "solid", Color.black),
      (this.x * this.CELL_SIZE) + (EDGE_SIZE / 2),
      (this.y * this.CELL_SIZE) + (CELL_SIZE / 2));
    //draw eastern border
    ws.placeImageXY(new RectangleImage(this.EDGE_SIZE, this.CELL_SIZE, "solid", Color.black),
      (this.x * this.CELL_SIZE) + (EDGE_SIZE / 2) + (this.CELL_SIZE - this.EDGE_SIZE),
      (this.y * this.CELL_SIZE) + (CELL_SIZE / 2));
  }
}

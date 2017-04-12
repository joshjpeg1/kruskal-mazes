import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javalib.impworld.*;

// represents a Maze
class Maze extends World {
  int width;
  int height;
  ArrayList<Edge> allEdges;
  ArrayList<Vertex> allVertices;
  ArrayList<Edge> edgesInTree;
  Utils utils;
  Random rand;
  int responsiveSize;

  // constructor (testing purposes)
  Maze(ArrayList<Edge> worklist) {
    this.rand = new Random();
    this.utils = new Utils();
    this.allEdges = worklist;
    this.allVertices = this.utils.collectVertices(this.allEdges);
  }

  // constructor
  Maze(int width, int height) {
    this.rand = new Random();
    this.utils = new Utils();
    this.width = width;
    this.height = height;
    this.allEdges = this.generateGraph(this.width, this.height);
    this.allVertices = this.utils.collectVertices(this.allEdges);
    this.edgesInTree = this.kruskal(this.allEdges, this.allVertices);
    this.responsiveSize = this.responsiveSize();
  }

  // runs the Maze application
  public static void main(String[] argv) {
    Maze maze = new Maze(3, 3);
    maze.bigBang(maze.width * maze.responsiveSize, maze.height * maze.responsiveSize, 3);
  }

  // generates a randomly weighted graph
  ArrayList<Edge> generateGraph(int width, int height) {
    ArrayList<Edge> edges = new ArrayList<>();
    int area = width * height;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        Vertex currVertex = new Vertex(x, y);
        Edge north = new Edge(currVertex, new Vertex(x, y - 1), rand.nextInt(area));
        Edge east = new Edge(currVertex, new Vertex(x + 1, y), rand.nextInt(area));
        Edge south = new Edge(currVertex, new Vertex(x, y + 1), rand.nextInt(area));
        Edge west = new Edge(currVertex, new Vertex(x - 1, y), rand.nextInt(area));
        if (y > 0) {
          utils.addNoDupes(edges, north);
        }
        if (y < height - 1) {
          utils.addNoDupes(edges, south);
        }
        if (x > 0) {
          utils.addNoDupes(edges, west);
        }
        if (x < width - 1) {
          utils.addNoDupes(edges, east);
        }
      }
    }
    return edges;
  }

  // returns a minimum spanning tree based on Kruskal algorithm
  ArrayList<Edge> kruskal(ArrayList<Edge> worklist, ArrayList<Vertex> vertices) {
    ArrayList<Edge> workSorted = this.utils.quicksort(worklist, new EdgeComparator());
    HashMap<Vertex, Vertex> reps = new HashMap<>();
    for (Vertex v : vertices) {
      reps.put(v, v);
    }
    ArrayList<Edge> goodEdges = new ArrayList<>();
    while (goodEdges.size() < vertices.size() - 1) {
      if (workSorted.size() > 0) {
        if (workSorted.get(0).causesCycle(reps)) {
          workSorted.remove(0);
        } else {
          goodEdges.add(workSorted.remove(0));
        }
      } else {
        throw new Error("Not enough edges for the given nodes");
      }
    }
    return goodEdges;
  }

  @Override
  // returns the current worldScene
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(this.width * this.responsiveSize,
        this.height * this.responsiveSize);
    for (Vertex v : this.allVertices) {
      v.drawVertex(ws, this.responsiveSize, this.width, this.height);
    }
    for (Edge e : this.edgesInTree) {
      e.drawEdge(ws, this.responsiveSize);
    }
    return ws;
  }

  // responsively returns the correct cell size for the maze's width and height
  int responsiveSize() {
    if (Vertex.CELL_SIZE * this.width > 1000 || Vertex.CELL_SIZE * this.height > 1000) {
      return Vertex.CELL_SIZE / 2;
    } else {
      return Vertex.CELL_SIZE;
    }
  }
}

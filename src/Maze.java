import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javalib.impworld.*;


// represents a maze
class Maze extends World {
  int width;
  int height;
  ArrayList<Edge> allEdges;
  ArrayList<Vertex> allVertices;
  ArrayList<Edge> edgesInTree;
  Utils utils;
  Random rand;

  Maze(ArrayList<Edge> worklist) {
    this.rand = new Random();
    this.utils = new Utils();
    this.allEdges = worklist;
    this.allVertices = this.utils.collectVertices(this.allEdges);
    this.edgesInTree = this.kruskal(this.allEdges, this.allVertices);
  }

  Maze(int width, int height) {
    this.rand = new Random();
    this.utils = new Utils();
    this.width = width;
    this.height = height;
    this.allEdges = this.generateGraph();
    this.allVertices = this.utils.collectVertices(this.allEdges);
    this.edgesInTree = this.kruskal(this.allEdges, this.allVertices);
  }

  public static void main(String args[]) {
    /*ArrayList<Edge> edges = new ArrayList<>();
    edges.add(new Edge("A", "B", 30));
    edges.add(new Edge("F", "D", 50));
    edges.add(new Edge("B", "E", 35));
    edges.add(new Edge("E", "C", 15));
    edges.add(new Edge("A", "E", 50));
    edges.add(new Edge("B", "C", 40));
    edges.add(new Edge("B", "F", 50));
    edges.add(new Edge("C", "D", 25));
    Maze maze = new Maze(edges);
    maze.kruskal();
    maze.utils.printList(maze.edgesInTree);

    ArrayList<Edge> edges2 = new ArrayList<>();
    edges2.add(new Edge("H", "I", 7));
    edges2.add(new Edge("I", "C", 2));
    edges2.add(new Edge("D", "E", 9));
    edges2.add(new Edge("H", "G", 1));
    edges2.add(new Edge("D", "F", 14));
    edges2.add(new Edge("A", "H", 8));
    edges2.add(new Edge("A", "B", 4));
    edges2.add(new Edge("B", "H", 11));
    edges2.add(new Edge("I", "G", 6));
    edges2.add(new Edge("G", "F", 2));
    edges2.add(new Edge("B", "C", 8));
    edges2.add(new Edge("C", "F", 4));
    edges2.add(new Edge("F", "E", 10));
    edges2.add(new Edge("C", "D", 7));
    Maze m2 = new Maze(edges2);
    m2.kruskal();
    m2.utils.printList(m2.edgesInTree);*/

    Maze m3 = new Maze(50, 50);
    m3.bigBang(m3.width * Vertex.CELL_SIZE, m3.height * Vertex.CELL_SIZE, 3);
  }

  ArrayList<Edge> generateGraph() {
    ArrayList<Edge> edges = new ArrayList<>();
    int area = this.width * this.height;
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
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(this.width * Vertex.CELL_SIZE, this.height * Vertex.CELL_SIZE);
    for (Vertex v : this.allVertices) {
      v.drawVertex(ws);
    }
    for (Edge e : this.edgesInTree) {
      e.drawEdge(ws);
    }
    return ws;
  }
}

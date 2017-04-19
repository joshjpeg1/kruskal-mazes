import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javalib.impworld.*;
import javalib.worldimages.FontStyle;
import javalib.worldimages.TextImage;

// represents a Maze
class Maze extends World {
  int width;
  int height;
  ArrayList<Edge> allEdges;
  ArrayList<Vertex> allVertices;
  ArrayList<Edge> edgesInTree;
  ArrayList<Edge> solution;
  Utils utils;
  Random rand;
  int responsiveSize;
  Player player;
  int horizFactor;
  int vertFactor;
  ArrayList<Edge> animateList;
  boolean animating;
  boolean drawSolution;

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
    this.horizFactor = 1;
    this.vertFactor = 1;
    this.generateGraph();
    this.responsiveSize = this.responsiveSize();
    this.solution = new ArrayList<>();
    this.player = new Player(this.allVertices.get(0));
    this.animating = true;
    this.drawSolution = false;
    this.animateList = new ArrayList<>();
    for (Edge e : this.edgesInTree) {
      this.animateList.add(e);
    }
    this.edgesInTree = new ArrayList<>();
  }

  void generateGraph() {
    this.allEdges = this.generateGraph(this.width, this.height);
    this.allVertices = this.utils.collectVertices(this.allEdges);
    this.edgesInTree = this.kruskal(this.allEdges, this.allVertices);
  }

  // runs the Maze application
  public static void main(String[] argv) {
    Maze maze = new Maze(20, 20);
    maze.bigBang(maze.width * maze.responsiveSize, maze.height * maze.responsiveSize, .01);
  }

  // generates a randomly weighted graph
  ArrayList<Edge> generateGraph(int width, int height) {
    ArrayList<Edge> edges = new ArrayList<>();
    int area = width * height;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        Vertex currVertex = new Vertex(x, y);
        // to make more vertical passages, multiply horizontal edges' weights by 2
        // to make more horizontal passages, multiply vertical edges' weights by 2
        Edge north = new Edge(currVertex, new Vertex(x, y - 1), rand.nextInt(area) * horizFactor);
        Edge east = new Edge(currVertex, new Vertex(x + 1, y), rand.nextInt(area) * vertFactor);
        Edge south = new Edge(currVertex, new Vertex(x, y + 1), rand.nextInt(area) * horizFactor);
        Edge west = new Edge(currVertex, new Vertex(x - 1, y), rand.nextInt(area) * vertFactor);
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
      v.drawVertex(ws, this.responsiveSize, this.width, this.height, false);
    }
    if (this.animating) {
      if (this.animateList.size() == 0) {
        this.animating = false;
        this.drawSolution = false;
      } else {
        if (this.drawSolution) {
          this.solution.add(0, this.animateList.remove(0));
          this.solution.get(0).correctEdge(this.allVertices);
        } else {
          this.edgesInTree.add(0, this.animateList.remove(0));
        }
      }
    }
    player.drawPlayer(ws, this.responsiveSize, this.width, this.height);
    for (Edge e : this.edgesInTree) {
      e.drawEdge(ws, this.responsiveSize);
    }
    for (Edge e : player.visited) {
      e.drawEdge(ws, this.responsiveSize);
    }
    for (Edge e : this.solution) {
      e.drawEdge(ws, this.responsiveSize);
    }
    return ws;
  }

  @Override
  public void onKeyEvent(String s) {
    System.out.println(s);
    if (s.equals("n") || s.equals("h") || s.equals("v")) {
      if (s.equals("h")) {
        this.horizFactor = 2;
        this.vertFactor = 1;
      } else if (s.equals("v")) {
        this.horizFactor = 1;
        this.vertFactor = 2;
      } else {
        this.horizFactor = 1;
        this.vertFactor = 1;
      }
      this.generateGraph();
      this.solution = new ArrayList<>();
      this.animating = true;
      this.drawSolution = false;
      this.animateList = new ArrayList<>();
      for (Edge e : this.edgesInTree) {
        this.animateList.add(e);
      }
      this.edgesInTree = new ArrayList<>();
    } else if (s.equals("escape")) {
      System.exit(0);
    } else if (!this.animating) {
      if (s.equals("b") || s.equals("d") || s.equals("s")) {
        for (Edge e : this.edgesInTree) {
          e.resetEdge(this.allVertices);
        }
        if (s.equals("b")) {
          this.solution = this.search(new Vertex(0, 0),
            new Vertex(this.width - 1, this.height - 1), this.edgesInTree, true);
        } else if (s.equals("d") || s.equals("s")) {
          this.solution = this.search(new Vertex(0, 0),
            new Vertex(this.width - 1, this.height - 1), this.edgesInTree, false);
          if (s.equals("s")) {
            for (Edge e : this.edgesInTree) {
              e.resetEdge(this.allVertices);
            }
          }
        }
        this.animating = true;
        this.drawSolution = true;
        this.animateList = new ArrayList<>();
        for (Edge e : solution) {
          this.animateList.add(e);
        }
        this.solution = new ArrayList<>();
      } else if (s.equals("r")) {
        for (Edge e : this.edgesInTree) {
          e.resetEdge(this.allVertices);
        }
        this.solution = new ArrayList<>();
      } else if (s.equals("up") || s.equals("left") || s.equals("down") || s.equals("right")) {
        int index;
        if (s.equals("up")) {
          index = player.movePlayer(0, -1, this.edgesInTree, this.width, this.height);
        } else if (s.equals("left")) {
          index = player.movePlayer(-1, 0, this.edgesInTree, this.width, this.height);
        } else if (s.equals("down")) {
          index = player.movePlayer(0, 1, this.edgesInTree, this.width, this.height);
        } else {
          index = player.movePlayer(1, 0, this.edgesInTree, this.width, this.height);
        }
        if (index > 0 && index < this.edgesInTree.size()) {
          this.edgesInTree.get(index).userEdge(this.allVertices);
        }
      }  else {
        return;
      }
    } else {
      return;
    }
  }

  // responsively returns the correct cell size for the maze's width and height
  int responsiveSize() {
    if (Vertex.CELL_SIZE * this.width > 1000 || Vertex.CELL_SIZE * this.height > 1000) {
      return Vertex.CELL_SIZE / 2;
    } else {
      return Vertex.CELL_SIZE;
    }
  }

  //
  ArrayList<Edge> search(Vertex start, Vertex end, ArrayList<Edge> edges, boolean breadth) {
    IDeque<Vertex> worklist;
    if (breadth) {
      worklist = new Queue<>();
    } else {
      worklist = new Stack<>();
    }
    HashMap<Vertex, Edge> cameFromEdge = new HashMap<>();
    worklist.push(start);
    Vertex last = start;
    ArrayList<Vertex> visited = new ArrayList<>();
    ArrayList<Edge> solution = new ArrayList<>();
    while (!worklist.empty()) {
      Vertex next = worklist.peek();
      if (visited.contains(next)) {
        worklist.pop();
      } else if (next.equals(end)) {
        solution = this.utils.reverseArr(this.searchHelp(start, cameFromEdge, next));
        return solution;
      } else {
        ArrayList<Vertex> neighbors = this.utils.getNeighbors(next, edges);
        worklist.pop();
        for (Vertex v : neighbors) {
          if (!visited.contains(v)) {
            worklist.push(v);
            cameFromEdge.put(v, new Edge(next, v, 0));
          }
        }
        visited.add(next);
        last = next;
      }
    }
    return solution;
  }

  ArrayList<Edge> searchHelp(Vertex start, HashMap<Vertex, Edge> cameFromEdge, Vertex v) {
    ArrayList<Edge> solution = new ArrayList<>();

    //this.utils.print(allVertices, cameFromEdge);
    ArrayList<Vertex> keys = this.utils.getKeys(cameFromEdge, allVertices);
    for (Vertex vert : this.allVertices) {
      if (keys.contains(vert)) {
        vert.visitVertex();
      }
    }
    ArrayList<Edge> worklist = this.utils.getValues(cameFromEdge, allVertices);
    for (Edge ed : this.edgesInTree) {
      if (worklist.contains(ed)) {
        ed.visitEdge(keys);
      }
    }

    while(!v.equals(start)) {
      /*solution.add(cameFromEdge.get(v));
      v = cameFromEdge.get(v).getOther(v);*/


      // QUICKEST SOLUTION
      for (Edge e : worklist) {
        if (e.to.equals(v)) {
          v = e.from;
          solution.add(worklist.get(worklist.indexOf(e)));
        }
      }
    }
    return solution;
  }
}
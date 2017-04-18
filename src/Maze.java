import java.awt.*;
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
  ArrayList<Edge> solution;
  Utils utils;
  Random rand;
  int responsiveSize;
  Player player;
  int horizFactor;
  int vertFactor;
  ArrayList<Edge> animateList;
  boolean animating;
  int drawingWhat;

  static final int MAZEWALLS = 0;
  static final int SEARCHALGO = 1;
  static final int SOLUTION = 2;

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
    this.drawingWhat = MAZEWALLS;
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
        this.drawingWhat = -1;
      } else {
        if (this.drawingWhat == Maze.SOLUTION) {
          this.solution.add(0, this.animateList.remove(0));
          this.solution.get(0).correctEdge(this.allVertices);
        } else if (this.drawingWhat == Maze.SEARCHALGO) {
          this.solution.add(0, this.animateList.remove(0));
          this.solution.get(0).visitEdge(this.allVertices);
        } else if (this.drawingWhat == Maze.MAZEWALLS) {
          this.edgesInTree.add(0, this.animateList.remove(0));
        }
      }
    }
    //player.drawPlayer(ws, this.responsiveSize, this.width, this.height);
    for (Edge e : this.edgesInTree) {
      e.drawEdge(ws, this.responsiveSize);
    }
    for (Edge e : this.solution) {
      e.drawEdge(ws, this.responsiveSize);
    }
    return ws;
  }

  public void animate(boolean breadth) {
    this.animating = true;
    this.animateList = new ArrayList<>();
    if (this.drawingWhat == MAZEWALLS) {
      for (Edge e : this.edgesInTree) {
        this.animateList.add(e);
      }
      this.edgesInTree = new ArrayList<>();
      this.solution = new ArrayList<>();
    } else if (this.drawingWhat == SEARCHALGO) {
      for (Edge e : this.edgesInTree) {
        e.resetEdge(this.allVertices);
      }
    } else if (this.drawingWhat == SOLUTION) {
      /*for (Edge e : this.edgesInTree) {
        e.resetEdge(this.allVertices);
      }*/
    } else {
      this.animating = false;
      // don't do anything
    }
  }

  @Override
  public void onKeyEvent(String s) {
    System.out.println(s);
    if (s.equals("n") || s.equals("h") || s.equals("v")) {
      for (Edge e : this.edgesInTree) {
        e.resetEdge(this.allVertices);
      }
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
      this.drawingWhat = MAZEWALLS;
      this.animate(false);
    } else if (s.equals("escape")) {
      System.exit(0);
    } else if (!this.animating) {
      if (s.equals("b") || s.equals("d") || s.equals("s")) {

        this.animating = true;
        this.animateList = new ArrayList<>();
        ArrayList<Edge> localSol = new ArrayList<>();
        if (s.equals("b") || s.equals("d")) {
          for (Edge e : this.edgesInTree) {
            e.resetEdge(this.allVertices);
          }
          if (s.equals("b")) {
            localSol = this.search(new Vertex(0, 0),
              new Vertex(this.width - 1, this.height - 1), this.edgesInTree, true, false);
          } else {
            localSol = this.search(new Vertex(0, 0),
              new Vertex(this.width - 1, this.height - 1), this.edgesInTree, false, false);
          }
          this.drawingWhat = SEARCHALGO;
          this.solution = new ArrayList<>();
        } else if (s.equals("s")) {
          localSol = this.search(new Vertex(0, 0),
            new Vertex(this.width - 1, this.height - 1), this.edgesInTree, false, true);
          /*for (Edge e : this.edgesInTree) {
            e.resetEdge(this.allVertices);
          }*/
          this.drawingWhat = SOLUTION;
        }

        this.utils.print(localSol);
        System.out.println("solution size: " + localSol.size());
        for (Edge e : localSol) {
          this.animateList.add(e);
        }
        localSol = new ArrayList<>();
        System.out.println("solution size: " + this.animateList.size());


      } else if (s.equals("r")) {
        for (Edge e : this.edgesInTree) {
          e.resetEdge(this.allVertices);
        }
        this.solution = new ArrayList<>();
      } else if (s.equals("up")) {

      } else if (s.equals("left")) {

      } else if (s.equals("down")) {

      } else if (s.equals("right")) {

      } else {
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
  ArrayList<Edge> search(Vertex start, Vertex end, ArrayList<Edge> edges, boolean breadth,
                         boolean solved) {
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
    ArrayList<Edge> searchList = new ArrayList<>();
    while (!worklist.empty()) {
      Vertex next = worklist.peek();
      if (visited.contains(next)) {
        worklist.pop();
      } else if (next.equals(end)) {
        if (solved) {
          return this.utils.reverseArr(this.searchHelp(start, cameFromEdge, next));
        } else {
          searchList.add(new Edge(last, next, 0));
          return searchList;
        }
      } else {
        ArrayList<Vertex> neighbors = this.utils.getNeighbors(next, edges);
        worklist.pop();

        for (Vertex v : neighbors) {
          if (!visited.contains(v)) {
            worklist.push(v);
            cameFromEdge.put(v, new Edge(next, v, 0));
            searchList.add(new Edge(next, v, 0));
          }
        }
        visited.add(next);
        last = next;
      }
    }
    return searchList;
  }

  ArrayList<Edge> searchHelp(Vertex start, HashMap<Vertex, Edge> cameFromEdge, Vertex v) {
    /*ArrayList<Vertex> keys = this.utils.getKeys(cameFromEdge, allVertices);
    for (Vertex vert : this.allVertices) {
      if (keys.contains(vert)) {
        vert.visitVertex();
      }
    }*/
    ArrayList<Edge> solution = new ArrayList<>();
    ArrayList<Edge> worklist = this.utils.getValues(cameFromEdge, allVertices);
    /*for (Edge ed : this.edgesInTree) {
      if (worklist.contains(ed)) {
        ed.visitEdge(keys);
      }
    }*/

    while(!v.equals(start)) {
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
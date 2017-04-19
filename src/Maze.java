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
  ArrayList<Edge> breadth;
  ArrayList<Edge> depth;
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
  int speed;

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
    this.player = new Player(this.allVertices.get(0));
    this.animating = true;
    this.drawingWhat = MAZEWALLS;
    this.animateList = new ArrayList<>();
    for (Edge e : this.edgesInTree) {
      this.animateList.add(e);
    }
    this.edgesInTree = new ArrayList<>();
    speed = 0;
  }

  void generateGraph() {
    this.allEdges = this.generateGraph(this.width, this.height);
    this.allVertices = this.utils.collectVertices(this.allEdges);
    this.edgesInTree = this.kruskal(this.allEdges, this.allVertices);
    this.breadth = this.search(new Vertex(0, 0),
      new Vertex(this.width - 1, this.height - 1), this.edgesInTree, true, false);
    this.depth = this.search(new Vertex(0, 0),
      new Vertex(this.width - 1, this.height - 1), this.edgesInTree, false, false);
    this.solution = this.search(new Vertex(0, 0),
      new Vertex(this.width - 1, this.height - 1), this.edgesInTree, false, true);
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
        if (this.drawingWhat == Maze.SEARCHALGO) {
          this.drawingWhat = Maze.SOLUTION;
          this.startAnimation(false);
        } else {
          this.animating = false;
          this.drawingWhat = -1;
        }
      } else {
        if (this.drawingWhat == Maze.SOLUTION || this.drawingWhat == Maze.SEARCHALGO) {
          if (this.speed % 2 == 0) {
            this.speed = 1;
            int index = this.utils.edgeIndex(this.edgesInTree, this.animateList.remove(0));
            if (index == -1) {
              this.utils.print(edgesInTree);
            }
            if (this.drawingWhat == Maze.SOLUTION) {
              this.edgesInTree.get(index).correctEdge(this.allVertices);
            } else {
              this.edgesInTree.get(index).visitEdge(this.allVertices);
            }
          } else {
            this.speed += 1;
          }
        } else if (this.drawingWhat == Maze.MAZEWALLS) {
          this.edgesInTree.add(0, this.animateList.remove(0));
        }
      }
    }
    player.drawPlayer(ws, this.responsiveSize, this.width, this.height);
    for (Edge e : this.edgesInTree) {
      e.drawEdge(ws, this.responsiveSize);
    }/*
    for (Edge e : player.visited) {
      e.drawEdge(ws, this.responsiveSize);
    }*/
    return ws;
  }

  public void startAnimation(boolean breadth) {
    this.animating = true;
    this.animateList = new ArrayList<>();

    if (this.drawingWhat == MAZEWALLS) {
      for (Edge e : this.edgesInTree) {
        this.animateList.add(e);
      }
      this.edgesInTree = new ArrayList<>();
    } else if (this.drawingWhat == SEARCHALGO || this.drawingWhat == SOLUTION) {
      ArrayList<Edge> localSol = new ArrayList<>();
      if (this.drawingWhat == SOLUTION) {
        for (Edge e : this.solution) {
          localSol.add(e);
        }
      } else {
        for (Edge e : this.edgesInTree) {
          e.resetEdge(this.allVertices);
        }
        if (breadth) {
          for (Edge e : this.breadth) {
            localSol.add(e);
          }
          this.utils.print(localSol);
        } else {
          for (Edge e : this.depth) {
            localSol.add(e);
          }
        }
      }
      for (Edge e : localSol) {
        this.animateList.add(e);
      }
    } else {
      this.animating = false;
      // don't do anything
    }
  }

  @Override
  public void onKeyEvent(String s) {
    s = s.toLowerCase();
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
      this.startAnimation(false);
    } else if (s.equals("escape")) {
      System.exit(0);
    } else if (!this.animating) {
      if (s.equals("b")) {
        this.drawingWhat = SEARCHALGO;
        this.startAnimation(true);
      } else if (s.equals("d")) {
        this.drawingWhat = SEARCHALGO;
        this.startAnimation(false);
      } /*else if (s.equals("s")) {
        this.drawingWhat = SOLUTION;
        this.startAnimation(false);
      }*/ else if (s.equals("r")) {
        for (Edge e : this.edgesInTree) {
          e.resetEdge(this.allVertices);
        }
        //this.solution = new ArrayList<>();
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
          Vertex last = next;
          boolean stop = false;
          for (Edge e : searchList) {
            if (!stop && e.containsVertex(next)) {
              last = e.getOther(next);
              stop = true;
            }
          }
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
      }
    }
    return searchList;
  }

  ArrayList<Edge> searchHelp(Vertex start, HashMap<Vertex, Edge> cameFromEdge, Vertex v) {
    ArrayList<Edge> result = new ArrayList<>();
    ArrayList<Edge> worklist = this.utils.getValues(cameFromEdge, allVertices);

    while(!v.equals(start)) {
      for (Edge e : worklist) {
        if (e.to.equals(v)) {
          v = e.from;
          result.add(worklist.get(worklist.indexOf(e)));
        }
      }
    }
    return result;
  }
}
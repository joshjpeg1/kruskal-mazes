import java.util.ArrayList;
import java.util.HashMap;

// represents a maze
class Maze {
  HashMap<String, String> representatives;
  ArrayList<Edge> edgesInTree;
  ArrayList<Edge> worklist;
  ArrayUtils utils;

  /*HashMap<String, String> kruskal(ArrayList<Edge> edges) {

  }*/

  Maze(ArrayList<Edge> worklist) {
    this.utils = new ArrayUtils();
    this.worklist = worklist;
    this.edgesInTree = new ArrayList<>();
    this.representatives = new HashMap<>();
  }

  Maze(int width, int height) {
    this.utils = new ArrayUtils();
    this.worklist = this.generateGraph(width * height);
    this.edgesInTree = new ArrayList<>();
    this.representatives = new HashMap<>();
    //this.kruskal();
  }

  public static void main(String args[]) {
    ArrayList<Edge> edges = new ArrayList<>();
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
    m2.utils.printList(m2.edgesInTree);
  }

  ArrayList<Edge> generateGraph(int numNodes) {
    ArrayList<Edge> edges = new ArrayList<>();
    for (int i = 0; i < numNodes; i += 1) {
      edges.add(new Edge(Integer.toString(i), Integer.toString(i), i));
    }
    return edges;
  }

  // returns a minimum spanning tree based on Kruskal algorithm
  ArrayList<Edge> kruskal() {
    ArrayList<Edge> workSorted = this.utils.quicksort(this.worklist, new EdgeComparator());
    ArrayList<String> nodes = this.utils.collectNodes(this.worklist);
    for (String s : nodes) {
      this.representatives.put(s, s);
    }
    while (this.edgesInTree.size() < nodes.size() - 1) {
      if (workSorted.size() > 0) {
        if (workSorted.get(0).causesCycle(this.representatives)) {
          workSorted.remove(0);
        } else {
          this.edgesInTree.add(workSorted.remove(0));
        }
      } else {
        throw new Error("Not enough edges for the given nodes");
      }
    }
    /*System.out.print("Nodes:    ");
    for (String s : nodes) {
      System.out.print(s + " ");
    }
    System.out.print("\nLinks:    ");
    for (String s : nodes) {
      System.out.print(this.representatives.get(s) + " ");
    }
    System.out.print("\n");*/
    return this.edgesInTree;
  }
}

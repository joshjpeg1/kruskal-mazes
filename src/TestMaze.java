import tester.Tester;
import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// tests the methods for Mazes
public class TestMaze {
  Utils utils = new Utils();

  // tests the hashCode method for Vertex
  boolean testHashCode(Tester t) {
    Vertex ex1 = new Vertex(1, 1);
    Vertex ex2 = new Vertex(2, 1);
    Vertex ex3 = new Vertex(1, 2);

    return t.checkExpect(ex1.hashCode(), 1001)
      && t.checkExpect(ex2.hashCode(), 2001)
      && t.checkExpect(ex3.hashCode(), 1002);
  }

  // tests the equals method for Vertex
  boolean testEqualsV(Tester t) {
    Vertex ex1 = new Vertex(1, 1);
    Vertex ex2 = new Vertex(2, 1);
    Vertex ex3 = new Vertex(1, 2);
    Vertex ex4 = new Vertex(1, 1);
    Edge edge = new Edge(ex1, ex2, 15);

    return t.checkExpect(ex1.equals(edge), false)
      && t.checkExpect(ex1.equals(ex2), false)
      && t.checkExpect(ex2.equals(ex3), false)
      && t.checkExpect(ex1.equals(ex4), true);

  }

  // tests the direction method for Vertex
  boolean testDirection(Tester t) {
    Vertex ex1 = new Vertex(1, 0);
    Vertex ex2 = new Vertex(0, 1);
    Vertex exMid = new Vertex(1, 1);
    Vertex ex3 = new Vertex(2, 1);
    Vertex ex4 = new Vertex(1, 2);

    return t.checkExpect(exMid.direction(ex1), Vertex.NORTH)
      && t.checkExpect(exMid.direction(ex2), Vertex.WEST)
      && t.checkExpect(exMid.direction(ex3), Vertex.EAST)
      && t.checkExpect(exMid.direction(ex4), Vertex.SOUTH);
  }

  // tests the cellColor method for Vertex
  boolean testCellColor(Tester t) {
    Vertex ex1 = new Vertex(0, 0);
    Vertex ex2 = new Vertex(2, 2);
    Vertex ex3 = new Vertex(1, 1);

    return t.checkExpect(ex1.cellColor(3, 3, false), new Color(33, 127, 70))
      && t.checkExpect(ex2.cellColor(3, 3, false), new Color(108, 32, 128))
      && t.checkExpect(ex3.cellColor(3, 3, false), new Color(192, 192, 192));
  }

  // tests the equals method for Edge
  boolean testEqualsE(Tester t) {
    Edge ex1 = new Edge(new Vertex(0, 0), new Vertex(1, 0), 10);
    Edge ex2 = new Edge(new Vertex(1, 0), new Vertex(1, 2), 12);
    Edge ex3 = new Edge(new Vertex(1, 0), new Vertex(1, 1), 10);
    Edge ex4 = new Edge(new Vertex(1, 1), new Vertex(1, 0), 12);
    Edge ex5 = new Edge(new Vertex(1, 1), new Vertex(1, 0), 12);
    Edge ex6 = new Edge(new Vertex(1, 0), new Vertex(1, 1), 12);
    Edge ex7 = new Edge(new Vertex(1, 0), new Vertex(1, 1), 10);

    return t.checkExpect(ex1.equals(new Vertex(0, 0)), false)
      && t.checkExpect(ex1.equals(ex2), false)
      && t.checkExpect(ex1.equals(ex3), false)
      && t.checkExpect(ex2.equals(ex3), false)
      && t.checkExpect(ex2.equals(ex4), false)
      && t.checkExpect(ex4.equals(ex5), true)
      && t.checkExpect(ex5.equals(ex6), true);
  }

  // tests the compareWeights method for Edge
  boolean testCompareWeights(Tester t) {
    Edge ex1 = new Edge(new Vertex(0, 0), new Vertex(1, 0), 10);
    Edge ex2 = new Edge(new Vertex(1, 0), new Vertex(1, 2), 12);
    Edge ex3 = new Edge(new Vertex(1, 0), new Vertex(1, 1), 10);

    return t.checkExpect(ex1.compareWeights(ex2), -1)
      && t.checkExpect(ex2.compareWeights(ex3), 1)
      && t.checkExpect(ex1.compareWeights(ex3), 0);
  }

  // tests the append method for Utils
  boolean testAppend(Tester t) {
    ArrayList<String> ex1 = new ArrayList<>(Arrays.asList("cow", "milk", "moo"));
    ArrayList<String> ex2 = new ArrayList<>(Arrays.asList("chocolate", "vanilla", "strawberry"));
    ArrayList<String> ex3 = new ArrayList<>(Arrays.asList("cow", "milk", "moo",
        "chocolate", "vanilla", "strawberry"));
    ArrayList<String> ex4 = new ArrayList<>();
    ArrayList<String> ex5 = new ArrayList<>();
    ArrayList<String> ex6 = new ArrayList<>(Arrays.asList("bumblebee"));
    ArrayList<String> ex7 = new ArrayList<>(Arrays.asList("sugarloaf"));


    return t.checkExpect(this.utils.append(ex1, ex2), ex3)
      && t.checkExpect(this.utils.append(ex4, ex5), ex4)
      && t.checkExpect(this.utils.append(ex4, ex6), ex6)
      && t.checkExpect(this.utils.append(ex7, ex5), ex7);
  }

  // tests the quicksort method for Utils
  boolean testQuicksort(Tester t) {
    Vertex exv1 = new Vertex(0, 0);
    Vertex exv2 = new Vertex(1, 0);
    Vertex exv3 = new Vertex(2, 0);

    Edge ex1 = new Edge(exv1, exv1, 0);
    Edge ex2 = new Edge(exv1, exv2, 1);
    Edge ex3 = new Edge(exv1, exv3, 2);
    Edge ex4 = new Edge(exv2, exv1, 3);
    Edge ex5 = new Edge(exv2, exv2, 4);
    Edge ex6 = new Edge(exv2, exv3, 5);

    ArrayList<Edge> unsorted = new ArrayList<>();
    unsorted.add(ex4);
    unsorted.add(ex2);
    unsorted.add(ex5);
    unsorted.add(ex6);
    unsorted.add(ex1);
    unsorted.add(ex3);

    ArrayList<Edge> sorted = new ArrayList<>();
    unsorted.add(ex1);
    unsorted.add(ex2);
    unsorted.add(ex3);
    unsorted.add(ex4);
    unsorted.add(ex5);
    unsorted.add(ex6);

    return t.checkExpect(this.utils.quicksort(unsorted, new EdgeComparator()), sorted);
  }

  // tests the quicksortHelp method for Utils
  boolean testQuicksortHelp(Tester t) {
    Vertex exv1 = new Vertex(0, 0);
    Vertex exv2 = new Vertex(1, 0);
    Vertex exv3 = new Vertex(2, 0);

    Edge ex1 = new Edge(exv1, exv1, 0);
    Edge ex2 = new Edge(exv1, exv2, 1);
    Edge ex3 = new Edge(exv1, exv3, 2);
    Edge ex4 = new Edge(exv2, exv1, 3);
    Edge ex5 = new Edge(exv2, exv2, 4);
    Edge ex6 = new Edge(exv2, exv3, 5);
    Edge ex7 = new Edge(exv1, exv3, 3);

    ArrayList<Edge> unsorted = new ArrayList<>();
    unsorted.add(ex2);
    unsorted.add(ex5);
    unsorted.add(ex7);
    unsorted.add(ex6);
    unsorted.add(ex1);
    unsorted.add(ex3);

    ArrayList<Edge> befores = new ArrayList<>();
    befores.add(ex2);
    befores.add(ex1);
    befores.add(ex3);

    ArrayList<Edge> afters = new ArrayList<>();
    afters.add(ex5);
    afters.add(ex7);
    afters.add(ex6);

    return t.checkExpect(this.utils.quicksortHelp(ex4, unsorted, new EdgeComparator(), true),
      befores)
      && t.checkExpect(this.utils.quicksortHelp(ex4, unsorted, new EdgeComparator(), false),
      afters);
  }

  // tests the addNoDupes method for Utils
  boolean testAddNoDupes(Tester t) {
    ArrayList<String> endp = new ArrayList<>();
    ArrayList<String> dList = new ArrayList<>(Arrays.asList("D"));
    ArrayList<String> ex1 = new ArrayList<>(Arrays.asList("A", "B", "C"));
    ArrayList<String> ex2 = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

    return t.checkExpect(this.utils.addNoDupes(endp, "D"), dList)
      && t.checkExpect(this.utils.addNoDupes(ex1, "D"), ex2)
      && t.checkExpect(this.utils.addNoDupes(ex2, "D"), ex1);
  }

  // tests the collectVertices method for Utils
  boolean testCollectVertices(Tester t) {
    Vertex exv1 = new Vertex(0, 0);
    Vertex exv2 = new Vertex(1, 0);
    Vertex exv3 = new Vertex(1, 2);
    Vertex exv4 = new Vertex(1, 1);
    Edge ex1 = new Edge(exv1, exv2, 10);
    Edge ex2 = new Edge(exv2, exv3, 12);
    Edge ex3 = new Edge(exv2, exv4, 10);
    ArrayList<Edge> exArr0 = new ArrayList<>();
    exArr0.add(ex1);
    exArr0.add(ex2);
    exArr0.add(ex3);
    ArrayList<Vertex> exArr1 = new ArrayList<>();
    exArr1.add(exv1);
    exArr1.add(exv2);
    exArr1.add(exv3);
    exArr1.add(exv4);

    return t.checkExpect(this.utils.collectVertices(exArr0), exArr1);
  }

  // tests the cycle method for Utils
  boolean testCycle(Tester t) {
    HashMap<String, String> hash = new HashMap<>();
    hash.put("A", "A");
    hash.put("B", "B");
    hash.put("C", "C");
    hash.put("D", "D");
    hash.put("E", "E");
    hash.put("F", "F");

    return t.checkExpect(this.utils.cycle(hash, "E", "C"), false)
      && t.checkExpect(this.utils.cycle(hash, "C", "D"), false)
      && t.checkExpect(this.utils.cycle(hash, "A", "B"), false)
      && t.checkExpect(this.utils.cycle(hash, "B", "E"), false)
      && t.checkExpect(this.utils.cycle(hash, "B", "C"), true)
      && t.checkExpect(this.utils.cycle(hash, "F", "D"), false)
      && t.checkExpect(this.utils.cycle(hash, "A", "E"), true)
      && t.checkExpect(this.utils.cycle(hash, "B", "F"), true);
  }

  // tests the findRoot method for Utils
  boolean testFindRoot(Tester t) {
    HashMap<String, String> hash = new HashMap<>();
    hash.put("A", "E");
    hash.put("B", "A");
    hash.put("C", "E");
    hash.put("D", "E");
    hash.put("E", "E");
    hash.put("F", "D");

    return t.checkException(new NullPointerException("Given element does not exist"),
      this.utils, "findRoot", hash, "G")
      && t.checkExpect(this.utils.findRoot(hash, "A"), "E")
      && t.checkExpect(this.utils.findRoot(hash, "B"), "E")
      && t.checkExpect(this.utils.findRoot(hash, "C"), "E")
      && t.checkExpect(this.utils.findRoot(hash, "D"), "E")
      && t.checkExpect(this.utils.findRoot(hash, "E"), "E")
      && t.checkExpect(this.utils.findRoot(hash, "F"), "E");
  }

  // tests the generateGraph method for Maze
//  boolean testGenerateGraph(Tester t) {
//    Maze maze = new Maze(0, 0);
//    return t.checkExpect(maze.generateGraph(0, 0).size(), 0)
//      && t.checkExpect(maze.generateGraph(3, 3).size(), 12)
//      && t.checkExpect(maze.generateGraph(100, 60).size(), 11840);
//  }


  // tests the kruskal method for Maze
  boolean testKruskal(Tester t) {
    Edge ex1 = new Edge(new Vertex(0, 0), new Vertex(0, 1), 2);
    Edge ex2 = new Edge(new Vertex(0, 0), new Vertex(1, 0), 8);
    Edge ex3 = new Edge(new Vertex(1, 0), new Vertex(1, 1), 2);
    Edge ex4 = new Edge(new Vertex(1, 0), new Vertex(1, 1), 3);
    Edge ex5 = new Edge(new Vertex(2, 0), new Vertex(2, 1), 1);
    Edge ex6 = new Edge(new Vertex(0, 1), new Vertex(0, 2), 2);
    Edge ex7 = new Edge(new Vertex(0, 1), new Vertex(1, 1), 8);
    Edge ex8 = new Edge(new Vertex(1, 1), new Vertex(1, 2), 3);
    Edge ex9 = new Edge(new Vertex(1, 1), new Vertex(2, 1), 0);
    Edge ex10 = new Edge(new Vertex(2, 1), new Vertex(2, 2), 5);
    Edge ex11 = new Edge(new Vertex(0, 2), new Vertex(1, 2), 0);
    Edge ex12 = new Edge(new Vertex(1, 2), new Vertex(2, 2), 2);

    ArrayList<Edge> worklist = new ArrayList<>(Arrays.asList(ex1, ex2, ex3, ex4, ex5, ex6, ex7,
        ex8, ex9, ex10, ex11, ex12));
    ArrayList<Edge> edgesInTree = new ArrayList<>(Arrays.asList(ex9, ex11, ex5, ex1,
        ex3, ex6, ex12, ex8));
    Maze maze = new Maze(worklist);

    return t.checkExpect(maze.kruskal(maze.allEdges, maze.allVertices), edgesInTree);
  }

  // tests the responsiveSize method for Maze
  boolean testResponsiveSize(Tester t) {
    Maze maze1 = new Maze(3, 3);
    Maze maze2 = new Maze(10, 10);
    Maze maze3 = new Maze(100, 60);
    Maze maze4 = new Maze(60, 100);

    return t.checkExpect(maze1.responsiveSize(), Vertex.CELL_SIZE)
      && t.checkExpect(maze2.responsiveSize(), Vertex.CELL_SIZE)
      && t.checkExpect(maze3.responsiveSize(), (Vertex.CELL_SIZE / 2))
      && t.checkExpect(maze4.responsiveSize(), (Vertex.CELL_SIZE / 2));
  }

  // tests the movePlayer method for Player
  void testMovePlayer(Tester t) {
    Maze exampleMaze = new Maze(3, 3);

    Player examplePlay = new Player(new Vertex(0, 0));
    Edge ex1 = new Edge(new Vertex(0, 0), new Vertex(0, 1), 10);
    Edge ex2 = new Edge(new Vertex(0, 1), new Vertex(0, 2), 10);
    Edge ex3 = new Edge(new Vertex(0, 2), new Vertex(1, 2), 10);
    Edge ex4 = new Edge(new Vertex(1, 2), new Vertex(1, 1), 10);
    Edge ex5 = new Edge(new Vertex(1, 1), new Vertex(1, 0), 10);

    ArrayList<Edge> edgesCatalog = new ArrayList();
    edgesCatalog.add(ex1);
    edgesCatalog.add(ex2);
    edgesCatalog.add(ex3);
    edgesCatalog.add(ex4);
    edgesCatalog.add(ex5);

    // attempting to move right
    examplePlay.movePlayer(1, 0, edgesCatalog, exampleMaze.width, exampleMaze.height);
    t.checkExpect(examplePlay, new Player(new Vertex(0, 0)));
    // attempting to move down
    examplePlay.movePlayer(0, 1, edgesCatalog, exampleMaze.width, exampleMaze.height);
    t.checkExpect(examplePlay, new Player(new Vertex(0, 1)));
    // attempting to move down (again)
    examplePlay.movePlayer(0, 1, edgesCatalog, exampleMaze.width, exampleMaze.height);
    t.checkExpect(examplePlay, new Player(new Vertex(0, 2)));
    // attempting to move left
    examplePlay.movePlayer(-1, 0, edgesCatalog, exampleMaze.width, exampleMaze.height);
    t.checkExpect(examplePlay, new Player(new Vertex(0, 2)));
  }

  // getNeighbors in Utils

  // reverseArr in Utils

  // getValues in Utils

  // reportScore in Maze
  
}

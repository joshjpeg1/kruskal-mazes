import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

// represents utilities for ArrayLists and HashMaps
class Utils {
  // appends two ArrayLists together
  <T> ArrayList<T> append(ArrayList<T> a1, ArrayList<T> a2) {
    for (T t : a2) {
      a1.add(t);
    }
    return a1;
  }

  // prints an ArrayList
  <T> void print(ArrayList<T> arr) {
    System.out.print("(");
    for (T t : arr) {
      if (arr.indexOf(t) > 0) {
        System.out.print(" ");
      }
      System.out.print(t.toString() + "");
      if (arr.indexOf(t) < arr.size() - 1) {
        System.out.print(",\n");
      } else {
        System.out.print(")");
      }
    }
    System.out.println("\n");
  }

  // prints a HashMap
  <T, U> void print(ArrayList<T> keys, HashMap<T, U> hash) {
    for (T t : keys) {
      if (hash.containsKey(t)) {
        System.out.println(t.toString() + " --> " + hash.get(t).toString());
      }
    }
    System.out.println("\n");
  }

  // sorts an ArrayList using a quicksort algorithm based on the given comparator
  <T> ArrayList<T> quicksort(ArrayList<T> arr, IComparator<T> comp) {
    if (arr.size() > 2) {
      T pivot = arr.remove(0);
      ArrayList<T> befores = this.quicksortHelp(pivot, arr, comp, true);
      ArrayList<T> afters = this.quicksortHelp(pivot, arr, comp, false);
      befores = this.quicksort(befores, comp);
      afters = this.quicksort(afters, comp);
      afters.add(0, pivot);
      return this.append(befores, afters);
    } else {
      return arr;
    }
  }

  // helper to the quicksort method
  // filters an ArrayList so all remaining elements either come before or after the given pivot
  // based on the given comparator
  <T> ArrayList<T> quicksortHelp(T pivot, ArrayList<T> arr, IComparator<T> comp, boolean before) {
    ArrayList<T> filteredList = new ArrayList<>();
    for (T t : arr) {
      if ((before && comp.compare(pivot, t) > 0) || (!before && comp.compare(pivot, t) <= 0)) {
        filteredList.add(t);
      }
    }
    return filteredList;
  }

  // adds an element to an ArrayList given that the element does not already exist in the list
  <T> ArrayList<T> addNoDupes(ArrayList<T> arr, T t) {
    boolean add = true;
    for (T item : arr) {
      if (add && item.equals(t)) {
        add = false;
      }
    }
    if (add) {
      arr.add(t);
    }
    return arr;
  }

  // collects all of the Vertices from a list of Edges and adds them to an ArrayList
  ArrayList<Vertex> collectVertices(ArrayList<Edge> arr) {
    ArrayList<Vertex> vertices = new ArrayList<>();
    for (Edge e : arr) {
      e.addVertices(vertices);
    }
    return vertices;
  }

  // returns whether or not the given items cause a cycle in a graph
  // if not, updates the HashMap accordingly
  <T> boolean cycle(HashMap<T, T> hash, T t1, T t2) {
    T rootOne = this.findRoot(hash, t1);
    T rootTwo = this.findRoot(hash, t2);
    if (rootOne.equals(rootTwo)) {
      // they are already connected
      return true;
    }
    else {
      // they are separate
      hash.replace(rootTwo, rootOne);
      return false;
    }
  }

  // helper to the cycle method
  // searches a HashMap to find the root connector of the given item
  <T> T findRoot(HashMap<T, T> hash, T t) {
    if (!hash.containsKey(t)) {
      throw new NullPointerException("Given element does not exist");
    } else {
      while (true) {
        if (hash.get(t).equals(t)) {
          return t;
        } else {
          t = hash.get(t);
        }
      }
    }
  }

  <T, U> void addNoDupesHash(HashMap<T, U> hash, T t, U u) {
    if (hash.containsKey(t)) {
      hash.replace(t, u);
    } else {
      hash.put(t, u);
    }
  }

  ArrayList<Vertex> getNeighbors(Vertex v, ArrayList<Edge> edges) {
    ArrayList<Vertex> neighbors = new ArrayList<>();
    for (Edge e : edges) {
      if (e.containsVertex(v)) {
        if (e.from.equals(v)) {
          neighbors.add(e.to);
        } else {
          neighbors.add(e.from);
        }
      }
    }
    return neighbors;
  }

  int getEdge(Vertex v1, Vertex v2, ArrayList<Edge> edges) {
    for (int i = 0; i < edges.size(); i += 1) {
      if (edges.get(i).containsVertex(v1) && edges.get(i).containsVertex(v2)) {
        return i;
      }
    }
    return -1;
  }

  Vertex findOther(Vertex v, ArrayList<Edge> edges) {
    for (Edge e : edges) {
      if (e.containsVertex(v)) {
        return e.getOther(v);
      }
    }
    return v;
  }

  <T> ArrayList<T> reverseArr(ArrayList<T> arr) {
    ArrayList<T> reversed = new ArrayList<>();
    for (T t : arr) {
      reversed.add(0, t);
    }
    return reversed;
  }

  <T, U> ArrayList<U> getValues(HashMap<T, U> hash, ArrayList<T> keys) {
    ArrayList<U> values = new ArrayList<>();
    for (T t : keys) {
      if (hash.containsKey(t)) {
        values.add(hash.get(t));
      }
    }
    return values;
  }

  <T, U> ArrayList<T> getKeys(HashMap<T, U> hash, ArrayList<T> potentialKeys) {
    ArrayList<T> keys = new ArrayList<>();
    for (T t : potentialKeys) {
      if (hash.containsKey(t)) {
        keys.add(t);
      }
    }
    return keys;
  }

  int edgeIndex(ArrayList<Edge> edges, Edge e) {
    for (int i = 0; i < edges.size(); i++) {
      if (e.equals(edges.get(i))) {
        return i;
      }
    }
    return -1;
  }
}
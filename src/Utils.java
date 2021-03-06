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

  // returns the neighbors of the given Vertex as directed by the given ArrayList of Edges
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

  // reverse an array
  <T> ArrayList<T> reverseArr(ArrayList<T> arr) {
    ArrayList<T> reversed = new ArrayList<>();
    for (T t : arr) {
      reversed.add(0, t);
    }
    return reversed;
  }

  // gets the values of a HashMap given the keys and returns them in an ArrayList
  <T, U> ArrayList<U> getValues(HashMap<T, U> hash, ArrayList<T> keys) {
    ArrayList<U> values = new ArrayList<>();
    for (T t : keys) {
      if (hash.containsKey(t)) {
        values.add(hash.get(t));
      }
    }
    return values;
  }
}
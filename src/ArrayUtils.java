import java.util.ArrayList;

// represents utilities for ArrayLists
class ArrayUtils {
  // appends two ArrayLists together
  <T> ArrayList<T> append(ArrayList<T> a1, ArrayList<T> a2) {
    for (T t : a2) {
      a1.add(t);
    }
    return a1;
  }

  // prints an ArrayList
  <T> void printList(ArrayList<T> arr) {
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

  <T> void addNoDupes(ArrayList<T> arr, T t) {
    if (!arr.contains(t)) {
      arr.add(t);
    }
  }

  ArrayList<String> collectNodes(ArrayList<Edge> arr) {
    ArrayList<String> nodes = new ArrayList<>();
    for (Edge e : arr) {
      this.addNoDupes(nodes, e.from);
      this.addNoDupes(nodes, e.to);
    }
    return this.quicksort(nodes, new StringComparator());
  }
}
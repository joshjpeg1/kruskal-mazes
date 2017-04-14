import java.util.ArrayList;


public class Queue<T> {
  ArrayList<T> items;

  Queue(ArrayList<T> items) {
    this.items = items;
  }

  Queue() {
    this.items = new ArrayList<T>();
  }


  public T pop() {
    return items.remove(0);
  }

  public T peek() {
    return items.get(0);
  }

  public T push(T t) {
    items.add(t);
    return t;
  }

}

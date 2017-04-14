import java.util.ArrayList;

// an ArrayList representing a queue structure
public class Queue<T> {
  ArrayList<T> items;

  // Constructor
  Queue(ArrayList<T> items) {
    this.items = items;
  }

  // Empty Constructor
  Queue() {
    this.items = new ArrayList<T>();
  }

  // returns the first (front) item in a Queue and removes it from the Queue
  public T pop() {
    if (this.empty()) {
      throw new NullPointerException("Queue is Empty! No Popping!");
    } else {
      return this.items.remove(0);
    }
  }

  // returns the first (front) item in a Queue
  public T peek() {
    if (this.empty()) {
      throw new NullPointerException("Queue is Empty! No Peeking!");
    } else {
      return this.items.get(0);
    }
  }

  // adds the given item to the end of the queue
  public T push(T t) {
      this.items.add(t);
      return t;
  }

  // returns whether or not this Queue is empty
  public boolean empty() {
    return this.items.size() == 0;
  }
}

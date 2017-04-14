import java.util.ArrayList;

// represents a Stack
public class Stack<T> implements IDeque<T> {
  ArrayList<T> items;

  // constructor
  Stack(ArrayList<T> items) {
    this.items = items;
  }

  // empty constructor
  Stack() {
    this.items = new ArrayList<>();
  }

  // returns the first element, or throws an error if the stack is empty
  public T peek() {
    if (this.empty()) {
      throw new NullPointerException("No items in list to peek at.");
    } else {
      return items.get(0);
    }
  }

  // returns and removes the first element, or throws an error if the stack is empty
  public T pop() {
    if (this.empty()) {
      throw new NullPointerException("No items in list to push.");
    } else {
      return items.remove(0);
    }
  }

  // adds the given element to the front of the list
  public T push(T t) {
    items.add(0, t);
    return t;
  }

  // determines if the stack is empty
  public boolean empty() {
    return items.size() == 0;
  }
}

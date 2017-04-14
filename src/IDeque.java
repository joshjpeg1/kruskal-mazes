// represents a Deque
public interface IDeque<T> {
  // returns the element at the head of the Deque
  T peek();

  // returns and removes the element at the head of the Deque
  T pop();

  // adds the given element to the head of the Deque
  T push(T t);

  // returns true if the Deque is empty
  boolean empty();
}

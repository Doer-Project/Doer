package datastructures;

///  This stack is mainly use in the message(notification)
public class CustomStack<T> {
    private T[] stackArray;
    private int top;
    private final int capacity;

    // Default constructor with limit 50
    public CustomStack() {
        this.capacity = 50;               // default limit
        stackArray = (T[]) new Object[capacity];
        top = -1;
    }

    // Push element onto stack
    public void push(T item) {
        if (top == capacity - 1) {
            throw new RuntimeException("Stack overflow: max limit " + capacity);
        }
        stackArray[++top] = item;
    }

    // Pop element from stack
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }
        T item = stackArray[top];
        stackArray[top--] = null; // clear reference
        return item;
    }

    // Check if stack is empty
    public boolean isEmpty() {
        return top == -1;
    }

    // Current size of stack
    public int size() {
        return top + 1;
    }

    // Display all elements from top to bottom
    public void display() {
        System.out.println("Stack elements (top to bottom):");
        for (int i = top; i >= 0; i--) {
            System.out.println(stackArray[i]);
        }
    }
}

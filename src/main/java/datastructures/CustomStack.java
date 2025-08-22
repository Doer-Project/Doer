package datastructures;

///  This stack is mainly use in the message(notification)
public class CustomStack<T> {
    private T[] stackArray;
    private int top;
    private final int capacity;

    public CustomStack() {
        this.capacity = 50;
        stackArray = (T[]) new Object[capacity];
        top = -1;
    }

    public void push(T item) {
        if (top == capacity - 1) {
            throw new RuntimeException("Stack overflow: max limit " + capacity);
        }
        stackArray[++top] = item;
    }

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }
        T item = stackArray[top];
        stackArray[top--] = null;
        return item;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }

    public void display() {
        System.out.println("Stack elements (top to bottom):");
        for (int i = top; i >= 0; i--) {
            System.out.println(stackArray[i]);
        }
    }
}

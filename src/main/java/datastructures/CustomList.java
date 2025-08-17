package datastructures;

import java.util.Arrays;

public class CustomList<T> {
    private Object[] data;
    private int size;

    // Constructor
    public CustomList() {
        data = new Object[10]; // default capacity
        size = 0;
    }

    // Add element
    public void add(T element) {
        ensureCapacity();
        data[size++] = element;
    }

    // Get element
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return (T) data[index];
    }

    // Remove element
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
        return removed;
    }

    // Size
    public int size() {
        return size;
    }

    // Empty check
    public boolean isEmpty() {
        return size == 0;
    }

    // Ensure capacity
    private void ensureCapacity() {
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
    }

    // ToString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

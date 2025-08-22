package datastructures;

import java.util.Iterator;

/// This is Custom LinkedList which we are using as a Data Structure.
public class CustomList<T> implements Iterable<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int size = 0;

    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        size++;
    }


    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.data;
    }

    public CustomList<T> subList(int from, int to) {
        if (from < 0 || to > size || from > to) {
            throw new IndexOutOfBoundsException("Invalid subList range: " + from + " to " + to);
        }

        CustomList<T> result = new CustomList<>();
        int index = 0;
        for (T item : this) {
            if (index >= from && index < to) {
                result.add(item);
            }
            index++;
            if (index >= to) break;
        }
        return result;
    }


    public void display() {
        if (isEmpty()) {
            System.out.println("List is empty");
            return;
        }
        Node<T> temp = head;
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    public void remove(T value) {
        if (head == null) return;

        if (head.data.equals(value)) {
            head = head.next;
            size--;
            return;
        }

        Node<T> temp = head;
        while (temp.next != null && !temp.next.data.equals(value)) {
            temp = temp.next;
        }

        if (temp.next != null) {
            temp.next = temp.next.next;
            size--;
        }
    }

    // Iterator for foreach loops
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T val = current.data;
                current = current.next;
                return val;
            }
        };
    }
}

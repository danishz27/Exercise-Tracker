package ca.umanitoba.cs.zaheerd.stack;

import ca.umanitoba.cs.comp2450.stack.Stack;
import com.google.common.base.Preconditions;

public class LinkedStack<T> implements Stack<T> {

    private static class Node<T> {
        private final T data;
        private Node<T> next;

        private Node(T data) {
            Preconditions.checkNotNull(data, "data cannot be null");
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> top;
    private int size;

    public LinkedStack() {
        this.top = null;
        this.size = 0;
        checkLinkedStack();
    }
    
    public void push(T item) {
        Preconditions.checkNotNull(item, "item cannot be null");
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size++;
        checkLinkedStack();
    }
    
    public T pop() throws Stack.EmptyStackException {
        if (isEmpty()) {
            throw new Stack.EmptyStackException("Cannot pop an empty stack.");
        }
        T data = top.data;
        top = top.next;
        size--;
        checkLinkedStack();
        return data;
    }
    
    public T peek() throws Stack.EmptyStackException {
        if (isEmpty()) {
            throw new Stack.EmptyStackException("Cannot peek an empty stack.");
        }
        return top.data;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }

    private void checkLinkedStack() {
        Preconditions.checkState(size >= 0, "size must be non-negative");
        if (size == 0) {
            Preconditions.checkState(top == null, "top must be null when size is 0");
        } else {
            Preconditions.checkState(top != null, "top must not be null when size is greater than 0");
        }
    }
}
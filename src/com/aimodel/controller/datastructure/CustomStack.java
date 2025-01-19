
package com.aimodel.controller.datastructure;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * A custom stack implementation with  LIFO (Last-In-First-Out) behavior.
 * New items are always added at the beginning (index 0) of the internal list.
 *
 * @param <T> the type of elements maintained by this stack
 * @author Aaviskar 23048648
 */
public class CustomStack<T> {
    private final List<T> stack;
    private final int maxSize;

    /**
     * Creates a new CustomStack with the specified maximum size.
     *
     * @param maxSize the maximum number of elements the stack can hold
     */
    public CustomStack(int maxSize) {
        this.stack = new ArrayList<>();
        this.maxSize = maxSize;
    }

    /**
     * Creates a new CustomStack with default maximum size of 10.
     */
    public CustomStack() {
        this(10);
    }

    /**
     * Pushes an item onto the top of this stack (at index 0).
     * If the stack is at maximum capacity, removes the oldest item before adding the new one.
     *
     * @param item the item to be pushed onto this stack
     */
    public void push(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot push null item onto stack");
        }
        
        if (stack.size() >= maxSize) {
            stack.remove(stack.size() - 1); // Remove oldest item (from the end)
        }
        stack.add(0, item); // Add new item at the beginning (true LIFO behavior)
    }

    /**
     * Removes and returns the most recently added item (from index 0).
     *
     * @return The most recently added item
     * @throws EmptyStackException if this stack is empty
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.remove(0); // Remove and return the most recent item
    }

    /**
     * Returns all items in stack order (newest to oldest).
     * No need to reverse since we're already storing in LIFO order.
     *
     * @return List of items in chronological order (newest first)
     */
    public List<T> getRecentItems() {
        return new ArrayList<>(stack); // Already in correct order
    }

    /**
     * Looks at the most recently added item without removing it.
     *
     * @return the most recently added item
     * @throws EmptyStackException if this stack is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.get(0); // Get most recent item
    }

    /**
     * Tests if this stack is empty.
     *
     * @return true if and only if this stack contains no items; false otherwise
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Returns the number of items in this stack.
     *
     * @return the number of items in this stack
     */
    public int size() {
        return stack.size();
    }

    /**
     * Returns the maximum size of this stack.
     *
     * @return the maximum number of items this stack can hold
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Removes all items from this stack.
     */
    public void clear() {
        stack.clear();
    }

    /**
     * Returns a string representation of this stack.
     *
     * @return a string representation of this stack
     */
    @Override
    public String toString() {
        return "CustomStack{" +
                "items=" + stack +
                ", size=" + size() +
                ", maxSize=" + maxSize +
                '}';
    }
}





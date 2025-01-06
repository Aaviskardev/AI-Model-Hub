package com.aimodel.util; 

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
/**
 *
 * @author Aaviskar  23048648
 */

public class CustomStack<T> {

    private List<T> stack;

    public CustomStack() {
        stack = new ArrayList<>();
    }

    /**
     * Pushes an item onto the top of this stack.
     * @param item the item to be pushed onto this stack.
     */
    public void push(T item) {
        stack.add(item);
    }

    /**
     * Removes the object at the top of this stack and returns that object.
     * @return The object at the top of this stack.
     * @throws EmptyStackException if this stack is empty.
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.remove(stack.size() - 1);
    }

    /**
     * Looks at the object at the top of this stack without removing it.
     * @return the object at the top of this stack.
     * @throws EmptyStackException if this stack is empty.
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.get(stack.size() - 1);
    }

    /**
     * Tests if this stack is empty.
     * @return true if and only if this stack contains no items; false otherwise.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Returns the number of items in this stack.
     * @return the number of items in this stack.
     */
    public int size() {
        return stack.size();
    }

    /**
     * Removes all of the elements from this stack.
     */
    public void clear() {
        stack.clear();
    }

    /**
     * Returns a string representation of this stack.
     * @return a string representation of this stack.
     */
    @Override
    public String toString() {
        return stack.toString();
    }
}
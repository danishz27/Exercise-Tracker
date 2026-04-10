package ca.umanitoba.cs.zaheerd.model.exceptions;

public class InvalidIndexException extends Exception {
    public InvalidIndexException(int index, int size) {
        super("Invalid selection '" + index + "'. Please enter a number between 0 and " + (size - 1) + ".");
    }
}
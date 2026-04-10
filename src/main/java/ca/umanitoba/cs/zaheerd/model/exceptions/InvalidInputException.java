package ca.umanitoba.cs.zaheerd.model.exceptions;

public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super("Invalid selection '" + message + "'.");}
}


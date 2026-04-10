package ca.umanitoba.cs.zaheerd.model.exceptions;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super("Incorrect password. Please try again.");
    }
}

package ca.umanitoba.cs.zaheerd.model.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String username) {
        super("Account '" + username + "' not found. Please check your username and try again.");
    }
}
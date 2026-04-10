package ca.umanitoba.cs.zaheerd.model.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String username) {
        super("Account '" + username + "' already exists. Please choose a different username.");
    }
}

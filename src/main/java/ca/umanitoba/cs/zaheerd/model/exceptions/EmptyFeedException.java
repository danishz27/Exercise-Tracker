package ca.umanitoba.cs.zaheerd.model.exceptions;

public class EmptyFeedException extends Exception {
    public EmptyFeedException() {
        super("No activities in feed. Add activities or follow users to see their activities.");
    }
}

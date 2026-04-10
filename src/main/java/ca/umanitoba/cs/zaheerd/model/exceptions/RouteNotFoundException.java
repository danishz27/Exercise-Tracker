package ca.umanitoba.cs.zaheerd.model.exceptions;

public class RouteNotFoundException extends Exception {
    public RouteNotFoundException(String start, String end) {
        super("No route found from " + start + " to " + end +
                ". Try adding more activities to expand the available routes.");
    }
}


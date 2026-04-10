package ca.umanitoba.cs.zaheerd.ui;

import ca.umanitoba.cs.zaheerd.model.exceptions.*;
import ca.umanitoba.cs.zaheerd.output.*;
import ca.umanitoba.cs.zaheerd.logic.PathFinder;
import ca.umanitoba.cs.zaheerd.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RouteDisplay {
    private final Tracker tracker;
    private final User user;
    private final Scanner scanner;

    public RouteDisplay(Tracker tracker, User user, Scanner scanner) {
        this.tracker = tracker;
        this.user = user;
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    findPersonalRoute();
                    break;
                case "2":
                    findFeedRoute();
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Route Finder Menu ---");
        System.out.println("1. Find Route (Personal Routes Only)");
        System.out.println("2. Find Route (Feed Routes)");
        System.out.println("3. Back");
        System.out.print("Enter option: ");
    }

    private void findPersonalRoute() {
        try {
            Point[] points = getStartAndEnd();
            if (points == null) return;

            PathFinder pathFinder = new PathFinder(tracker.getWorldMap());
            Path route = pathFinder.findPersonalRoute(points[0], points[1], user);

            if (route == null) {
                throw new RouteNotFoundException(points[0].toString(), points[1].toString());
            }

            displayRoute(route);

        } catch (RouteNotFoundException | InvalidInputException | InvalidCoordinateException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void findFeedRoute() {
        try {
            Point[] points = getStartAndEnd();
            if (points == null) return;

            List<Exercise> allExercises = new ArrayList<>();
            for (User u : tracker.getUsers()) {
                if (!u.equals(user)) {
                    allExercises.addAll(u.getExercises());
                }
            }

            PathFinder pathFinder = new PathFinder(tracker.getWorldMap());
            Path route = pathFinder.findFeedRoute(points[0], points[1], allExercises);

            if (route == null) {
                throw new RouteNotFoundException(points[0].toString(), points[1].toString());
            }

            displayRoute(route);

        } catch (RouteNotFoundException | InvalidInputException | InvalidCoordinateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Point[] getStartAndEnd() throws InvalidInputException, InvalidCoordinateException {
        Map map = tracker.getWorldMap();

        System.out.print("Enter start point (x y): ");
        Point start = readPoint(map);

        System.out.print("Enter end point (x y): ");
        Point end = readPoint(map);

        return new Point[]{start, end};

    }

    private Point readPoint(Map map) throws InvalidCoordinateException {
        try {
            String line = scanner.nextLine().trim();
            String[] parts = line.split(" ");

            if (parts.length < 2) {
                throw new InvalidCoordinateException(
                        "Invalid format. Please enter x and y separated by a space (e.g., '3 4').");
            }

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            if (!map.isWithinBounds(x, y)) {
                throw new InvalidCoordinateException(
                        "Coordinates (" + x + ", " + y + ") are out of bounds. " +
                                "Valid range is 0-" + (map.getWidth() - 1) + " for x and 0-" +
                                (map.getHeight() - 1) + " for y.");
            }

            return new Point(x, y);

        } catch (NumberFormatException | InvalidCoordinateException e) {
            throw new InvalidCoordinateException(
                    "Invalid coordinate. Please enter valid integers for x and y.");
        }
    }

    private void displayRoute(Path route) {
        System.out.println("\n--- Route Found! ---");
        System.out.println(MapPrinter.getLegend());
        System.out.print(MapPrinter.formatGrid(
                MapPrinter.makeGridWithFoundRoute(tracker.getWorldMap(), route)));
    }
}
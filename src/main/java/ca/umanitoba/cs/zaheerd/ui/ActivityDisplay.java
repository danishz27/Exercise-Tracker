package ca.umanitoba.cs.zaheerd.ui;

import ca.umanitoba.cs.zaheerd.model.exceptions.*;
import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.output.Formatter;
import ca.umanitoba.cs.zaheerd.persistence.PersistenceException;
import ca.umanitoba.cs.zaheerd.persistence.TrackerPersistence;

import java.util.List;
import java.util.Scanner;

public class ActivityDisplay {

    private final Tracker tracker;
    private final User user;
    private final Scanner scanner;
    private final TrackerPersistence persistence;

    public ActivityDisplay(Tracker tracker, User user, Scanner scanner, TrackerPersistence persistence) {
        this.tracker = tracker;
        this.user = user;
        this.scanner = scanner;
        this.persistence = persistence;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    addActivity();
                    break;
                case "2":
                    removeActivity();
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
        System.out.println("\n--- Activity Menu ---");
        System.out.println("1. Add Activity");
        System.out.println("2. Remove Activity");
        System.out.println("3. Back");
        System.out.print("Enter option: ");
    }

    private void addActivity() {
        try {
            System.out.print("Enter activity name: ");
            String name = scanner.nextLine().trim();
            validateInput(name, "Activity name");

            ExerciseType type = selectExerciseType();
            if (type == null) return;

            System.out.print("Enter distance (km): ");
            double distance = readDouble();

            System.out.print("Enter duration (minutes): ");
            int duration = readInt();

            if (duration <= 0) {
                throw new InvalidInputException("Duration must be positive. Please enter a number greater than 0.");
            }

            if (distance < 0) {
                throw new InvalidInputException("Distance must be non-negative. Please enter a valid distance.");
            }

            Path path = selectRoute(name);
            if (path == null) return;

            Map map = tracker.getWorldMap();
            if (!path.isWithinBounds(map.getWidth(), map.getHeight())) {
                throw new InvalidCoordinateException("Path is out of map bounds (0-" +
                        (map.getWidth() - 1) + ", 0-" + (map.getHeight() - 1) + "). Please re-enter waypoints.");
            }

            map.addPath(path);

            Exercise exercise = new Exercise.Builder()
                    .name(name)
                    .type(type)
                    .distance(distance)
                    .duration(duration)
                    .exercisePath(path)
                    .build();

            user.addExercise(exercise);
            savePersistence();

            System.out.println("Activity '" + name + "' added successfully!");

        } catch (InvalidInputException | InvalidCoordinateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private ExerciseType selectExerciseType() {
        System.out.println("Exercise types: 1. RUNNING  2. CYCLING");
        System.out.print("Enter exercise type: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": return ExerciseType.RUNNING;
            case "2": return ExerciseType.CYCLING;
            default:
                System.out.println("Invalid exercise type. Please enter 1 or 2.");
                return null;
        }
    }

    private Path selectRoute(String activityName) {
        System.out.println("\n--- Route Options ---");
        System.out.println("1. Enter new route");
        System.out.println("2. Duplicate previous route");
        System.out.print("Enter option: ");
        String option = scanner.nextLine().trim();

        switch (option) {
            case "1": return enterNewRoute(activityName);
            case "2": return duplicateRoute(activityName);
            default:
                System.out.println("Invalid option. Please enter 1 or 2.");
                return null;
        }
    }

    private Path enterNewRoute(String activityName) {
        try {
            Path path = new Path(activityName + "_route");

            System.out.print("Enter number of waypoints (minimum 2): ");
            int numWaypoints = readInt();

            if (numWaypoints < 2) {
                throw new InvalidInputException("Path must have at least 2 waypoints. Please enter 2 or more.");
            }

            for (int i = 0; i < numWaypoints; i++) {
                System.out.print("Enter waypoint " + (i + 1) + " (x y): ");
                String line = scanner.nextLine().trim();
                String[] parts = line.split(" ");

                if (parts.length < 2) {
                    throw new InvalidCoordinateException("Invalid format. Please enter x and y separated by a space (e.g., '3 4').");
                }

                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                if (x < 0 || y < 0) {
                    throw new InvalidCoordinateException("Coordinates must be non-negative. Please enter valid coordinates.");
                }

                path.addWaypoint(new Point(x, y));
            }

            if (!path.isValid()) {
                throw new InvalidInputException("Path must have at least 2 waypoints.");
            }

            return path;

        } catch (InvalidInputException | InvalidCoordinateException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid coordinate. Please enter valid integers for x and y.");
            return null;
        }
    }

    private Path duplicateRoute(String activityName) {
        try {
            List<Exercise> exercises = user.getExercises();
            if (exercises.isEmpty()) {
                System.out.println("No previous activities to duplicate. Please enter a new route.");
                return null;
            }

            System.out.println("\n--- Your Previous Activities ---");
            Formatter.printExerciseList(exercises);

            System.out.print("Enter activity number to duplicate: ");
            int index = readInt();

            if (index < 0 || index >= exercises.size()) {
                throw new InvalidIndexException(index, exercises.size());
            }

            Path original = exercises.get(index).getExercisePath();
            Path duplicate = new Path(activityName + "_route");
            for (Point waypoint : original.getWaypoints()) {
                duplicate.addWaypoint(waypoint);
            }

            System.out.println("Route duplicated successfully!");
            return duplicate;

        } catch (InvalidIndexException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private void removeActivity() {
        try {
            List<Exercise> exercises = user.getExercises();
            if (exercises.isEmpty()) {
                System.out.println("No activities to remove.");
                return;
            }

            System.out.println("\n--- Your Activities ---");
            Formatter.printExerciseList(exercises);

            System.out.print("Enter activity number to remove (or -1 to cancel): ");
            int index = readInt();

            if (index == -1) {
                System.out.println("Removal cancelled.");
                return;
            }

            if (index < 0 || index >= exercises.size()) {
                throw new InvalidIndexException(index, exercises.size());
            }

            Exercise exercise = exercises.get(index);
            tracker.getWorldMap().removePath(exercise.getExercisePath());
            user.removeExercise(index);
            System.out.println("Activity '" + exercise.getName() + "' removed successfully.");

        } catch (InvalidIndexException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void savePersistence() {
        try {
            persistence.saveTracker(tracker);
        } catch (PersistenceException e) {
            System.out.println("Warning: could not save data (" + e.getMessage() + ").");
        }
    }

    private void validateInput(String input, String fieldName) throws InvalidInputException {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty. Please try again.");
        }
    }

    private int readInt() throws InvalidInputException {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input. Please enter a whole number.");
        }
    }

    private double readDouble() throws InvalidInputException {
        try {
            String line = scanner.nextLine().trim();
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input. Please enter a valid number.");
        }
    }
}
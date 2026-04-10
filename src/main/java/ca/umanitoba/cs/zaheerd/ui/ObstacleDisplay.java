package ca.umanitoba.cs.zaheerd.ui;

import ca.umanitoba.cs.zaheerd.model.exceptions.*;
import ca.umanitoba.cs.zaheerd.output.*;
import ca.umanitoba.cs.zaheerd.model.*;

import java.util.Scanner;

public class ObstacleDisplay {
    private final Tracker tracker;
    private final Scanner scanner;

    public ObstacleDisplay(Tracker tracker, Scanner scanner) {
        this.tracker = tracker;
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    addObstacle();
                    break;
                case "2":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1 or 2.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Obstacle Menu ---");
        System.out.println("1. Add Obstacle");
        System.out.println("2. Back");
        System.out.print("Enter option: ");
    }

    private void addObstacle() {
        try {
            ObstacleType type = selectObstacleType();
            if (type == null) return;

            Map map = tracker.getWorldMap();

            System.out.print("Enter obstacle coordinates (x y): ");
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

            Obstacles obstacle = new Obstacles(type, new Point(x, y));
            map.addObstacle(obstacle);
            System.out.println("Obstacle added: " + type + " at (" + x + ", " + y + ")");

        } catch (InvalidCoordinateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid coordinate. Please enter valid integers for x and y.");
        }
    }


    private ObstacleType selectObstacleType() {
        System.out.println("Obstacle types: 1. RIVER  2. TREE  3. ROCK");
        System.out.print("Enter obstacle type: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                return ObstacleType.RIVER;
            case "2":
                return ObstacleType.TREE;
            case "3":
                return ObstacleType.ROCK;
            default:
                System.out.println("Invalid obstacle type. Please enter 1, 2, or 3.");
                return null;
        }
    }

}
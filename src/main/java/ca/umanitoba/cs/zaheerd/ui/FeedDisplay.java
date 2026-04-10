package ca.umanitoba.cs.zaheerd.ui;

import ca.umanitoba.cs.zaheerd.model.exceptions.*;
import ca.umanitoba.cs.zaheerd.output.*;
import ca.umanitoba.cs.zaheerd.model.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class FeedDisplay {
    private final Tracker tracker;
    private final User user;
    private final Scanner scanner;

    public FeedDisplay(Tracker tracker, User user, Scanner scanner) {
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
                    showFeed();
                    break;
                case "2":
                    showOwnActivities();
                    break;
                case "3":
                    showFollowedActivities();
                    break;
                case "4":
                    viewActivity();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 5.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Feed Menu ---");
        System.out.println("1. View Full Feed");
        System.out.println("2. Filter by Own Activities");
        System.out.println("3. Filter by Followed Users");
        System.out.println("4. View Activity on Map");
        System.out.println("5. Back");
        System.out.print("Enter option: ");
    }

    private void showFeed() {
        List<Exercise> feed = new ArrayList<>();
        for (User u : tracker.getUsers()) {
            feed.addAll(u.getExercises());
        }

        if (feed.isEmpty()) {
            System.out.println("No activities in feed. Add activities to see them here.");
        } else {
            System.out.println("\n--- Feed ---");
            int index = 0;
            for (User u : tracker.getUsers()) {
                for (Exercise exercise : u.getExercises()) {
                    System.out.println(Formatter.formatFeedEntry(index, exercise, u.getUsername()));
                    index++;
                }
            }
        }
    }

    private void showOwnActivities() {
        List<Exercise> exercises = user.getExercises();
        if (exercises.isEmpty()) {
            System.out.println("You have no activities yet. Add an activity to get started.");
        } else {
            System.out.println("\n--- Your Activities ---");
            Formatter.printExerciseList(exercises);
        }
    }

    private void showFollowedActivities() {
        List<User> following = user.getFollowing();
        if (following.isEmpty()) {
            System.out.println("You are not following anyone. Follow users to see their activities.");
        } else {
            System.out.println("\n--- Followed Users' Activities ---");
            boolean hasActivities = false;
            for (User followed : following) {
                List<Exercise> exercises = followed.getExercises();
                if (!exercises.isEmpty()) {
                    hasActivities = true;
                    System.out.println("\n" + followed.getUsername() + "'s activities:");
                    Formatter.printExerciseList(exercises);
                }
            }
            if (!hasActivities) {
                System.out.println("No activities from followed users yet.");
            }
        }
    }

    private void viewActivity() {
        try {
            List<Exercise> feed = new ArrayList<>();
            List<String> usernames = new ArrayList<>();

            for (User u : tracker.getUsers()) {
                for (Exercise exercise : u.getExercises()) {
                    feed.add(exercise);
                    usernames.add(u.getUsername());
                }
            }

            if (feed.isEmpty()) {
                throw new EmptyFeedException();
            }

            System.out.println("\n--- Select Activity to View ---");
            for (int i = 0; i < feed.size(); i++) {
                System.out.println(Formatter.formatFeedEntry(i, feed.get(i), usernames.get(i)));
            }

            System.out.print("Enter activity number: ");
            int index = readInt();

            if (index < 0 || index >= feed.size()) {
                throw new InvalidIndexException(index, feed.size());
            }

            Exercise exercise = feed.get(index);
            Map map = tracker.getWorldMap();

            System.out.println("\n--- Activity: " + exercise.getName() + " ---");
            System.out.println("By: " + usernames.get(index));
            System.out.println("Type: " + exercise.getType());
            System.out.printf("Distance: %.2f km%n", exercise.getDistance());
            System.out.printf("Duration: %d min%n", exercise.getDuration());
            System.out.println();
            System.out.println(MapPrinter.getLegend());
            System.out.print(MapPrinter.formatGrid(
                    MapPrinter.makeGridWithPath(map, exercise.getExercisePath())));

        } catch (EmptyFeedException | InvalidIndexException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private int readInt() throws InvalidInputException {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input. Please enter a number.");
        }
    }
}
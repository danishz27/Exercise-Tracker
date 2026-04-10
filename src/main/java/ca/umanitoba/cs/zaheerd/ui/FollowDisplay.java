package ca.umanitoba.cs.zaheerd.ui;

import ca.umanitoba.cs.zaheerd.model.exceptions.*;
import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.output.Formatter;
import ca.umanitoba.cs.zaheerd.persistence.PersistenceException;
import ca.umanitoba.cs.zaheerd.persistence.TrackerPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FollowDisplay {

    private final Tracker tracker;
    private final User user;
    private final Scanner scanner;
    private final TrackerPersistence persistence;

    public FollowDisplay(Tracker tracker, User user, Scanner scanner, TrackerPersistence persistence) {
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
                    followUser();
                    break;
                case "2":
                    unfollowUser();
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
        System.out.println("\n--- Follow Menu ---");
        System.out.println("1. Follow User");
        System.out.println("2. Unfollow User");
        System.out.println("3. Back");
        System.out.print("Enter option: ");
    }

    private void followUser() {
        try {
            List<User> availableUsers = getFollowableUsers();

            if (availableUsers.isEmpty()) {
                System.out.println("No users available to follow. " +
                        "There are no other accounts or you are already following everyone.");
                return;
            }

            System.out.println("\n--- Available Users ---");
            Formatter.printUserList(availableUsers);

            System.out.print("Enter user number to follow (or -1 to cancel): ");
            int index = readInt();

            if (index == -1) {
                System.out.println("Cancelled.");
                return;
            }

            if (index < 0 || index >= availableUsers.size()) {
                throw new InvalidIndexException(index, availableUsers.size());
            }

            User toFollow = availableUsers.get(index);
            user.followUser(toFollow);
            savePersistence();

            System.out.println("You are now following " + toFollow.getUsername() + "!");

        } catch (InvalidIndexException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void unfollowUser() {
        try {
            List<User> following = user.getFollowing();

            if (following.isEmpty()) {
                System.out.println("You are not following anyone.");
                return;
            }

            System.out.println("\n--- Users You Follow ---");
            Formatter.printUserList(following);

            System.out.print("Enter user number to unfollow (or -1 to cancel): ");
            int index = readInt();

            if (index == -1) {
                System.out.println("Cancelled.");
                return;
            }

            if (index < 0 || index >= following.size()) {
                throw new InvalidIndexException(index, following.size());
            }

            User toUnfollow = following.get(index);
            user.unfollowUser(toUnfollow);
            System.out.println("You have unfollowed " + toUnfollow.getUsername() + ".");

        } catch (InvalidIndexException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private List<User> getFollowableUsers() {
        List<User> allUsers = tracker.getUsers();
        List<User> followable = new ArrayList<>();
        for (User u : allUsers) {
            if (!u.equals(user) && !user.isFollowing(u)) {
                followable.add(u);
            }
        }
        return followable;
    }

    private void savePersistence() {
        try {
            persistence.saveTracker(tracker);
        } catch (PersistenceException e) {
            System.out.println("Warning: could not save data (" + e.getMessage() + ").");
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
}
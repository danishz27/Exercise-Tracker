package ca.umanitoba.cs.zaheerd.ui;

import ca.umanitoba.cs.zaheerd.model.exceptions.*;
import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.persistence.PersistenceException;
import ca.umanitoba.cs.zaheerd.persistence.TrackerPersistence;

import java.util.Scanner;

public class AccountDisplay {

    private final Tracker tracker;
    private final User user;
    private final Scanner scanner;
    private final TrackerPersistence persistence;

    public AccountDisplay(Tracker tracker, User user, Scanner scanner, TrackerPersistence persistence) {
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
                    updateUsername();
                    break;
                case "2":
                    updatePassword();
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
        System.out.println("\n--- Update Account Menu ---");
        System.out.println("Current username: " + user.getUsername());
        System.out.println("1. Update Username");
        System.out.println("2. Update Password");
        System.out.println("3. Back");
        System.out.print("Enter option: ");
    }

    private void updateUsername() {
        try {
            System.out.print("Enter new username: ");
            String newUsername = scanner.nextLine().trim();
            if (tracker.hasUser(newUsername)) {
                throw new UserAlreadyExistsException(newUsername);
            }
            validateInput(newUsername, "Username");

            user.setUsername(newUsername);
            savePersistence();

            System.out.println("Username updated successfully to '" + newUsername + "'.");

        } catch (UserAlreadyExistsException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updatePassword() {
        try {
            System.out.print("Enter current password: ");
            String currentPassword = scanner.nextLine().trim();

            if (!user.getPassword().equals(currentPassword)) {
                throw new InvalidCredentialsException();
            }

            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine().trim();
            validateInput(newPassword, "Password");

            user.setPassword(newPassword);
            savePersistence();

            System.out.println("Password updated successfully.");

        } catch (InvalidCredentialsException | InvalidInputException e) {
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
}

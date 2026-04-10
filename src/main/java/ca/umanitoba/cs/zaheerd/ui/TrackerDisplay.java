package ca.umanitoba.cs.zaheerd.ui;

import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.model.exceptions.*;
import ca.umanitoba.cs.zaheerd.persistence.NotFoundException;
import ca.umanitoba.cs.zaheerd.persistence.PersistenceException;
import ca.umanitoba.cs.zaheerd.persistence.TrackerPersistence;

import java.util.Scanner;

public class TrackerDisplay {

    private final Tracker tracker;
    private final Scanner scanner;
    private final TrackerPersistence persistence;

    public TrackerDisplay(TrackerPersistence persistence) {
        this.persistence = persistence;
        this.scanner = new Scanner(System.in);

        Tracker loaded = null;
        try {
            loaded = persistence.loadTracker();
            System.out.println("Previous session restored.");
        } catch (NotFoundException e) {
        } catch (PersistenceException e) {
            System.out.println("Warning: could not load saved data (" + e.getMessage() + "). Starting fresh.");
        }
        if (loaded != null) {
            this.tracker = loaded;
        } else {
            this.tracker = new Tracker();
        }
    }

    public void run() {
        System.out.println("--------------   EXERCISE TRACKER   --------------");

        boolean running = true;
        while (running) {
            printMenu();
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    handleSignIn();
                    break;
                case "2":
                    handleCreateAccount();
                    break;
                case "3":
                    System.out.println("Thank you for using this Exercise Tracker!!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n--- Sign In Menu ---");
        System.out.println("1. Sign In");
        System.out.println("2. Create Account");
        System.out.println("3. Exit");
        System.out.print("Enter option: ");
    }

    private void handleSignIn() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            validateInput(username, "Username");
            validateInput(password, "Password");

            User user = tracker.getUser(username);

            if (user == null) {
                throw new UserNotFoundException(username);
            }

            if (!user.getPassword().equals(password)) {
                throw new InvalidCredentialsException();
            }

            System.out.println("Welcome back, " + username + "!");
            MainMenuDisplay mainMenu = new MainMenuDisplay(tracker, user, scanner, persistence);
            mainMenu.run();

        } catch (UserNotFoundException | InvalidCredentialsException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleCreateAccount() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            validateInput(username, "Username");
            validateInput(password, "Password");

            if (tracker.hasUser(username)) {
                throw new UserAlreadyExistsException(username);
            }

            User user = new User.Builder()
                    .username(username)
                    .password(password)
                    .build();

            tracker.addUser(user);
            savePersistence();

            System.out.println("Account created successfully! Please sign in.");

        } catch (UserAlreadyExistsException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void savePersistence() {
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
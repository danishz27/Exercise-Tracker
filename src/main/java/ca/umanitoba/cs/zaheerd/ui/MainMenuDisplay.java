package ca.umanitoba.cs.zaheerd.ui;

import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.persistence.TrackerPersistence;

import java.util.Scanner;

public class MainMenuDisplay {

    private final Tracker tracker;
    private final User user;
    private final Scanner scanner;
    private final TrackerPersistence persistence;

    public MainMenuDisplay(Tracker tracker, User user, Scanner scanner, TrackerPersistence persistence) {
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
                    new FeedDisplay(tracker, user, scanner).run();
                    break;
                case "2":
                    new ActivityDisplay(tracker, user, scanner, persistence).run();
                    break;
                case "3":
                    new RouteDisplay(tracker, user, scanner).run();
                    break;
                case "4":
                    new ObstacleDisplay(tracker, scanner).run();
                    break;
                case "5":
                    new FollowDisplay(tracker, user, scanner, persistence).run();
                    break;
                case "6":
                    new AccountDisplay(tracker, user, scanner, persistence).run();
                    break;
                case "7":
                    System.out.println("Signing out...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 7.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Main Menu --- Welcome, " + user.getUsername() + "!");
        System.out.println("1. View Feed");
        System.out.println("2. Add/Remove Activity");
        System.out.println("3. Find New Route");
        System.out.println("4. Add Obstacle");
        System.out.println("5. Follow/Unfollow User");
        System.out.println("6. Update Account");
        System.out.println("7. Sign Out");
        System.out.print("Enter option: ");
    }
}
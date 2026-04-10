package ca.umanitoba.cs.zaheerd.output;

import ca.umanitoba.cs.zaheerd.model.*;
import com.google.common.base.Preconditions;
import java.util.List;

public class Formatter {

    public static String formatExerciseWithIndex(int index, Exercise exercise) {
        Preconditions.checkNotNull(exercise, "exercise cannot be null");
        return String.format("[%d] %s (%s) - %.2f km, %d min",
                index, exercise.getName(), exercise.getType(),
                exercise.getDistance(), exercise.getDuration());
    }

    public static String formatUser(int index, User user) {
        Preconditions.checkNotNull(user, "user cannot be null");
        return String.format("[%d] %s", index, user.getUsername());
    }

    public static String formatFeedEntry(int index, Exercise exercise, String username) {
        Preconditions.checkNotNull(exercise, "exercise cannot be null");
        Preconditions.checkNotNull(username, "username cannot be null");
        return String.format("[%d] %s by %s (%s) - %.2f km, %d min",
                index, exercise.getName(), username, exercise.getType(),
                exercise.getDistance(), exercise.getDuration());
    }

    public static void printExerciseList(List<Exercise> exercises) {
        Preconditions.checkNotNull(exercises, "exercises cannot be null");
        if (exercises.isEmpty()) {
            System.out.println("No activities found.");
        } else {
            for (int i = 0; i < exercises.size(); i++) {
                System.out.println(formatExerciseWithIndex(i, exercises.get(i)));
            }
        }
    }


    public static void printUserList(List<User> users) {
        Preconditions.checkNotNull(users, "users cannot be null");
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (int i = 0; i < users.size(); i++) {
                System.out.println(formatUser(i, users.get(i)));
            }
        }
    }
}

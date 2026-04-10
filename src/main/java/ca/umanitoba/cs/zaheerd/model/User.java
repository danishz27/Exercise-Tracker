package ca.umanitoba.cs.zaheerd.model;

import com.google.common.base.Preconditions;
import java.util.*;


public class User {
    private String username;
    private String password;
    private final List<Exercise> exercises;
    private final List<User> following;

    public User(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.exercises = new ArrayList<>();
        this.following = new ArrayList<>();
        checkUser();
    }


    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setUsername(String username) {
        Preconditions.checkNotNull(username, "username cannot be null");
        Preconditions.checkState(!username.trim().isEmpty(), "username cannot be empty");
        this.username = username;
        checkUser();
    }

    public void setPassword(String password) {
        Preconditions.checkNotNull(password, "password cannot be null");
        Preconditions.checkState(!password.trim().isEmpty(), "password cannot be empty");
        this.password = password;
        checkUser();
    }

    public void addExercise(Exercise exercise) {
        Preconditions.checkNotNull(exercise, "exercise cannot be null");
        exercises.add(exercise);
        checkUser();
    }

    public void removeExercise(int index) {
        Preconditions.checkState(index >= 0 && index < exercises.size(),
                "invalid index: %s", index);
        exercises.remove(index);
        checkUser();
    }

    public List<Exercise> getExercises() {
        return new ArrayList<>(exercises);
    }

    public void followUser(User user) {
        Preconditions.checkNotNull(user, "user cannot be null");
        Preconditions.checkState(!user.equals(this), "cannot follow yourself");
        Preconditions.checkState(!following.contains(user), "already following this user");
        following.add(user);
        checkUser();
    }

    public void unfollowUser(User user) {
        Preconditions.checkNotNull(user, "user cannot be null");
        Preconditions.checkState(following.contains(user), "not following this user");
        following.remove(user);
        checkUser();
    }

    public List<User> getFollowing() {
        return new ArrayList<>(following);
    }


    public boolean isFollowing(User user) {
        Preconditions.checkNotNull(user, "user cannot be null");
        return following.contains(user);
    }

    private void checkUser() {
        Preconditions.checkNotNull(username, "username must not be null");
        Preconditions.checkState(!username.trim().isEmpty(), "username must not be empty");
        Preconditions.checkNotNull(password, "password must not be null");
        Preconditions.checkState(!password.trim().isEmpty(), "password must not be empty");
        Preconditions.checkNotNull(exercises, "exercises must not be null");
        Preconditions.checkNotNull(following, "following must not be null");
        Preconditions.checkState(!following.contains(this), "following must not contain self");
    }

    public List<Exercise> getFeed() {
        List<Exercise> feed = new ArrayList<>(exercises);
        for (User followed : following) {
            feed.addAll(followed.getExercises());
        }
        return feed;
    }

    public static class Builder {
        private String username;
        private String password;

        public Builder username(String username) {
            Preconditions.checkNotNull(username, "username cannot be null");
            Preconditions.checkState(!username.trim().isEmpty(), "username cannot be empty");
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            Preconditions.checkNotNull(password, "password cannot be null");
            Preconditions.checkState(!password.trim().isEmpty(), "password cannot be empty");
            this.password = password;
            return this;
        }

        public User build() {
            Preconditions.checkNotNull(username, "username must be set");
            Preconditions.checkNotNull(password, "password must be set");
            return new User(this);
        }
    }
}
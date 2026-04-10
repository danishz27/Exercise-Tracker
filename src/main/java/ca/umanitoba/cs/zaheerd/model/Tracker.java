package ca.umanitoba.cs.zaheerd.model;

import com.google.common.base.Preconditions;

import java.util.*;

public class Tracker {
    private final List<User> users;
    private final Map worldMap;

    public Tracker() {
        this.users = new ArrayList<>();
        this.worldMap = new Map();
        checkTracker();
    }

    public void addUser(User user) {
        Preconditions.checkNotNull(user, "user cannot be null");
        users.add(user);
        checkTracker();
    }

    public User getUser(String username) {
        Preconditions.checkNotNull(username, "username cannot be null");
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean hasUser(String username) {
        Preconditions.checkNotNull(username, "username cannot be null");
        return getUser(username) != null;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public Map getWorldMap() {
        return worldMap;
    }

    private void checkTracker() {
        Preconditions.checkNotNull(users, "users must not be null");
        Preconditions.checkNotNull(worldMap, "worldMap must not be null");
    }

}

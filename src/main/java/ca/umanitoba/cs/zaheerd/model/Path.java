package ca.umanitoba.cs.zaheerd.model;

import com.google.common.base.Preconditions;

import java.util.*;


public class Path{
    private final String name;
    private final List<Point> waypoints;

    public Path(String name) {
        Preconditions.checkNotNull(name,"path cannot be null");
        Preconditions.checkState(!name.trim().isEmpty(),"path cannot be empty");

        this.name = name;
        this.waypoints = new ArrayList<>();

        checkPath();
    }

    public String getName() {
        return name;
    }

    public void addWaypoint(Point point) {
        Preconditions.checkNotNull(point, "waypoint cannot be null");
        waypoints.add(point);
        checkPath();
    }

    public List<Point> getWaypoints() {
        return new ArrayList<>(waypoints);
    }

    public boolean isValid() {
        return waypoints.size() >= 2;
    }

    public boolean isWithinBounds(int width, int height) {
        Preconditions.checkState(width > 0,"width must be positive");
        Preconditions.checkState(height > 0,"height must be positive");
        for (Point point : waypoints) {
            if (point.getX() < 0 || point.getX() >= width ||
                    point.getY() < 0 || point.getY() >= height) {
                return false;
            }
        }
        return true;
    }

    private void checkPath() {
        Preconditions.checkState(name != null && !name.trim().isEmpty(),"name must not be null or empty");
        Preconditions.checkNotNull(waypoints,"waypoints must not be null");
    }
}
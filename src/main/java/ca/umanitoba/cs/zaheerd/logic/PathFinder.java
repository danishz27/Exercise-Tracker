package ca.umanitoba.cs.zaheerd.logic;

import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.model.Map;
import com.google.common.base.Preconditions;
import  ca.umanitoba.cs.zaheerd.stack.*;

import java.util.*;

public class PathFinder {

    private final Map map;

    public PathFinder(Map map) {
        Preconditions.checkNotNull(map, "map cannot be null");
        this.map = map;
        checkInvariant();
    }

    public Path findPersonalRoute(Point start, Point end, User user) {
        Preconditions.checkNotNull(start, "start cannot be null");
        Preconditions.checkNotNull(end, "end cannot be null");
        Preconditions.checkNotNull(user, "user cannot be null");

        List<Point> visitable = getVisitablePoints(user.getExercises());
        return findPath(start, end, visitable);
    }

    public Path findFeedRoute(Point start, Point end, List<Exercise> allExercises) {
        Preconditions.checkNotNull(start, "start cannot be null");
        Preconditions.checkNotNull(end, "end cannot be null");
        Preconditions.checkNotNull(allExercises, "allExercises cannot be null");

        List<Point> visitable = getVisitablePoints(allExercises);
        return findPath(start, end, visitable);
    }

    private Path findPath(Point start, Point end, List<Point> visitable) {
        Preconditions.checkNotNull(start, "start cannot be null");
        Preconditions.checkNotNull(end, "end cannot be null");
        Preconditions.checkNotNull(visitable, "visitable cannot be null");

        if (!visitable.contains(start) || !visitable.contains(end)) {
            return null;
        }

        LinkedStack<Point> stack = new LinkedStack<>();
        List<Point> visited = new ArrayList<>();
        List<Point> cameFromKeys = new ArrayList<>();
        List<Point> cameFromValues = new ArrayList<>();

        stack.push(start);

        while (!stack.isEmpty()) {
            Point current = stack.pop();

            if (current.equals(end)) {
                return reconstructPath(start, end, cameFromKeys, cameFromValues);
            }

            if (!visited.contains(current)) {
                visited.add(current);

                for (Point candidate : visitable) {
                    if (!visited.contains(candidate)) {
                        stack.push(candidate);
                        if (!cameFromKeys.contains(candidate)) {
                            cameFromKeys.add(candidate);
                            cameFromValues.add(current);
                        }
                    }
                }
            }
        }

        return null;
    }

    private Path reconstructPath(Point start, Point end,
                                 List<Point> cameFromKeys, List<Point> cameFromValues) {
        Path path = new Path("found_route");
        List<Point> points = new ArrayList<>();

        Point current = end;
        while (!current.equals(start)) {
            points.add(current);
            int index = cameFromKeys.indexOf(current);
            current = cameFromValues.get(index);
        }
        points.add(start);

        Collections.reverse(points);
        for (Point point : points) {
            path.addWaypoint(point);
        }

        return path;
    }

    private List<Point> getVisitablePoints(List<Exercise> exercises) {
        Preconditions.checkNotNull(exercises, "exercises cannot be null");
        List<Point> points = new ArrayList<>();
        for (Exercise exercise : exercises) {
            for (Point waypoint : exercise.getExercisePath().getWaypoints()) {
                if (!map.isObstacle(waypoint.getX(), waypoint.getY())
                        && !points.contains(waypoint)) {
                    points.add(waypoint);
                }
            }
        }
        return points;
    }

    private void checkInvariant() {
        Preconditions.checkNotNull(map, "map must not be null");
    }
}

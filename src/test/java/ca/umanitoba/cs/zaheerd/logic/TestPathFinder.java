package ca.umanitoba.cs.zaheerd.tests.logic;

import ca.umanitoba.cs.zaheerd.logic.PathFinder;
import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.model.Map;
import ca.umanitoba.cs.zaheerd.tests.TestResults;
import ca.umanitoba.cs.zaheerd.tests.TestSuite;

import java.util.ArrayList;
import java.util.List;

public class TestPathFinder implements TestSuite {

    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "PathFinder tests";
    }

    @Override
    public TestResults runTests() {
        testPersonalRouteHappyPath();
        testPersonalRouteNotFoundReturnsNull();
        testFeedRouteHappyPath();
        testFeedRouteEmptyExercisesReturnsNull();
        testStartOnObstacleReturnsNull();
        testRouteContainsStartAndEnd();
        return results;
    }

    private void testPersonalRouteHappyPath() {
        try {
            Map map = new Map();
            User user = buildUser("alice");

            Point p1 = new Point(0, 0);
            Point p2 = new Point(1, 0);
            Point p3 = new Point(2, 0);
            user.addExercise(buildExercise("Run", ExerciseType.RUNNING, p1, p2, p3));

            PathFinder pf = new PathFinder(map);
            Path route = pf.findPersonalRoute(p1, p3, user);

            if (route == null) {
                results.fail("testPersonalRouteHappyPath: expected a route but got null");
            } else if (route.getWaypoints().isEmpty()) {
                results.fail("testPersonalRouteHappyPath: route should have waypoints");
            } else {
                results.pass("testPersonalRouteHappyPath: personal route found successfully");
            }
        } catch (Exception e) {
            results.fail("testPersonalRouteHappyPath: unexpected exception: " + e.getMessage());
        }
    }

    private void testPersonalRouteNotFoundReturnsNull() {
        try {
            Map map = new Map();
            User user = buildUser("alice");

            Point p1 = new Point(0, 0);
            Point p2 = new Point(1, 0);
            user.addExercise(buildExercise("Short Run", ExerciseType.RUNNING, p1, p2));

            PathFinder pf = new PathFinder(map);
            Path route = pf.findPersonalRoute(p1, new Point(5, 5), user);

            if (route != null) {
                results.fail("testPersonalRouteNotFoundReturnsNull: expected null for unreachable end point");
            } else {
                results.pass("testPersonalRouteNotFoundReturnsNull: correctly returned null when no route exists");
            }
        } catch (Exception e) {
            results.fail("testPersonalRouteNotFoundReturnsNull: unexpected exception: " + e.getMessage());
        }
    }

    private void testFeedRouteHappyPath() {
        try {
            Map map = new Map();

            Point p1 = new Point(0, 0);
            Point p2 = new Point(3, 3);
            Point p3 = new Point(6, 6);

            List<Exercise> allExercises = new ArrayList<>();
            allExercises.add(buildExercise("Feed Run", ExerciseType.RUNNING, p1, p2, p3));

            PathFinder pf = new PathFinder(map);
            Path route = pf.findFeedRoute(p1, p3, allExercises);

            if (route == null) {
                results.fail("testFeedRouteHappyPath: expected a feed route but got null");
            } else {
                results.pass("testFeedRouteHappyPath: feed route found successfully");
            }
        } catch (Exception e) {
            results.fail("testFeedRouteHappyPath: unexpected exception: " + e.getMessage());
        }
    }

    private void testFeedRouteEmptyExercisesReturnsNull() {
        try {
            Map map = new Map();
            PathFinder pf = new PathFinder(map);
            Path route = pf.findFeedRoute(new Point(0, 0), new Point(1, 1), new ArrayList<>());

            if (route != null) {
                results.fail("testFeedRouteEmptyExercisesReturnsNull: expected null for empty exercise list");
            } else {
                results.pass("testFeedRouteEmptyExercisesReturnsNull: correctly returned null for empty feed");
            }
        } catch (Exception e) {
            results.fail("testFeedRouteEmptyExercisesReturnsNull: unexpected exception: " + e.getMessage());
        }
    }

    private void testStartOnObstacleReturnsNull() {
        try {
            Map map = new Map();
            Point obstaclePoint = new Point(0, 0);
            map.addObstacle(new Obstacles(ObstacleType.ROCK, obstaclePoint));

            User user = buildUser("alice");
            Point p1 = new Point(0, 0);
            Point p2 = new Point(1, 0);
            user.addExercise(buildExercise("Run", ExerciseType.RUNNING, p1, p2));

            PathFinder pf = new PathFinder(map);
            Path route = pf.findPersonalRoute(p1, p2, user);

            if (route != null) {
                results.fail("testStartOnObstacleReturnsNull: expected null when start is an obstacle");
            } else {
                results.pass("testStartOnObstacleReturnsNull: correctly returned null when start is blocked");
            }
        } catch (Exception e) {
            results.fail("testStartOnObstacleReturnsNull: unexpected exception: " + e.getMessage());
        }
    }

    private void testRouteContainsStartAndEnd() {
        try {
            Map map = new Map();
            User user = buildUser("alice");

            Point start = new Point(0, 0);
            Point mid   = new Point(1, 0);
            Point end   = new Point(2, 0);
            user.addExercise(buildExercise("Run", ExerciseType.RUNNING, start, mid, end));

            PathFinder pf = new PathFinder(map);
            Path route = pf.findPersonalRoute(start, end, user);

            if (route == null) {
                results.fail("testRouteContainsStartAndEnd: route should not be null");
                return;
            }

            List<Point> waypoints = route.getWaypoints();
            Point first = waypoints.get(0);
            Point last  = waypoints.get(waypoints.size() - 1);

            if (!first.equals(start)) {
                results.fail("testRouteContainsStartAndEnd: first waypoint should be start");
            } else if (!last.equals(end)) {
                results.fail("testRouteContainsStartAndEnd: last waypoint should be end");
            } else {
                results.pass("testRouteContainsStartAndEnd: route begins at start and ends at end");
            }
        } catch (Exception e) {
            results.fail("testRouteContainsStartAndEnd: unexpected exception: " + e.getMessage());
        }
    }

    private User buildUser(String username) {
        return new User.Builder().username(username).password("pass").build();
    }

    private Exercise buildExercise(String name, ExerciseType type, Point... points) {
        Path path = new Path(name + "_route");
        for (Point p : points) {
            path.addWaypoint(p);
        }
        return new Exercise.Builder()
                .name(name)
                .type(type)
                .distance(1.0)
                .duration(10)
                .exercisePath(path)
                .build();
    }
}
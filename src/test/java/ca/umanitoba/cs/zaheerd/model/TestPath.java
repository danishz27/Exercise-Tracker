package ca.umanitoba.cs.zaheerd.tests.model;

import ca.umanitoba.cs.zaheerd.model.Path;
import ca.umanitoba.cs.zaheerd.model.Point;
import ca.umanitoba.cs.zaheerd.tests.TestResults;
import ca.umanitoba.cs.zaheerd.tests.TestSuite;

public class TestPath implements TestSuite {

    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "Path tests";
    }

    @Override
    public TestResults runTests() {
        testHappyPath();
        testNullNameRejected();
        testIsValidRequiresTwoWaypoints();
        testIsWithinBounds();
        testWaypointOutOfBoundsReturnsFalse();
        return results;
    }

    private void testHappyPath() {
        try {
            Path path = new Path("test_route");
            path.addWaypoint(new Point(0, 0));
            path.addWaypoint(new Point(3, 4));

            if (!path.getName().equals("test_route")) {
                results.fail("testHappyPath: name mismatch");
            } else if (path.getWaypoints().size() != 2) {
                results.fail("testHappyPath: expected 2 waypoints");
            } else {
                results.pass("testHappyPath: path built and waypoints added correctly");
            }
        } catch (Exception e) {
            results.fail("testHappyPath: unexpected exception: " + e.getMessage());
        }
    }

    private void testNullNameRejected() {
        try {
            new Path(null);
            results.fail("testNullNameRejected: expected exception for null name");
        } catch (NullPointerException | IllegalStateException e) {
            results.pass("testNullNameRejected: correctly rejected null name");
        }
    }

    private void testIsValidRequiresTwoWaypoints() {
        try {
            Path path = new Path("route");
            if (path.isValid()) {
                results.fail("testIsValidRequiresTwoWaypoints: empty path should not be valid");
                return;
            }
            path.addWaypoint(new Point(0, 0));
            if (path.isValid()) {
                results.fail("testIsValidRequiresTwoWaypoints: one-waypoint path should not be valid");
                return;
            }
            path.addWaypoint(new Point(1, 1));
            if (!path.isValid()) {
                results.fail("testIsValidRequiresTwoWaypoints: two-waypoint path should be valid");
            } else {
                results.pass("testIsValidRequiresTwoWaypoints: isValid correctly requires at least 2 waypoints");
            }
        } catch (Exception e) {
            results.fail("testIsValidRequiresTwoWaypoints: unexpected exception: " + e.getMessage());
        }
    }

    private void testIsWithinBounds() {
        try {
            Path path = new Path("route");
            path.addWaypoint(new Point(0, 0));
            path.addWaypoint(new Point(9, 9));

            if (!path.isWithinBounds(10, 10)) {
                results.fail("testIsWithinBounds: path within (10,10) should return true");
            } else {
                results.pass("testIsWithinBounds: correctly identified in-bounds path");
            }
        } catch (Exception e) {
            results.fail("testIsWithinBounds: unexpected exception: " + e.getMessage());
        }
    }

    private void testWaypointOutOfBoundsReturnsFalse() {
        try {
            Path path = new Path("route");
            path.addWaypoint(new Point(0, 0));
            path.addWaypoint(new Point(5, 5));

            if (path.isWithinBounds(5, 5)) {
                results.fail("testWaypointOutOfBoundsReturnsFalse: point (5,5) is out of bounds for 5x5 map (valid 0-4)");
            } else {
                results.pass("testWaypointOutOfBoundsReturnsFalse: correctly identified out-of-bounds path");
            }
        } catch (Exception e) {
            results.fail("testWaypointOutOfBoundsReturnsFalse: unexpected exception: " + e.getMessage());
        }
    }
}
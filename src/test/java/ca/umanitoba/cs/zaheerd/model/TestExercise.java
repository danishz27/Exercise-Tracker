package ca.umanitoba.cs.zaheerd.tests.model;

import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.tests.TestResults;
import ca.umanitoba.cs.zaheerd.tests.TestSuite;

public class TestExercise implements TestSuite {

    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "Exercise tests";
    }

    @Override
    public TestResults runTests() {
        testHappyPathRunning();
        testHappyPathCycling();
        testNullNameRejected();
        testEmptyNameRejected();
        testNegativeDurationRejected();
        testNegativeDistanceRejected();
        testZeroDistanceAllowed();
        testNullPathRejected();
        return results;
    }

    private void testHappyPathRunning() {
        try {
            Exercise ex = buildExercise("Morning Run", ExerciseType.RUNNING, 5.2, 30);

            if (!ex.getName().equals("Morning Run")) {
                results.fail("testHappyPathRunning: name mismatch");
            } else if (ex.getType() != ExerciseType.RUNNING) {
                results.fail("testHappyPathRunning: type mismatch");
            } else if (ex.getDistance() != 5.2) {
                results.fail("testHappyPathRunning: distance mismatch");
            } else if (ex.getDuration() != 30) {
                results.fail("testHappyPathRunning: duration mismatch");
            } else {
                results.pass("testHappyPathRunning: RUNNING exercise built with correct fields");
            }
        } catch (Exception e) {
            results.fail("testHappyPathRunning: unexpected exception: " + e.getMessage());
        }
    }

    private void testHappyPathCycling() {
        try {
            Exercise ex = buildExercise("Evening Ride", ExerciseType.CYCLING, 12.0, 45);

            if (ex.getType() != ExerciseType.CYCLING) {
                results.fail("testHappyPathCycling: type should be CYCLING");
            } else {
                results.pass("testHappyPathCycling: CYCLING exercise built correctly");
            }
        } catch (Exception e) {
            results.fail("testHappyPathCycling: unexpected exception: " + e.getMessage());
        }
    }

    private void testNullNameRejected() {
        try {
            new Exercise.Builder().name(null);
            results.fail("testNullNameRejected: expected exception for null name");
        } catch (NullPointerException | IllegalStateException e) {
            results.pass("testNullNameRejected: correctly rejected null name");
        }
    }

    private void testEmptyNameRejected() {
        try {
            new Exercise.Builder().name("   ");
            results.fail("testEmptyNameRejected: expected exception for blank name");
        } catch (IllegalStateException e) {
            results.pass("testEmptyNameRejected: correctly rejected blank name");
        }
    }

    private void testNegativeDurationRejected() {
        try {
            new Exercise.Builder().duration(-1);
            results.fail("testNegativeDurationRejected: expected exception for negative duration");
        } catch (IllegalStateException e) {
            results.pass("testNegativeDurationRejected: correctly rejected negative duration");
        }
    }

    private void testNegativeDistanceRejected() {
        try {
            new Exercise.Builder().distance(-0.1);
            results.fail("testNegativeDistanceRejected: expected exception for negative distance");
        } catch (IllegalStateException e) {
            results.pass("testNegativeDistanceRejected: correctly rejected negative distance");
        }
    }

    private void testZeroDistanceAllowed() {
        try {
            Exercise ex = buildExercise("Stationary", ExerciseType.RUNNING, 0.0, 10);
            if (ex.getDistance() != 0.0) {
                results.fail("testZeroDistanceAllowed: distance should be 0.0");
            } else {
                results.pass("testZeroDistanceAllowed: zero distance is valid");
            }
        } catch (Exception e) {
            results.fail("testZeroDistanceAllowed: unexpected exception: " + e.getMessage());
        }
    }

    private void testNullPathRejected() {
        try {
            new Exercise.Builder()
                    .name("Run")
                    .type(ExerciseType.RUNNING)
                    .distance(1.0)
                    .duration(10)
                    .exercisePath(null);
            results.fail("testNullPathRejected: expected exception for null path");
        } catch (NullPointerException | IllegalStateException e) {
            results.pass("testNullPathRejected: correctly rejected null exercisePath");
        }
    }


    private Exercise buildExercise(String name, ExerciseType type, double distance, int duration) {
        Path path = new Path(name + "_route");
        path.addWaypoint(new Point(0, 0));
        path.addWaypoint(new Point(2, 2));
        return new Exercise.Builder()
                .name(name)
                .type(type)
                .distance(distance)
                .duration(duration)
                .exercisePath(path)
                .build();
    }
}
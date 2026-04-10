package ca.umanitoba.cs.zaheerd.tests.model;

import ca.umanitoba.cs.zaheerd.model.*;
import ca.umanitoba.cs.zaheerd.tests.TestResults;
import ca.umanitoba.cs.zaheerd.tests.TestSuite;

public class TestUser implements TestSuite {

    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "User tests";
    }

    @Override
    public TestResults runTests() {
        testHappyPathBuild();
        testNullUsernameRejected();
        testEmptyUsernameRejected();
        testNullPasswordRejected();
        testAddAndGetExercise();
        testRemoveExerciseValidIndex();
        testRemoveExerciseInvalidIndex();
        testFollowUser();
        testFollowSelfThrows();
        testFollowSameUserTwiceThrows();
        testUnfollowUser();
        testUnfollowUserNotFollowingThrows();
        testIsFollowing();
        testGetFeedIncludesFollowedExercises();
        testSetUsernameUpdates();
        testSetPasswordUpdates();
        return results;
    }

    private void testHappyPathBuild() {
        try {
            User user = new User.Builder()
                    .username("alice")
                    .password("pass123")
                    .build();

            if (!user.getUsername().equals("alice")) {
                results.fail("testHappyPathBuild: username mismatch");
            } else if (!user.getPassword().equals("pass123")) {
                results.fail("testHappyPathBuild: password mismatch");
            } else if (!user.getExercises().isEmpty()) {
                results.fail("testHappyPathBuild: exercises should start empty");
            } else if (!user.getFollowing().isEmpty()) {
                results.fail("testHappyPathBuild: following should start empty");
            } else {
                results.pass("testHappyPathBuild: user built with correct initial state");
            }
        } catch (Exception e) {
            results.fail("testHappyPathBuild: unexpected exception: " + e.getMessage());
        }
    }

    private void testNullUsernameRejected() {
        try {
            new User.Builder().username(null);
            results.fail("testNullUsernameRejected: expected exception for null username");
        } catch (NullPointerException | IllegalStateException e) {
            results.pass("testNullUsernameRejected: correctly rejected null username");
        }
    }

    private void testEmptyUsernameRejected() {
        try {
            new User.Builder().username("   ");
            results.fail("testEmptyUsernameRejected: expected exception for blank username");
        } catch (IllegalStateException e) {
            results.pass("testEmptyUsernameRejected: correctly rejected blank username");
        }
    }

    private void testNullPasswordRejected() {
        try {
            new User.Builder().username("alice").password(null);
            results.fail("testNullPasswordRejected: expected exception for null password");
        } catch (NullPointerException | IllegalStateException e) {
            results.pass("testNullPasswordRejected: correctly rejected null password");
        }
    }

    private void testAddAndGetExercise() {
        try {
            User user = buildUser("alice");
            Exercise ex = buildExercise("Morning Run", ExerciseType.RUNNING, 5.0, 30);
            user.addExercise(ex);

            if (user.getExercises().size() != 1) {
                results.fail("testAddAndGetExercise: expected 1 exercise, got " + user.getExercises().size());
            } else if (!user.getExercises().get(0).getName().equals("Morning Run")) {
                results.fail("testAddAndGetExercise: exercise name mismatch");
            } else {
                results.pass("testAddAndGetExercise: exercise added and retrieved correctly");
            }
        } catch (Exception e) {
            results.fail("testAddAndGetExercise: unexpected exception: " + e.getMessage());
        }
    }

    private void testRemoveExerciseValidIndex() {
        try {
            User user = buildUser("alice");
            user.addExercise(buildExercise("Run A", ExerciseType.RUNNING, 3.0, 20));
            user.addExercise(buildExercise("Run B", ExerciseType.CYCLING, 6.0, 40));
            user.removeExercise(0);

            if (user.getExercises().size() != 1) {
                results.fail("testRemoveExerciseValidIndex: expected 1 exercise after removal");
            } else if (!user.getExercises().get(0).getName().equals("Run B")) {
                results.fail("testRemoveExerciseValidIndex: wrong exercise remains after removal");
            } else {
                results.pass("testRemoveExerciseValidIndex: correct exercise removed");
            }
        } catch (Exception e) {
            results.fail("testRemoveExerciseValidIndex: unexpected exception: " + e.getMessage());
        }
    }

    private void testRemoveExerciseInvalidIndex() {
        try {
            User user = buildUser("alice");
            user.addExercise(buildExercise("Run A", ExerciseType.RUNNING, 3.0, 20));
            user.removeExercise(5);
            results.fail("testRemoveExerciseInvalidIndex: expected exception for out-of-bounds index");
        } catch (IllegalStateException e) {
            results.pass("testRemoveExerciseInvalidIndex: correctly rejected invalid index");
        }
    }

    private void testFollowUser() {
        try {
            User alice = buildUser("alice");
            User bob   = buildUser("bob");
            alice.followUser(bob);

            if (!alice.isFollowing(bob)) {
                results.fail("testFollowUser: alice should be following bob");
            } else if (alice.getFollowing().size() != 1) {
                results.fail("testFollowUser: following list should have 1 entry");
            } else {
                results.pass("testFollowUser: follow relationship correctly established");
            }
        } catch (Exception e) {
            results.fail("testFollowUser: unexpected exception: " + e.getMessage());
        }
    }

    private void testFollowSelfThrows() {
        try {
            User alice = buildUser("alice");
            alice.followUser(alice);
            results.fail("testFollowSelfThrows: expected exception when following self");
        } catch (IllegalStateException e) {
            results.pass("testFollowSelfThrows: correctly prevented self-follow");
        }
    }

    private void testFollowSameUserTwiceThrows() {
        try {
            User alice = buildUser("alice");
            User bob   = buildUser("bob");
            alice.followUser(bob);
            alice.followUser(bob);
            results.fail("testFollowSameUserTwiceThrows: expected exception on duplicate follow");
        } catch (IllegalStateException e) {
            results.pass("testFollowSameUserTwiceThrows: correctly prevented duplicate follow");
        }
    }

    private void testUnfollowUser() {
        try {
            User alice = buildUser("alice");
            User bob   = buildUser("bob");
            alice.followUser(bob);
            alice.unfollowUser(bob);

            if (alice.isFollowing(bob)) {
                results.fail("testUnfollowUser: alice should no longer follow bob");
            } else if (!alice.getFollowing().isEmpty()) {
                results.fail("testUnfollowUser: following list should be empty after unfollow");
            } else {
                results.pass("testUnfollowUser: unfollow correctly removes relationship");
            }
        } catch (Exception e) {
            results.fail("testUnfollowUser: unexpected exception: " + e.getMessage());
        }
    }

    private void testUnfollowUserNotFollowingThrows() {
        try {
            User alice = buildUser("alice");
            User bob   = buildUser("bob");
            alice.unfollowUser(bob);
            results.fail("testUnfollowUserNotFollowingThrows: expected exception when not following");
        } catch (IllegalStateException e) {
            results.pass("testUnfollowUserNotFollowingThrows: correctly rejected unfollow of non-followed user");
        }
    }

    private void testIsFollowing() {
        try {
            User alice = buildUser("alice");
            User bob   = buildUser("bob");
            User carol = buildUser("carol");
            alice.followUser(bob);

            if (!alice.isFollowing(bob)) {
                results.fail("testIsFollowing: should be following bob");
            } else if (alice.isFollowing(carol)) {
                results.fail("testIsFollowing: should not be following carol");
            } else {
                results.pass("testIsFollowing: isFollowing returns correct results");
            }
        } catch (Exception e) {
            results.fail("testIsFollowing: unexpected exception: " + e.getMessage());
        }
    }


    private void testGetFeedIncludesFollowedExercises() {
        try {
            User alice = buildUser("alice");
            User bob   = buildUser("bob");

            Exercise aliceEx = buildExercise("Alice Run", ExerciseType.RUNNING, 3.0, 20);
            Exercise bobEx   = buildExercise("Bob Ride",  ExerciseType.CYCLING, 8.0, 45);
            alice.addExercise(aliceEx);
            bob.addExercise(bobEx);
            alice.followUser(bob);

            if (alice.getFeed().size() != 2) {
                results.fail("testGetFeedIncludesFollowedExercises: expected 2 feed entries, got " + alice.getFeed().size());
            } else {
                results.pass("testGetFeedIncludesFollowedExercises: feed correctly includes own + followed exercises");
            }
        } catch (Exception e) {
            results.fail("testGetFeedIncludesFollowedExercises: unexpected exception: " + e.getMessage());
        }
    }

    private void testSetUsernameUpdates() {
        try {
            User user = buildUser("alice");
            user.setUsername("alicia");

            if (!user.getUsername().equals("alicia")) {
                results.fail("testSetUsernameUpdates: username was not updated");
            } else {
                results.pass("testSetUsernameUpdates: username updated correctly");
            }
        } catch (Exception e) {
            results.fail("testSetUsernameUpdates: unexpected exception: " + e.getMessage());
        }
    }

    private void testSetPasswordUpdates() {
        try {
            User user = buildUser("alice");
            user.setPassword("newpass");

            if (!user.getPassword().equals("newpass")) {
                results.fail("testSetPasswordUpdates: password was not updated");
            } else {
                results.pass("testSetPasswordUpdates: password updated correctly");
            }
        } catch (Exception e) {
            results.fail("testSetPasswordUpdates: unexpected exception: " + e.getMessage());
        }
    }

    private User buildUser(String username) {
        return new User.Builder()
                .username(username)
                .password("password")
                .build();
    }

    private Exercise buildExercise(String name, ExerciseType type, double distance, int duration) {
        Path path = new Path(name + "_route");
        path.addWaypoint(new Point(0, 0));
        path.addWaypoint(new Point(1, 1));
        return new Exercise.Builder()
                .name(name)
                .type(type)
                .distance(distance)
                .duration(duration)
                .exercisePath(path)
                .build();
    }
}
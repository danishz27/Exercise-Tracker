package ca.umanitoba.cs.zaheerd.persistence.json;

import ca.umanitoba.cs.zaheerd.model.Exercise;
import ca.umanitoba.cs.zaheerd.model.ExerciseType;
import ca.umanitoba.cs.zaheerd.model.Map;
import ca.umanitoba.cs.zaheerd.model.Obstacles;
import ca.umanitoba.cs.zaheerd.model.ObstacleType;
import ca.umanitoba.cs.zaheerd.model.Path;
import ca.umanitoba.cs.zaheerd.model.Point;
import ca.umanitoba.cs.zaheerd.model.Tracker;
import ca.umanitoba.cs.zaheerd.model.User;
import ca.umanitoba.cs.zaheerd.persistence.NotFoundException;
import ca.umanitoba.cs.zaheerd.persistence.PersistenceException;
import ca.umanitoba.cs.zaheerd.persistence.TrackerPersistence;
import com.google.common.base.Preconditions;

import javax.json.*;
import java.io.IOException;
import java.nio.file.Files;

public class TrackerPersistenceJson implements TrackerPersistence {

    private final java.nio.file.Path storageFile;

    public TrackerPersistenceJson(java.nio.file.Path storageFile) {
        Preconditions.checkNotNull(storageFile, "storageFile cannot be null");
        this.storageFile = storageFile;
        checkInvariant();
    }

    @Override
    public Tracker saveTracker(Tracker tracker) throws PersistenceException {
        Preconditions.checkNotNull(tracker, "tracker cannot be null");
        checkInvariant();

        try {
            JsonObjectBuilder root = Json.createObjectBuilder();
            JsonArrayBuilder usersArray = Json.createArrayBuilder();
            for (User user : tracker.getUsers()) {
                usersArray.add(userToJson(user));
            }
            root.add("users", usersArray);
            JsonArrayBuilder obstaclesArray = Json.createArrayBuilder();
            for (Obstacles obstacle : tracker.getWorldMap().getObstacles()) {
                obstaclesArray.add(obstacleToJson(obstacle));
            }
            root.add("obstacles", obstaclesArray);

            JsonWriter writer = Json.createWriter(Files.newOutputStream(storageFile));
            writer.writeObject(root.build());
            writer.close();

        } catch (IOException e) {
            throw new PersistenceException("Failed to write tracker to " + storageFile);
        }

        return tracker;
    }

    public Tracker loadTracker() throws NotFoundException, PersistenceException {
        checkInvariant();

        if (!Files.exists(storageFile)) {
            throw new NotFoundException("No saved tracker found at " + storageFile);
        }

        try {
            JsonReader reader = Json.createReader(Files.newInputStream(storageFile));
            JsonObject root = reader.readObject();
            reader.close();

            Tracker tracker = new Tracker();

            JsonArray usersArray = root.getJsonArray("users");
            for (JsonValue userVal : usersArray) {
                JsonObject userJson = userVal.asJsonObject();
                User user = userFromJson(userJson, tracker.getWorldMap());
                tracker.addUser(user);
            }

            for (JsonValue userVal : usersArray) {
                JsonObject userJson = userVal.asJsonObject();
                String username = userJson.getString("username");
                User user = tracker.getUser(username);

                JsonArray followingArray = userJson.getJsonArray("following");
                for (JsonValue followVal : followingArray) {
                    String followedName = ((JsonString) followVal).getString();
                    User followed = tracker.getUser(followedName);
                    if (followed != null) {
                        user.followUser(followed);
                    }
                }
            }

            JsonArray obstaclesArray = root.getJsonArray("obstacles");
            for (JsonValue obsVal : obstaclesArray) {
                JsonObject obsJson = obsVal.asJsonObject();
                Obstacles obstacle = obstacleFromJson(obsJson);
                tracker.getWorldMap().addObstacle(obstacle);
            }

            return tracker;

        } catch (IOException e) {
            throw new PersistenceException("Failed to read tracker from " + storageFile);
        } catch (JsonException e) {
            throw new PersistenceException("Tracker file is corrupt or malformed: " + storageFile);
        }
    }


    private JsonObject userToJson(User user) {
        Preconditions.checkNotNull(user, "user cannot be null");

        JsonArrayBuilder exercisesArray = Json.createArrayBuilder();
        for (Exercise exercise : user.getExercises()) {
            exercisesArray.add(exerciseToJson(exercise));
        }

        JsonArrayBuilder followingArray = Json.createArrayBuilder();
        for (User followed : user.getFollowing()) {
            followingArray.add(followed.getUsername());
        }

        return Json.createObjectBuilder()
                .add("username", user.getUsername())
                .add("password", user.getPassword())
                .add("exercises", exercisesArray)
                .add("following", followingArray)
                .build();
    }

    private JsonObject exerciseToJson(Exercise exercise) {
        Preconditions.checkNotNull(exercise, "exercise cannot be null");

        return Json.createObjectBuilder()
                .add("name", exercise.getName())
                .add("type", exercise.getType().toString())
                .add("distance", exercise.getDistance())
                .add("duration", exercise.getDuration())
                .add("path", pathToJson(exercise.getExercisePath()))
                .build();
    }

    private JsonObject pathToJson(Path modelPath) {
        Preconditions.checkNotNull(modelPath, "path cannot be null");

        JsonArrayBuilder waypointsArray = Json.createArrayBuilder();
        for (Point waypoint : modelPath.getWaypoints()) {
            waypointsArray.add(pointToJson(waypoint));
        }

        return Json.createObjectBuilder()
                .add("name", modelPath.getName())
                .add("waypoints", waypointsArray)
                .build();
    }

    private JsonObject pointToJson(Point point) {
        Preconditions.checkNotNull(point, "point cannot be null");

        return Json.createObjectBuilder()
                .add("x", point.getX())
                .add("y", point.getY())
                .build();
    }

    private JsonObject obstacleToJson(Obstacles obstacle) {
        Preconditions.checkNotNull(obstacle, "obstacle cannot be null");

        return Json.createObjectBuilder()
                .add("type", obstacle.getType().toString())
                .add("x", obstacle.getX())
                .add("y", obstacle.getY())
                .build();
    }


    private User userFromJson(JsonObject userJson, Map worldMap) {
        Preconditions.checkNotNull(userJson, "userJson cannot be null");
        Preconditions.checkNotNull(worldMap, "worldMap cannot be null");

        String username = userJson.getString("username");
        String password = userJson.getString("password");

        User user = new User.Builder()
                .username(username)
                .password(password)
                .build();

        JsonArray exercisesArray = userJson.getJsonArray("exercises");
        for (JsonValue exVal : exercisesArray) {
            JsonObject exJson = exVal.asJsonObject();
            Exercise exercise = exerciseFromJson(exJson, worldMap);
            user.addExercise(exercise);
        }

        return user;
    }

    private Exercise exerciseFromJson(JsonObject exJson, Map worldMap) {
        Preconditions.checkNotNull(exJson, "exJson cannot be null");
        Preconditions.checkNotNull(worldMap, "worldMap cannot be null");

        String name = exJson.getString("name");
        ExerciseType type = ExerciseType.valueOf(exJson.getString("type"));
        double distance = exJson.getJsonNumber("distance").doubleValue();
        int duration = exJson.getInt("duration");

        Path modelPath = pathFromJson(exJson.getJsonObject("path"));
        if (modelPath.isWithinBounds(worldMap.getWidth(), worldMap.getHeight())) {
            worldMap.addPath(modelPath);
        }

        return new Exercise.Builder()
                .name(name)
                .type(type)
                .distance(distance)
                .duration(duration)
                .exercisePath(modelPath)
                .build();
    }

    private Path pathFromJson(JsonObject pathJson) {
        Preconditions.checkNotNull(pathJson, "pathJson cannot be null");

        String name = pathJson.getString("name");
        Path modelPath = new Path(name);

        JsonArray waypointsArray = pathJson.getJsonArray("waypoints");
        for (JsonValue wpVal : waypointsArray) {
            JsonObject wpJson = wpVal.asJsonObject();
            modelPath.addWaypoint(pointFromJson(wpJson));
        }

        return modelPath;
    }

    private Point pointFromJson(JsonObject pointJson) {
        Preconditions.checkNotNull(pointJson, "pointJson cannot be null");

        int x = pointJson.getInt("x");
        int y = pointJson.getInt("y");
        return new Point(x, y);
    }

    private Obstacles obstacleFromJson(JsonObject obsJson) {
        Preconditions.checkNotNull(obsJson, "obsJson cannot be null");

        ObstacleType type = ObstacleType.valueOf(obsJson.getString("type"));
        int x = obsJson.getInt("x");
        int y = obsJson.getInt("y");
        return new Obstacles(type, new Point(x, y));
    }

    private void checkInvariant() {
        Preconditions.checkNotNull(storageFile, "storageFile must not be null");
    }
}
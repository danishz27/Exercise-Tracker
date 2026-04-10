package ca.umanitoba.cs.zaheerd.model;

import com.google.common.base.Preconditions;

public class Exercise {
    private final String name;
    private final ExerciseType type;
    private final double distance;
    private final int duration;
    private final Path exercisePath;

    private Exercise(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.distance = builder.distance;
        this.duration = builder.duration;
        this.exercisePath = builder.exercisePath;
        checkExercise();
    }

    public String getName() { return name; }
    public ExerciseType getType() { return type; }
    public double getDistance() { return distance; }
    public int getDuration() { return duration; }
    public Path getExercisePath() { return exercisePath; }

    private void checkExercise() {
        Preconditions.checkNotNull(name, "name must not be null");
        Preconditions.checkState(!name.trim().isEmpty(), "name must not be empty");
        Preconditions.checkNotNull(type, "type must not be null");
        Preconditions.checkState(distance >= 0, "distance must be non-negative");
        Preconditions.checkState(duration > 0, "duration must be positive");
        Preconditions.checkNotNull(exercisePath, "exercisePath must not be null");
    }

    public static class Builder {
        private String name;
        private ExerciseType type;
        private double distance;
        private int duration;
        private Path exercisePath;

        public Builder name(String name) {
            Preconditions.checkNotNull(name, "name cannot be null");
            Preconditions.checkState(!name.trim().isEmpty(), "name cannot be empty");
            this.name = name;
            return this;
        }

        public Builder type(ExerciseType type) {
            Preconditions.checkNotNull(type, "type cannot be null");
            this.type = type;
            return this;
        }

        public Builder distance(double distance) {
            Preconditions.checkState(distance >= 0, "distance must be non-negative");
            this.distance = distance;
            return this;
        }

        public Builder duration(int duration) {
            Preconditions.checkState(duration > 0, "duration must be positive");
            this.duration = duration;
            return this;
        }

        public Builder exercisePath(Path exercisePath) {
            Preconditions.checkNotNull(exercisePath, "exercisePath cannot be null");
            this.exercisePath = exercisePath;
            return this;
        }

        public Exercise build() {
            Preconditions.checkNotNull(name, "name must be set");
            Preconditions.checkNotNull(type, "type must be set");
            Preconditions.checkNotNull(exercisePath, "exercisePath must be set");
            return new Exercise(this);
        }
    }
}



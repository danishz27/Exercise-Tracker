package ca.umanitoba.cs.zaheerd.model;

import com.google.common.base.Preconditions;


public class Obstacles {
    private final ObstacleType type;
    private final Point position;

    public Obstacles(ObstacleType type, Point position) {
        Preconditions.checkNotNull(type, "obstacle type cannot be null");
        Preconditions.checkNotNull(position, "position cannot be null");
        this.type = type;
        this.position = position;

        checkObstacles();
    }

    public ObstacleType getType() {
        return type;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public boolean isWithinBounds(int width, int height) {
        Preconditions.checkState(width > 0, "width must be positive");
        Preconditions.checkState(height > 0, "height must be positive");
        return position.getX() >= 0 && position.getX() < width
                && position.getY() >= 0 && position.getY() < height;
    }

    private void checkObstacles() {
        Preconditions.checkNotNull(type, "type must not be null");
        Preconditions.checkNotNull(position, "position must not be null");
    }
}

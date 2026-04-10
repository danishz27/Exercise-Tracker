package ca.umanitoba.cs.zaheerd.model;

import com.google.common.base.Preconditions;

import java.util.*;

public class Map {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    private final int width;
    private final int height;
    private final List<Path> paths;
    private final List<Obstacles> obstacleList;

    public Map() {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.paths = new ArrayList<>();
        this.obstacleList = new ArrayList<>();
        checkMap();
    }

    public void addPath(Path path) {
        Preconditions.checkNotNull(path, "path cannot be null");
        Preconditions.checkState(path.isWithinBounds(width, height),
                "path must be within map bounds");
        paths.add(path);
        checkMap();
    }

    public void removePath(Path path) {
        Preconditions.checkNotNull(path, "path cannot be null");
        paths.remove(path);
        checkMap();
    }

    public void addObstacle(Obstacles obstacle) {
        Preconditions.checkNotNull(obstacle, "obstacle cannot be null");
        Preconditions.checkState(obstacle.isWithinBounds(width, height),
                "obstacle must be within map bounds");
        obstacleList.add(obstacle);
        checkMap();
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public List<Path> getPaths() {
        return new ArrayList<>(paths);
    }

    public List<Obstacles> getObstacles() {
        return new ArrayList<>(obstacleList);
    }

    public boolean isObstacle(int x, int y) {
        for (Obstacles obstacle : obstacleList) {
            if (obstacle.getX() == x && obstacle.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private void checkMap() {
        Preconditions.checkState(width > 0, "width must be positive");
        Preconditions.checkState(height > 0, "height must be positive");
        Preconditions.checkNotNull(paths, "paths must not be null");
        Preconditions.checkNotNull(obstacleList, "obstacleList must not be null");
    }

}

package ca.umanitoba.cs.zaheerd.output;

import ca.umanitoba.cs.zaheerd.model.*;
import com.google.common.base.Preconditions;

public class MapPrinter {

    private static final char EMPTY = '.';
    private static final char PATH = '>';
    private static final char OBSTACLE = '*';
    private static final char FOUND_ROUTE = '@';

    public static char[][] makeGrid(Map map) {
        Preconditions.checkNotNull(map, "map cannot be null");
        char[][] cells = initializeGrid(map);

        for (Path path : map.getPaths()) {
            for (Point waypoint : path.getWaypoints()) {
                int x = waypoint.getX();
                int y = waypoint.getY();
                if (map.isWithinBounds(x, y)) {
                    cells[y][x] = PATH;
                }
            }
        }

        for (Obstacles obstacle : map.getObstacles()) {
            int x = obstacle.getX();
            int y = obstacle.getY();
            if (map.isWithinBounds(x, y)) {
                cells[y][x] = OBSTACLE;
            }
        }

        return cells;
    }

    public static char[][] makeGridWithPath(Map map, Path specificPath) {
        Preconditions.checkNotNull(map, "map cannot be null");
        Preconditions.checkNotNull(specificPath, "path cannot be null");
        char[][] cells = initializeGrid(map);

        for (Point waypoint : specificPath.getWaypoints()) {
            int x = waypoint.getX();
            int y = waypoint.getY();
            if (map.isWithinBounds(x, y)) {
                cells[y][x] = PATH;
            }
        }

        for (Obstacles obstacle : map.getObstacles()) {
            int x = obstacle.getX();
            int y = obstacle.getY();
            if (map.isWithinBounds(x, y)) {
                cells[y][x] = OBSTACLE;
            }
        }

        return cells;
    }

    public static char[][] makeGridWithFoundRoute(Map map, Path foundRoute) {
        Preconditions.checkNotNull(map, "map cannot be null");
        Preconditions.checkNotNull(foundRoute, "foundRoute cannot be null");
        char[][] cells = makeGrid(map);

        for (Point waypoint : foundRoute.getWaypoints()) {
            int x = waypoint.getX();
            int y = waypoint.getY();
            if (map.isWithinBounds(x, y)) {
                cells[y][x] = FOUND_ROUTE;
            }
        }

        return cells;
    }

    public static String formatGrid(char[][] cells) {
        Preconditions.checkNotNull(cells, "cells cannot be null");
        StringBuilder sb = new StringBuilder();
        for (char[] row : cells) {
            for (char cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String getLegend() {
        return EMPTY + " = Empty area\n" +
                OBSTACLE + " = Obstacle\n" +
                PATH + " = Activity path\n" +
                FOUND_ROUTE + " = Found route\n";
    }

    private static char[][] initializeGrid(Map map) {
        char[][] cells = new char[map.getHeight()][map.getWidth()];
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                cells[y][x] = EMPTY;
            }
        }
        return cells;
    }
}

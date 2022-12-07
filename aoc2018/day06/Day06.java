import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day06 {
    public static void main(String[] args) {
        Day06 day = new Day06();
    }

    public Day06() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private int part1(List<Coordinate> input) {
        int minX = input.stream().mapToInt(c -> c.x).min().getAsInt();
        int maxX = input.stream().mapToInt(c -> c.x).max().getAsInt();
        int minY = input.stream().mapToInt(c -> c.y).min().getAsInt();
        int maxY = input.stream().mapToInt(c -> c.y).max().getAsInt();

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                Coordinate tmpC = null;
                int minDistance = Integer.MAX_VALUE;
                boolean nomansland = false;
                // min dis between i,j and c.x, c.y
                for (Coordinate coordinate : input) {
                    int distance = Math.abs(coordinate.x - i) + Math.abs(coordinate.y - j); // hm, just lucky? Maybe sort by distance and pick top only if we only have one of that distance
                    if (distance < minDistance) {
                        minDistance = distance;
                        tmpC = coordinate;
                        nomansland = false;
                    } else if (distance == minDistance){
                        nomansland = true;
                    }
                    if (coordinate.x == minX || coordinate.x == maxX || coordinate.y == minY || coordinate.y == maxY) {
                        coordinate.edge = true; // hm, only do this once? even remove
                    }
                }
                if (!nomansland) {
                    tmpC.manhattanArea++;
                }
            }
        }

        return input.stream().filter(coordinate -> !coordinate.edge).mapToInt(coordinate -> coordinate.manhattanArea).max().getAsInt();
    }

    private int part2(List<Coordinate> input) {
        int minX = input.stream().mapToInt(c -> c.x).min().getAsInt();
        int maxX = input.stream().mapToInt(c -> c.x).max().getAsInt();
        int minY = input.stream().mapToInt(c -> c.y).min().getAsInt();
        int maxY = input.stream().mapToInt(c -> c.y).max().getAsInt();

        int[][] grid = new int[maxX+1][maxY+1];

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                for (Coordinate coordinate : input) {
                    int distance = Math.abs(coordinate.x - i) + Math.abs(coordinate.y - j);
                    grid[i][j] += distance;
                }
            }
        }

        int regionSize = 0;
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                if (grid[i][j] < 10000) {
                    regionSize++;
                }
            }
        }


        return regionSize;
    }

    private List<Coordinate> init(){
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.map(Coordinate::new).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private class Coordinate {
        int x;
        int y;
        int manhattanArea;
        boolean edge;

        public Coordinate(String line) {
            String[] split = line.split(", ");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
        }
    }


}

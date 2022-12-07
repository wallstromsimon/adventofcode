import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day11 {
    public static void main(String[] args) {
        Day11 day = new Day11();
    }

    public Day11() {
        int input = 1723;
        //int input = 18;
        long start = System.currentTimeMillis();

        //System.out.println(calculatePower(122,79,input));

        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(int gridSerialNumber) {
        int gridSize = 300;
        long[][] grid = populateGrid(gridSerialNumber, gridSize);

        int powerSquareSize = 3;
        int maxX = 0;
        int maxY = 0;
        long maxPower = Integer.MIN_VALUE;
        for (int i = 0; i < (gridSize - powerSquareSize); i++) {
            for (int j = 0; j < (gridSize - powerSquareSize); j++) {
                long tmpPower = 0;
                for (int k = i; k < i + powerSquareSize; k++) {
                    for (int l = j; l < j + powerSquareSize; l++) {
                        tmpPower += grid[k][l];
                    }
                }
                if (tmpPower > maxPower) {
                    maxX = i + 1;
                    maxY = j + 1;
                    maxPower = tmpPower;
                }
            }
        }
        return maxX + "," + maxY;
    }

    private long[][] populateGrid(int gridSerialNumber, int gridSize) {
        long[][] grid = new long[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int x = i + 1;
                int y = j + 1;
                grid[i][j] = calculatePower(x, y, gridSerialNumber);
            }
        }
        return grid;
    }

    private long calculatePower(int x, int y, int gridSerialNumber) {
        int rackID = x + 10;
        long powerLevel = rackID * y;
        powerLevel += gridSerialNumber;
        powerLevel *= rackID;
        if (powerLevel > 99) {
            powerLevel = (powerLevel / 100) % 10;
        } else {
            powerLevel = 0;
        }
        powerLevel -= 5;
        return powerLevel;
    }

    private String part2(int gridSerialNumber) {
        int gridSize = 300;
        long[][] grid = populateGrid(gridSerialNumber, gridSize);

        int maxX = 0;
        int maxY = 0;
        int squareSize = 0;
        long maxPower = Integer.MIN_VALUE;
        for (int powerSquareSize = 0; powerSquareSize < gridSize; powerSquareSize++) {
            for (int i = 0; i < (gridSize - powerSquareSize); i++) {
                for (int j = 0; j < (gridSize - powerSquareSize); j++) {
                    long tmpPower = 0;
                    for (int k = i; k < i + powerSquareSize; k++) {
                        for (int l = j; l < j + powerSquareSize; l++) {
                            tmpPower += grid[k][l];
                        }
                    }
                    if (tmpPower > maxPower) {
                        maxX = i + 1;
                        maxY = j + 1;
                        maxPower = tmpPower;
                        squareSize = powerSquareSize;
                    }
                }
            }
        }

        return maxX + "," + maxY + "," + squareSize;
    }

    private List<String> init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}

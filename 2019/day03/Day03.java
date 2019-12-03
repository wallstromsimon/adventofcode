import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day03 {
    public static void main(String[] args) {
        Day03 day = new Day03();
    }

    public Day03() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input)); // 651
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input)); // 7534
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(List<String> input) {
        List<List<Line>> allLines = getAllPaths(input);
        List<Line> firsLine = allLines.get(0);
        List<Line> secondLine = allLines.get(1);
        List<Point> crossPoints = getCrossPoints(firsLine, secondLine);
        Point origo = new Point(0, 0);
        return "part1: " + crossPoints.stream().mapToInt(point-> origo.distance(point)).min().getAsInt();
    }

    private String part2(List<String> input) {
        List<List<Line>> allLines = getAllPaths(input);
        List<Line> firsLine = allLines.get(0);
        List<Line> secondLine = allLines.get(1);
        List<Point> crossPoints = getCrossPoints(firsLine, secondLine);

        int minCombinedSteps = Integer.MAX_VALUE;
        for (Point cross : crossPoints) {
            int combinedSteps = 0;

            // for each line list, walk until the cross
            for (Line line : firsLine) {
                if (line.containsHorizontal(cross)) {
                    combinedSteps += Math.abs(line.a.x - cross.x);
                    break;
                } else if (line.containsVertical(cross)) {
                    combinedSteps += Math.abs(line.a.y - cross.y);
                    break;
                } else {
                    combinedSteps += Math.abs(line.a.x - line.b.x) + Math.abs(line.a.y - line.b.y);
                }
            }

            for (Line line : secondLine) {
                if (line.containsHorizontal(cross)) {
                    combinedSteps += Math.abs(line.a.x - cross.x);
                    break;
                } else if (line.containsVertical(cross)) {
                    combinedSteps += Math.abs(line.a.y - cross.y);
                    break;
                } else {
                    combinedSteps += Math.abs(line.a.x - line.b.x) + Math.abs(line.a.y - line.b.y);
                }

            }
            minCombinedSteps = minCombinedSteps < combinedSteps ? minCombinedSteps : combinedSteps;
        }

        return "part2: " + minCombinedSteps;
    }

    private List<Point> getCrossPoints(List<Line> firsLine, List<Line> secondLine) {
        List<Point> crosses = new ArrayList<>();
        for (Line line : firsLine) {
            for (Line otherLine : secondLine) {
                Point cross = line.cross(otherLine);
                if (cross != null && !cross.isOrigo()) {
                    crosses.add(cross);
                }
            }
        }
        return crosses;
    }

    private List<List<Line>> getAllPaths(List<String> input) {
        List<List<Line>> allLines = new ArrayList<>();
        for (String path : input) {
            List<String> steps = Arrays.asList(path.split(","));
            List<Line> lines = new ArrayList<>();
            Point lastPoint = new Point(0, 0);
            for (String step : steps) {
                String direction = step.substring(0, 1);
                //System.out.println("dir: " + direction);
                int distance = Integer.parseInt(step.substring(1));
                //System.out.println("dist: " + distance);
                Point newPoint = null;
                switch (direction) {
                    case "U":
                        newPoint = new Point(lastPoint.x, lastPoint.y + distance);
                        break;
                    case "D":
                        newPoint = new Point(lastPoint.x, lastPoint.y - distance);
                        break;
                    case "L":
                        newPoint = new Point(lastPoint.x - distance, lastPoint.y);
                        break;
                    case "R":
                        newPoint = new Point(lastPoint.x + distance, lastPoint.y);
                        break;
                    default:
                        System.out.println("dir is fu: " + direction);
                }
                Line line = new Line(lastPoint, newPoint);
                lines.add(line);
                lastPoint = newPoint;
            }
            allLines.add(lines);
        }
        return allLines;
    }

    private List<String> init() {
        String path = "input.txt";
        //String path = "input_test.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(x: " + x + " y: " + y + ")";
        }

        private int distance(Point otherPoint) {
            return Math.abs(this.x - otherPoint.x) + Math.abs(this.y - otherPoint.y);
        }

        private boolean isOrigo() {
            return x == 0 && y == 0;
        }
    }

    private class Line {
        Point a;
        Point b;

        Line(Point a, Point b) {
            this.a = a;
            this.b = b;
        }

        private boolean isHorizontal() {
            return a.y == b.y;
        }

        private boolean isParallel(Line otherLine) {
            return this.isHorizontal() && otherLine.isHorizontal() || !this.isHorizontal() && !otherLine.isHorizontal();
        }

        private boolean containsPoint(Point point) {
            boolean containsVertical = !this.isHorizontal() && this.a.x == point.x && (this.a.y <= point.y && this.b.y >= point.y || this.b.y <= point.y && this.a.y >= point.y);
            boolean containsHorizontal = this.isHorizontal() && this.a.y == point.y && (this.a.x <= point.x && this.b.x >= point.x || this.b.x <= point.y && this.a.x >= point.x);
            // break out above to methods?
            return containsHorizontal || containsVertical;
        }

        private boolean containsVertical(Point point) {
            return !this.isHorizontal() && this.a.x == point.x && (this.a.y <= point.y && this.b.y >= point.y || this.b.y <= point.y && this.a.y >= point.y);
        }

        private boolean containsHorizontal(Point point) {
            return this.isHorizontal() && this.a.y == point.y && (this.a.x <= point.x && this.b.x >= point.x || this.b.x <= point.y && this.a.x >= point.x);
        }

        // Add crossing method
        private Point cross(Line otherLine) {
            if (this.isParallel(otherLine)) {
                return null;
            }

            Line horizontalLine = this.isHorizontal() ? this : otherLine;
            Line verticalLine = !this.isHorizontal() ? this : otherLine;

            int x = verticalLine.a.x;
            int y = horizontalLine.a.y;

            if (horizontalLine.a.x <= x && horizontalLine.b.x >= x || horizontalLine.b.x <= x && horizontalLine.a.x >= x) {
                if (verticalLine.a.y <= y && verticalLine.b.y >= y || verticalLine.b.y <= y && verticalLine.a.y >= y) {
                    return new Point(x, y);
                }
            }
            return null;
        }

    }
}

import javax.print.DocFlavor;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day15 {
    public static void main(String[] args) {
        Day15 day = new Day15();
    }

    public Day15() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(Cave input) {
        for (int i = 0; i < 5; i++) {
            input.round();
            input.print();
            System.out.println("-----------------------------------");

        }
        return "part1";
    }

    private String part2(Cave input) {
        return "part2";
    }

    private Cave init() {
        String path = "test.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return new Cave(stream); //.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Cave {
        CavePart[][] caveParts;
        int x;
        int y;

        private Cave(Stream<String> inputLines) {
            List<String> input = inputLines.collect(Collectors.toList());
            x = input.get(0).split("").length;
            y = input.size();

            System.out.println("x: " + x + ", y: " + y);
            caveParts = new CavePart[y][x];

            int i = 0;
            for (String line : input) {
                System.out.println(line);
                int j = 0;
                for (String c : line.split("")) {
                    CavePart cavePart = null;
                    switch (c) {
                        case "#":
                            cavePart = new Wall();
                            break;
                        case ".":
                            cavePart = new Open();
                            break;
                        case "E":
                            cavePart = new Elf();
                            break;
                        case "G":
                            cavePart = new Goblin();
                            break;
                        default:
                            System.out.println("What");
                    }
                    caveParts[i][j++] = cavePart;
                }
                i++;
            }
            System.out.println();
            print();
        }

        void round() {
            CavePart[][] tmpCaveParts = new CavePart[y][x];
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    tmpCaveParts[i][j] = caveParts[i][j];
                }
            }
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    // if have turn, take it!
                    CavePart cavePart = caveParts[i][j];
                    if (cavePart.haveTurn()) {
                        Player player = (Player) cavePart;
                        System.out.println("play: [" + i + ", " + j + "]: " + player.symbol);
                        // Check if there are targets at all?
                        List<Coordinate> coordinates = getInRange(cavePart);
                        if (!coordinates.isEmpty()) {
                            coordinates.stream().map(coordinate -> (Player) caveParts[coordinate.y][coordinate.x]).map(coordinate -> coordinate.symbol + " ").forEach(System.out::print);
                            System.out.println(coordinates);

                            // Attack "first" if next to;
                            Coordinate here = new Coordinate(j, i);
                            final Coordinate hereF = new Coordinate(j, i);
                            List<Coordinate> adjacent = coordinates.stream()
                                    .filter(coordinate -> coordinate.isNextTo(hereF))
                                    .collect(Collectors.toList());

                            if (!adjacent.isEmpty()) { // Attack!
                                Player targetLowestHp = adjacent.stream()
                                        .map(coordinate -> (Player) caveParts[coordinate.y][coordinate.x])
                                        .min(Comparator.comparing(player1 -> player.hp))
                                        .get();
                                Coordinate targetLowestHpC = adjacent.stream()
                                        .min(Comparator.comparing(coordinate -> ((Player) caveParts[coordinate.y][coordinate.x]).hp))
                                        .get();
                                targetLowestHp.hp -= player.attack;
                                System.out.println("attack! " + targetLowestHpC);
                                if (targetLowestHp.hp <= 0) {
                                    caveParts[targetLowestHpC.y][targetLowestHpC.x] = new Open();
                                }
                                continue;
                            }

                            // Move one step closer to the closest target, reading style
                            // Move backwards and populate dynamically? Choose first (reading) with lowest number.
                            int minSteps = Integer.MAX_VALUE;
                            Coordinate minDisCoor = new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE);
                            for (Coordinate coordinate : coordinates) {
                                int[][] steps = new int[y][x];
                                for (int k = 0; k < y; k++) {
                                    for (int l = 0; l < x; l++) {
                                        steps[k][l] = Integer.MAX_VALUE;
                                    }
                                }

                                boolean[][] visited = new boolean[y][x];
                                // here = 0 for each neighbour that !blocks
                                steps[coordinate.y][coordinate.x] = 0;
                                visited[coordinate.y][coordinate.x] = true;
                                visited[here.y][here.x] = true;

                                LinkedList<Coordinate> queue = new LinkedList<>();
                                queue.add(new Coordinate(coordinate.x - 1, coordinate.y));
                                queue.add(new Coordinate(coordinate.x + 1, coordinate.y));
                                queue.add(new Coordinate(coordinate.x, coordinate.y - 1));
                                queue.add(new Coordinate(coordinate.x, coordinate.y + 1));

                                while (!queue.isEmpty()) {
                                    Coordinate currCoor = queue.pop();
                                    visited[currCoor.y][currCoor.x] = true;

                                    if (!caveParts[currCoor.y][currCoor.x].blocks()) {
                                        // Take shortst +1 from neighbours
                                        steps[currCoor.y][currCoor.x] = 1
                                                + IntStream.of(steps[currCoor.y][currCoor.x], steps[currCoor.y][currCoor.x], steps[currCoor.y][currCoor.x], steps[currCoor.y][currCoor.x])
                                                .min()
                                                .getAsInt();
                                        if (!visited[currCoor.y - 1][currCoor.x]) {
                                            queue.add(new Coordinate(currCoor.x, currCoor.y - 1));
                                        }
                                        if (!visited[currCoor.y + 1][currCoor.x]) {
                                            queue.add(new Coordinate(currCoor.x, currCoor.y + 1));
                                        }
                                        if (!visited[currCoor.y][currCoor.x - 1]) {
                                            queue.add(new Coordinate(currCoor.x - 1, currCoor.y));
                                        }
                                        if (!visited[currCoor.y][currCoor.x + 1]) {
                                            queue.add(new Coordinate(currCoor.x + 1, currCoor.y));
                                        }
                                    }

                                    if (currCoor.isNextTo(here)) {
                                        // if this is has shorter path or is earlier in read order
                                        if (steps[currCoor.y][currCoor.x] < minSteps) {
                                            minSteps = steps[currCoor.y][currCoor.x];
                                            minDisCoor = currCoor;
                                        } else if (steps[currCoor.y][currCoor.x] == minSteps && (currCoor.y < minDisCoor.y || (
                                                currCoor.y <= minDisCoor.y && currCoor.x < minDisCoor.x))) {
                                            minDisCoor = currCoor;
                                        }
                                    }

                                }

                            }
                            System.out.println("Move: " + here + " -> " + minDisCoor);
                            caveParts[here.y][here.x] = new Open();
                            here = minDisCoor;
                            caveParts[here.y][here.x] = player;

                            // Check if we can attack after move

                            coordinates = getInRange(player);
                            final Coordinate hereF2 = new Coordinate(here.x, here.y);
                            List<Coordinate> adjacent2 = coordinates.stream()
                                    .filter(coordinate -> coordinate.isNextTo(hereF2))
                                    .collect(Collectors.toList());

                            if (!adjacent2.isEmpty()) { // Attack!
                                Player targetLowestHp = adjacent2.stream()
                                        .map(coordinate -> (Player) caveParts[coordinate.y][coordinate.x])
                                        .min(Comparator.comparing(player1 -> player.hp))
                                        .get();
                                Coordinate targetLowestHpC = adjacent2.stream()
                                        .min(Comparator.comparing(coordinate -> ((Player) caveParts[coordinate.y][coordinate.x]).hp))
                                        .get();
                                targetLowestHp.hp -= player.attack;
                                System.out.println("attack! " + targetLowestHpC);
                                if (targetLowestHp.hp <= 0) {
                                    caveParts[targetLowestHpC.y][targetLowestHpC.x] = new Open();
                                }
                                continue;
                            }
                            //print();
                        }

                    }
                }
            }
            //caveParts = tmpCaveParts;
        }

        List<Coordinate> getInRange(CavePart cavePart) {
            List<Coordinate> coordinates = new ArrayList<>();
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    CavePart target = caveParts[i][j];
                    if (target.haveTurn() && !cavePart.symbol.equals(target.symbol)) {
                        coordinates.add(new Coordinate(j, i));
                    }
                }
            }

            return coordinates;
        }

        void print() {
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    System.out.print(caveParts[i][j].symbol);
                }
                System.out.println();
            }
        }
    }

    private abstract class CavePart {
        String symbol;

        abstract boolean blocks();

        abstract boolean haveTurn();

    }

    private class Player extends CavePart {
        int hp = 200;
        int attack = 3;

        @Override
        boolean blocks() {
            return true;
        }

        @Override
        boolean haveTurn() {
            return true;
        }
    }

    private class Elf extends Player {
        private Elf() {
            symbol = "E";

        }
    }

    private class Goblin extends Player {
        private Goblin() {
            symbol = "G";
        }

    }

    private class Wall extends CavePart {
        private Wall() {
            symbol = "#";
        }

        @Override
        boolean blocks() {
            return true;
        }

        @Override
        boolean haveTurn() {
            return false;
        }
    }

    private class Open extends CavePart {
        private Open() {
            symbol = ".";
        }

        @Override
        boolean blocks() {
            return false;
        }

        @Override
        boolean haveTurn() {
            return false;
        }
    }

    private class Coordinate {
        int x;
        int y;

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isNextTo(int x, int y) {
            boolean xNextTo = Math.abs(this.x - x) == 1;
            boolean yNextTo = Math.abs(this.y - y) == 1;
            return xNextTo && yNextTo;
        }

        boolean isNextTo(Coordinate other) {
            boolean xNextTo = Math.abs(this.x - other.x) == 1;
            boolean yNextTo = Math.abs(this.y - other.y) == 1;
            return (xNextTo && !yNextTo) || (!xNextTo && yNextTo);
        }

        @Override
        public String toString() {
            return "[" + y + ", " + x + "]";
        }
    }
}

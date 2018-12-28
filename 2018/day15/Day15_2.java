package day15;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15_2 {
    public static void main(String[] args) {
        Day15_2 day = new Day15_2();
    }

    public Day15_2() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(Cave input) {
        int rounds = 0;
        while (!input.round()) {
            System.out.println("ROUND: " + rounds);
            input.print();
            rounds++;
            System.out.println("-----------------------------------");
        }

        int remainingHp = input.remainingHpInCave();
        return rounds + " * " + remainingHp + " = " + (rounds * remainingHp);
        // 208853 too low
        // 208856 too low
        // 210605 too low
        // 256128 nope
        // 255852 nope :(
        // 237745 from py
        // 251910 nope
        // 240033 ??
        // 246530 ??
        // 451520 nope
        // 254527 nope
    }

    private String part2(Cave input) {
        return "part2";
    }

    private Cave init() {
        //String path = "input.txt";
        //String path = "test.txt";
        String path = "test2.txt"; // 47 * 590 = 27730
        //String path = "test5.txt"; // 37 * 982 = 36334
        //String path = "test6.txt"; // 46 * 859 = 39514
        //String path = "test7.txt"; // 35 * 793 = 27755
        //String path = "test8.txt"; // 54 * 536 = 28944
        //String path = "test9.txt"; // 20 * 937 = 18740
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/day15/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return new Cave(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Cave {
        CavePart[][] caveParts;
        int x;
        int y;
        List<Player> players = new ArrayList<>();

        private Cave(@NotNull Stream<String> inputLines) {
            List<String> input = inputLines.collect(Collectors.toList());
            x = input.get(0).length();
            y = input.size();

            System.out.println("x = " + x + ", y = " + y);

            caveParts = new CavePart[y][x];

            int tmpy = 0;
            for (String line : input) {
                System.out.println(line);
                int tmpx = 0;
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
                            players.add(new Elf(tmpx, tmpy));
                            cavePart = new Open();
                            break;
                        case "G":
                            players.add(new Goblin(tmpx, tmpy));
                            cavePart = new Open();
                            break;
                        default:
                            System.out.println("What");
                    }
                    caveParts[tmpy][tmpx++] = cavePart;
                }
                tmpy++;
            }
            System.out.println();
            print();
            System.out.println();
        }

        // Return true if we are done!
        boolean round() {
            Comparator<Player> comparator = Comparator.comparingInt((Player p) -> p.y).thenComparingInt((Player p) -> p.x);
            List<Player> alivePlayers = players.stream().filter(player -> player.hp > 0).sorted(comparator).collect(Collectors.toList());

            for (final Player player : alivePlayers) {
                if (player.hp <= 0) {
                    continue;
                }

                System.out.println("play: " + player);
                List<Player> targets = alivePlayers.stream()
                        .filter(player1 -> !player1.symbol.equals(player.symbol))
                        .filter(player1 -> player1.hp > 0)
                        .collect(Collectors.toList());

                if (targets.isEmpty()) {
                    return true;
                }
                System.out.println("targets: " + targets);

                // Attack and be happy
                if (attack(player, targets)) {
                    continue;
                }

                targets = alivePlayers.stream()
                        .filter(player1 -> !player1.symbol.equals(player.symbol))
                        .filter(player1 -> player1.hp > 0)
                        .collect(Collectors.toList());

                move(player, targets);

                // Check if we can attack after move
                targets = alivePlayers.stream()
                        .filter(player1 -> !player1.symbol.equals(player.symbol))
                        .filter(player1 -> player1.hp > 0)
                        .collect(Collectors.toList());
                attack(player, targets);
                print();
            }
            return false;
        }

        final int maxSteps = Integer.MAX_VALUE - 1000000;

        private void move(Player player, List<Player> targets) {
            List<CoordinateStepToTarget> coordinateSteps = new ArrayList<>();
            for (Player target : targets) {
                CoordinateStepToTarget tmp = shortestPathToTarget(player, target);
                if (tmp != null) {
                    coordinateSteps.add(tmp);
                }
            }

            if (!coordinateSteps.isEmpty()) {
                Comparator<CoordinateStepToTarget> comparator = Comparator.comparingInt((CoordinateStepToTarget c) -> c.steps)
                        .thenComparingInt((CoordinateStepToTarget c) -> c.target.y)
                        .thenComparingInt((CoordinateStepToTarget c) -> c.target.x);
                coordinateSteps.sort(comparator);
                System.out.println("Pot coords: " + coordinateSteps);

                Coordinate minDisCoor = coordinateSteps.get(0).coordinate;

                if (minDisCoor.y < maxSteps && minDisCoor.x < maxSteps) {
                    System.out.println("Move: " + minDisCoor);
                    player.y = minDisCoor.y;
                    player.x = minDisCoor.x;
                }
            }
        }

        private CoordinateStepToTarget shortestPathToTarget(Player player, Player target) {
            int minSteps = maxSteps;
            Coordinate minDisCoor = new Coordinate(maxSteps, maxSteps);
            boolean[][] visited = new boolean[y][x];
            int[][] steps = new int[y][x];

            LinkedList<Coordinate> queue = new LinkedList<>();

            for (int tmpy = 0; tmpy < y; tmpy++) {
                for (int tmpx = 0; tmpx < x; tmpx++) {
                    steps[tmpy][tmpx] = maxSteps;
                    visited[tmpy][tmpx] = caveParts[tmpy][tmpx].blocks();
                }
            }
            for (Player player1 : players) {
                visited[player1.y][player1.x] = true;
            }
            for (int tmpy = 0; tmpy < y; tmpy++) {
                for (int tmpx = 0; tmpx < x; tmpx++) {
                    if (!visited[tmpy][tmpx]) {
                        queue.add(new Coordinate(tmpx, tmpy));
                    }
                }
            }
            System.out.println("Calc path to target: " + target);
            steps[target.y][target.x] = 0;

            if (!visited[target.y - 1][target.x]) {
                Coordinate tmpCoor = new Coordinate(target.x, target.y - 1);
                steps[tmpCoor.y][tmpCoor.x] = 1;
                //queue.add(tmpCoor);
            }

            if (!visited[target.y + 1][target.x]) {
                Coordinate tmpCoor = new Coordinate(target.x, target.y + 1);
                steps[tmpCoor.y][tmpCoor.x] = 1;
                //queue.add(tmpCoor);
            }

            if (!visited[target.y][target.x - 1]) {
                Coordinate tmpCoor = new Coordinate(target.x - 1, target.y);
                steps[tmpCoor.y][tmpCoor.x] = 1;
                //queue.add(tmpCoor);
            }

            if (!visited[target.y][target.x + 1]) {
                Coordinate tmpCoor = new Coordinate(target.x + 1, target.y);
                steps[tmpCoor.y][tmpCoor.x] = 1;
                //queue.add(tmpCoor);
            }

            Comparator<Coordinate> comparator = Comparator.comparingInt((Coordinate c) -> steps[c.y][c.x])
                    .thenComparingInt((Coordinate c) -> c.y)
                    .thenComparingInt((Coordinate c) -> c.x);
            while (!queue.isEmpty()) {
                queue.sort(comparator);
                //System.out.println("q:");
                //queue.stream().filter(c -> steps[c.y][c.x] != maxSteps).map((Coordinate c) -> c + ": " + steps[c.y][c.x]).forEach(System.out::println);
                Coordinate currCoor = queue.pop();

                visited[currCoor.y][currCoor.x] = true;

                if (steps[currCoor.y][currCoor.x] == minSteps) {
                    System.out.println("BREAK B:" + currCoor + " : " + steps[currCoor.y][currCoor.x]);
                    return null;
                }

                if (currCoor.isNextTo(player)) {
                    System.out.println("BREAK A:" + currCoor + " : " + steps[currCoor.y][currCoor.x]);
                    if (steps[currCoor.y][currCoor.x] < minSteps) {
                        minSteps = steps[currCoor.y][currCoor.x];
                        minDisCoor = currCoor;
                    }
                    return new CoordinateStepToTarget(minDisCoor, minSteps, target);
                }

                if (!visited[currCoor.y][currCoor.x - 1]) {
                    Coordinate tmpCoor = new Coordinate(currCoor.x - 1, currCoor.y);
                    steps[tmpCoor.y][tmpCoor.x] = steps[tmpCoor.y][tmpCoor.x] < steps[currCoor.y][currCoor.x] + 1 ?
                            steps[tmpCoor.y][tmpCoor.x] :
                            steps[currCoor.y][currCoor.x] + 1;
                }
                if (!visited[currCoor.y][currCoor.x + 1]) {
                    Coordinate tmpCoor = new Coordinate(currCoor.x + 1, currCoor.y);
                    steps[tmpCoor.y][tmpCoor.x] = steps[tmpCoor.y][tmpCoor.x] < steps[currCoor.y][currCoor.x] + 1 ?
                            steps[tmpCoor.y][tmpCoor.x] :
                            steps[currCoor.y][currCoor.x] + 1;
                }
                if (!visited[currCoor.y - 1][currCoor.x]) {
                    Coordinate tmpCoor = new Coordinate(currCoor.x, currCoor.y - 1);
                    steps[tmpCoor.y][tmpCoor.x] = steps[tmpCoor.y][tmpCoor.x] < steps[currCoor.y][currCoor.x] + 1 ?
                            steps[tmpCoor.y][tmpCoor.x] :
                            steps[currCoor.y][currCoor.x] + 1;
                }
                if (!visited[currCoor.y + 1][currCoor.x]) {
                    Coordinate tmpCoor = new Coordinate(currCoor.x, currCoor.y + 1);
                    steps[tmpCoor.y][tmpCoor.x] = steps[tmpCoor.y][tmpCoor.x] < steps[currCoor.y][currCoor.x] + 1 ?
                            steps[tmpCoor.y][tmpCoor.x] :
                            steps[currCoor.y][currCoor.x] + 1;
                }
            }
            System.out.println("BREAK C:");
            return null;
        }

        private boolean attack(Player player, List<Player> possibleTargets) {
            List<Player> adjacent = possibleTargets.stream().filter(player1 -> player1.isNextTo(player)).collect(Collectors.toList());
            if (!adjacent.isEmpty()) {
                Comparator<Player> comparator = Comparator.comparingInt((Player p) -> p.hp)
                        .thenComparingInt((Player p) -> p.y)
                        .thenComparingInt((Player p) -> p.x);
                Player targetLowestHp = adjacent.stream().sorted(comparator).findFirst().get();
                targetLowestHp.hp -= player.attack;
                //System.out.println("attack! " + targetLowestHp);
                if (targetLowestHp.hp <= 0) {
                    //System.out.println(targetLowestHp + " is dead");
                }
                return true;
            }
            return false;
        }

        int remainingHpInCave() {
            return players.stream().filter(player -> player.hp > 0).mapToInt(player -> player.hp).sum();
        }

        void print() {
            for (int tmpy = 0; tmpy < y; tmpy++) {
                for (int tmpx = 0; tmpx < x; tmpx++) {
                    final int tx = tmpx;
                    final int ty = tmpy;
                    if (players.stream().anyMatch(player -> player.x == tx && player.y == ty && player.hp > 0)) {
                        System.out.print(players.stream().filter(player -> player.x == tx && player.y == ty).findFirst().get().symbol);
                    } else {
                        System.out.print(caveParts[tmpy][tmpx].symbol);
                    }
                }
                System.out.println();
            }
            System.out.println();
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
        int x;
        int y;

        private Player(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        boolean blocks() {
            return true;
        }

        @Override
        boolean haveTurn() {
            return true;
        }

        @Override
        public String toString() {
            return "[" + y + "," + x + "]: " + symbol + " (" + hp + ")";
        }

        boolean isNextTo(Player player) {
            boolean above = this.x == player.x && this.y == player.y - 1;
            boolean below = this.x == player.x && this.y == player.y + 1;
            boolean left = this.x == player.x - 1 && this.y == player.y;
            boolean right = this.x == player.x + 1 && this.y == player.y;
            return above || below || left || right;
        }
    }

    private class Elf extends Player {
        private Elf(int x, int y) {
            super(x, y);
            symbol = "E";

        }
    }

    private class Goblin extends Player {
        private Goblin(int x, int y) {
            super(x, y);
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

        boolean isNextTo(Player player) {
            boolean above = this.x == player.x && this.y == player.y - 1;
            boolean below = this.x == player.x && this.y == player.y + 1;
            boolean left = this.x == player.x - 1 && this.y == player.y;
            boolean right = this.x == player.x + 1 && this.y == player.y;
            return above || below || left || right;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Coordinate) {
                Coordinate c = (Coordinate) o;
                return c.x == this.x && c.y == this.y;
            }
            return false;

        }
    }

    class CoordinateStepToTarget {
        Coordinate coordinate;
        int steps;
        Player target;

        public CoordinateStepToTarget(Coordinate coordinate, int steps, Player target) {
            this.coordinate = coordinate;
            this.steps = steps;
            this.target = target;
        }

        @Override
        public String toString() {
            return coordinate.toString() + ": " + steps;
        }
    }
}

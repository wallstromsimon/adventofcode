package day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    }

    private String part2(Cave input) {
        return "part2";
    }

    private Cave init() {
        String path = "test2.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/day15/" + path;
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
        List<Player> players = new ArrayList<>();

        private Cave(Stream<String> inputLines) {
            List<String> input = inputLines.collect(Collectors.toList());
            x = input.get(0).split("").length;
            y = input.size();

            //System.out.println("x: " + x + ", y: " + y);
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
                            players.add(new Elf(j, i));
                            cavePart = new Open();
                            break;
                        case "G":
                            players.add(new Goblin(j, i));
                            cavePart = new Open();
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

        // Return true if we are done!
        boolean round() {
            Comparator<Player> comparator = Comparator.comparingInt((Player p) -> p.y).thenComparingInt((Player p) -> p.x);

            List<Player> alivePlayers = players.stream().filter(player -> player.hp > 0).sorted(comparator).collect(Collectors.toList());

            for (Player player : alivePlayers) {
                if (player.hp <= 0) {
                    continue;
                }

                System.out.println("play: " + player);
                // Check if there are targets at all?
                List<Player> targets = players.stream()
                        .filter(player1 -> !player1.symbol.equals(player.symbol))
                        .filter(player1 -> player1.hp > 0)
                        .collect(Collectors.toList()); //getInRange(player);
                if (!targets.isEmpty()) {
                    System.out.println("targets: " + targets);

                    boolean attacked = attack(player, targets);
                    if (attacked) {
                        continue;
                    }

                    move(player, targets);

                    // Check if we can attack after move
                    targets = players.stream()
                            .filter(player1 -> !player1.symbol.equals(player.symbol))
                            .filter(player1 -> player1.hp > 0)
                            .collect(Collectors.toList());
                    boolean attacked2 = attack(player, targets);
                    //print();
                } else {
                    System.out.println("DONE?");
                    return true;
                }
            }
            return false;
        }

        private void move(Player player, List<Player> targets) {
            // Move one step closer to the closest target, reading style
            // Move backwards and populate dynamically? Choose first (reading) with lowest number.
            int maxSteps = Integer.MAX_VALUE - 1000000;
            int minSteps = maxSteps;
            Coordinate minDisCoor = new Coordinate(maxSteps, maxSteps);
            boolean[][] visited = new boolean[y][x];
            int[][] steps = new int[y][x];
            for (int k = 0; k < y; k++) {
                for (int l = 0; l < x; l++) {
                    steps[k][l] = maxSteps;
                    visited[k][l] = caveParts[k][l].blocks();
                }
            }
            visited[player.y][player.x] = true;
            for (Player player1 : players) {
                visited[player1.y][player1.x] = true;
            }

            LinkedList<Coordinate> queue = new LinkedList<>();
            for (Player target : targets) {
                // Don't do it for all targets, set all targets to 0 and add all neighbours to queue!
                // Nope! Just add targets, set them to 0.
                steps[target.y][target.x] = 0;
                visited[target.y][target.x] = true;

                Coordinate left = new Coordinate(target.x - 1, target.y);
                if (!isBlocked(left.x, left.y)) {
                    queue.add(left);
                }
                Coordinate right = new Coordinate(target.x + 1, target.y);
                if (!isBlocked(right.x, right.y)) {
                    queue.add(right);
                }
                Coordinate below = new Coordinate(target.x, target.y - 1);
                if (!isBlocked(below.x, below.y)) {
                    queue.add(below);
                }
                Coordinate above = new Coordinate(target.x, target.y + 1);
                if (!isBlocked(above.x, above.y)) {
                    queue.add(above);
                }
            }

            while (!queue.isEmpty()) {
                //System.out.println("q: " + queue);

                Comparator<Coordinate> comparator = Comparator.comparingInt((Coordinate c) -> steps[c.x][c.y]);
                queue.sort(comparator);
                Coordinate currCoor = queue.pop();
                visited[currCoor.y][currCoor.x] = true;



                // Take shortst +1 from neighbours
                // OR steps[currCoor.y][currCoor.x] if that's already lower!!!!!!
                int tmp = 1 + IntStream.of(steps[currCoor.y - 1][currCoor.x], steps[currCoor.y + 1][currCoor.x], steps[currCoor.y][currCoor.x - 1], steps[currCoor.y][currCoor.x + 1]).min().getAsInt();
                steps[currCoor.y][currCoor.x] = steps[currCoor.y][currCoor.x] < tmp ? steps[currCoor.y][currCoor.x] : tmp;

                // Calc neighbour path instead,

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

                // Stop if we are on "player"
                if (currCoor.isNextTo(player)) {
                    //System.out.println("pot curr: " + currCoor + " " + steps[currCoor.y][currCoor.x]);
                    // if this is has shorter path or is earlier in read order
                    if (steps[currCoor.y][currCoor.x] < minSteps) {
                        minSteps = steps[currCoor.y][currCoor.x];
                        minDisCoor = currCoor;
                    } else if (steps[currCoor.y][currCoor.x] == minSteps && (currCoor.y < minDisCoor.y || (currCoor.y <= minDisCoor.y
                            && currCoor.x < minDisCoor.x))) {
                        minDisCoor = currCoor;
                    }
                    //break; // hmmmm....
                }
            }
            if (minSteps < maxSteps && minDisCoor.x < maxSteps && minDisCoor.y < maxSteps) {
                System.out.println("Move: " + minDisCoor);
                player.y = minDisCoor.y;
                player.x = minDisCoor.x;
            }
        }

        private boolean attack(Player player, List<Player> possibleTargets) {
            // Attack "first" if next to;
            List<Player> adjacent = possibleTargets.stream().filter(player1 -> player1.isNextTo(player)).collect(Collectors.toList());
            if (!adjacent.isEmpty()) { // Attack!
                Player targetLowestHp = adjacent.stream().min(Comparator.comparing(player1 -> player.hp)).get();
                targetLowestHp.hp -= player.attack;
                System.out.println("attack! " + targetLowestHp);
                if (targetLowestHp.hp <= 0) {
                    System.out.println(targetLowestHp + " is dead");
                }
                return true;
            }
            return false;
        }

        List<Coordinate> getInRange(Player player) {
            List<Coordinate> coordinates = new ArrayList<>();
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    CavePart target = caveParts[i][j];
                    if (target.haveTurn() && !player.symbol.equals(target.symbol)) {
                        coordinates.add(new Coordinate(j, i));
                    }
                }
            }

            return coordinates;
        }

        boolean isBlocked(int x, int y) {
            return caveParts[y][x].blocks() || players.stream().anyMatch(player1 -> player1.x == x && player1.y == y);
        }

        int remainingHpInCave() {
            return players.stream().filter(player -> player.hp > 0).mapToInt(player -> player.hp).sum();
        }

        void print() {
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    final int tx = j;
                    final int ty = i;
                    if (players.stream().anyMatch(player -> player.x == tx && player.y == ty && player.hp > 0)) {
                        System.out.print(players.stream().filter(player -> player.x == tx && player.y == ty).findFirst().get().symbol);
                    } else {
                        System.out.print(caveParts[i][j].symbol);
                    }
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
            // Can be simplified but I'm not that smart?
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
            return "[" + y + ", " + x + "]";
        }
    }
}

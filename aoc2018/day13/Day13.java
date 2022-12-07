import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day13 {
    public static void main(String[] args) {
        Day13 day = new Day13();
    }

    public Day13() {
        var input = init();

        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(init()));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(String[][] input) {
        List<Cart> carts = getCarts(input);
        Comparator<Cart> comparator = Comparator.comparingInt((Cart c) -> c.y).thenComparingInt((Cart c) -> c.x);

        while (true) {
            carts.sort(comparator);
            for (Cart cart : carts) {
                move(input, cart);
                if (carts.stream().filter(cart1 -> cart.x == cart1.x && cart.y == cart1.y).count() > 1) {
                    return cart.x + "," + cart.y;
                }
            }
        }
    }

    private void move(String[][] input, Cart cart) {
        // Move in the right direction
        switch (cart.dir) {
            case SOUTH:
                cart.y++;
                break;
            case NORTH:
                cart.y--;
                break;
            case EAST:
                cart.x++;
                break;
            case WEST:
                cart.x--;
                break;
        }
        String next = input[cart.x][cart.y];

        // Change direction if a corner or a crossing
        if ("/".equals(next)) {
            if (cart.dir == Dir.EAST || cart.dir == Dir.WEST) {
                cart.dir = cart.dir.turnLeft();
            } else {
                cart.dir = cart.dir.turnRight();
            }
        } else if ("\\".equals(next)) {
            if (cart.dir == Dir.EAST || cart.dir == Dir.WEST) {
                cart.dir = cart.dir.turnRight();
            } else {
                cart.dir = cart.dir.turnLeft();
            }
        } else if ("+".equals(next)) {
            cart.dir = cart.turn.getNewDir(cart.dir);
            cart.turn = cart.turn.nextTurn();
        }
    }

    private String part2(String[][] input) {
        List<Cart> carts = getCarts(input);
        Comparator<Cart> comparator = Comparator.comparingInt((Cart c) -> c.y).thenComparingInt((Cart c) -> c.x);
        while (true) {
            carts.sort(comparator);
            for (Cart cart : carts.stream().filter(cart -> !cart.crashed).collect(Collectors.toList())) {
                move(input, cart);
                if (carts.stream().filter(cart1 -> cart.x == cart1.x && cart.y == cart1.y && !cart.crashed && !cart1.crashed).count() > 1) {
                    carts.stream().filter(cart1 -> cart.x == cart1.x && cart.y == cart1.y).forEach(cart1 -> cart1.crashed = true);
                }
            }
            if (carts.stream().filter(cart -> !cart.crashed).collect(Collectors.toList()).size() == 1) {
                Cart cart = carts.stream().filter(c -> !c.crashed).findFirst().get();
                return cart.x + "," + cart.y;
            }
        }
        // 71,123 <---
    }

    private List<Cart> getCarts(String[][] input) {
        List<Cart> carts = new ArrayList<>();
        for (int y = 0; y < input[0].length; y++) {
            for (int x = 0; x < input.length; x++) {
                String curr = input[x][y];
                if ("v".equals(curr) || "^".equals(curr) || ">".equals(curr) || "<".equals(curr)) {
                    // Create cart
                    Cart cart = new Cart();
                    cart.x = x;
                    cart.y = y;
                    cart.dir = Dir.getDir(curr);
                    carts.add(cart);

                    // Fill in the right piece where the Cart where
                    String replacement = "NOPE";
                    switch (curr) {
                        case ("v"):
                        case ("^"):
                            replacement = "|";
                            break;
                        case (">"):
                        case ("<"):
                            replacement = "-";
                    }
                    input[x][y] = replacement;
                }
            }
        }
        return carts;
    }

    private String[][] init() {
        String path = "input.txt";
        //String path = "test.txt";
        //String path = "test2.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            List<String> lines = stream.collect(Collectors.toList());
            String[][] in = new String[lines.stream().mapToInt(String::length).max().getAsInt()][lines.size()];
            int x, y = 0;
            for (String line : lines) {
                x = 0;
                for (String c : line.split("")) {
                    in[x++][y] = c;
                }
                y++;
            }
            return in;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void print(String[][] strings) {
        for (int y = 0; y < strings[0].length; y++) {
            for (int x = 0; x < strings.length; x++) {
                System.out.print(strings[x][y]);
            }
            System.out.println();
        }
    }

    private void print(String[][] strings, List<Cart> carts) {
        for (int y = 0; y < strings[0].length; y++) {
            for (int x = 0; x < strings.length; x++) {
                final int tx = x;
                final int ty = y;
                Cart c = carts.stream().filter(cart -> cart.x == tx && cart.y == ty).findFirst().orElse(null);
                if (c != null) {
                    if (carts.stream().filter(cart -> cart.x == tx && cart.y == ty).count() > 1) {
                        System.out.print("X");
                    } else {
                        System.out.print(c.dir.getDirSymbol());
                    }
                } else {
                    System.out.print(strings[x][y]);
                }
            }
            System.out.println();
        }
    }

    class Cart {
        int x;
        int y;
        Dir dir = null;
        Turn turn = Turn.LEFT;
        boolean crashed = false;

        @Override
        public String toString() {
            return "[" + x + "," + y + "]: " + dir;
        }
    }

    enum Dir {
        NORTH("^"),
        EAST ("<"),
        SOUTH ("v"),
        WEST (">");

        private String dirSymbol;

        Dir(String dirSymbol) {
            this.dirSymbol = dirSymbol;
        }

        public Dir turnRight() {
            return Dir.values()[(this.ordinal() + 1) % Dir.values().length];
        }

        public Dir turnLeft() {
            return Dir.values()[(this.ordinal() + 3) % Dir.values().length];
        }

        public static Dir getDir(String s) {
            switch (s) {
                case ("^"):
                    return NORTH;
                case ("v"):
                    return SOUTH;
                case (">"):
                    return EAST;
                case ("<"):
                    return WEST;
            }
            return null;
        }

        public String getDirSymbol() {
            return dirSymbol;
        }
    }

    enum Turn {
        LEFT, STRAIGHT, RIGHT;

        public Dir getNewDir(Dir dir) {
            if (this == Turn.STRAIGHT) {
                return dir;
            } else if (this == Turn.LEFT) {
                return dir.turnLeft();
            } else {
                return dir.turnRight();
            }
        }

        public Turn nextTurn() {
            return Turn.values()[(this.ordinal() + 1) % Turn.values().length];
        }
    }
}

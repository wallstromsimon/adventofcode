import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day05 {
    public static void main(String[] args) {
        Day05 day = new Day05();
    }

    public Day05() {
        var input = init();
        System.out.println("Part one: " + part1(input));
        System.out.println("Part two: " + part2(input));
    }

    private int part1(String input) {
        return reactPoly(input);
    }

    private int reactPoly(String input) {
        LinkedList<String> stack = new LinkedList<>();
        for (String s : input.split("")) {
            if (s.equalsIgnoreCase(stack.peek()) && !s.equals(stack.peek())) { // (Math.abs((int) c - (int) c1) == 32)
                stack.pop();
            } else {
                stack.push(s);
            }
        }

        return stack.size();
    }

    private int part2(String input) {
        return IntStream.range(1, 26).map(i -> removeAndReactPoly(input, i)).min().getAsInt();
    }

    private int removeAndReactPoly(String input, int i) {
        return reactPoly(input.replaceAll((char) ('a' + i) + "|" + (char) ('A' + i), ""));
    }

    private String init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.findFirst().get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

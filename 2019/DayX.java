import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class DayX {
    public static void main(String[] args) {
        DayX day = new DayX();
    }

    public DayX() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(List<String> input) {
        return "part1";
    }

    private String part2(List<String> input) {
        return "part2";
    }

    private List<String> init(){
        String path = "input.txt";
        //String path = "input_test.txt";
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

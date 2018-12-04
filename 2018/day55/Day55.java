import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day55 {
    public static void main(String[] args) {
        Day55 day = new Day55();
    }

    public Day55() {
        var input = init();
        System.out.println("Part one: " + part1(input));
        System.out.println("Part two: " + part2(input));
    }

    private String part1(List<String> input) {
        return "part1";
    }

    private String part2(List<String> input) {
        return "part1";
    }

    private List<String> init(){
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

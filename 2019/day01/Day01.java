import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day01 {
    public static void main(String[] args) {
        Day01 day = new Day01();
    }

    public Day01() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input)); // 3382284 is correct
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input)); // 5070541 is correct
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    /**
     * Fuel required to launch a given module is based on its mass.
     * Specifically, to find the fuel required for a module, take its mass, divide by three, round down, and subtract 2.
     */
    private String part1(List<Long> input) {
        long sum = 0L;
        for (Long module : input) {
            long fuel = calcFuel(module);
            sum += fuel;
        }
        return "part1: " + sum;
    }

    private long calcFuel(long in) {
        return Math.floorDiv(in, 3) - 2;
    }

    private String part2(List<Long> input) {
        long sum = 0L;
        for (Long module : input) {
            long totalFuel = 0L;
            long fuel = calcFuel(module);
            totalFuel += fuel;
            long extraFuel = calcFuel(fuel);
            while (extraFuel > 0) {
                totalFuel += extraFuel;
                extraFuel = calcFuel(extraFuel);
            }
            sum += totalFuel;
        }
        return "part2: " + sum;
    }

    private List<Long> init() {
        String path = "input.txt";
        //String path = "input_test.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.map(Long::parseLong).collect(Collectors.toList());//forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}

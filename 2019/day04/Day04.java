import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day04 {
    public static void main(String[] args) {
        Day04 day = new Day04();
    }

    public Day04() {
        long min = 307237;
        long max = 769058;
        // 307237-769058
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(min, max));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(min, max));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(long min, long max) {
        return "part1";
    }

    private String part2(long min, long max) {
        return "part2";
    }
}

import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day02 {
    public static void main(String[] args) {
        Day02 day = new Day02();
    }

    public Day02() {
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(init())); // 4690667
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2()); // 6255
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(List<Integer> input) {
        runIntProg(input);
        return "part1 val at pos 0: " + input.get(0);
    }

    private void runIntProg(List<Integer> input) {
        for (int i = 0; i < input.size(); i += 4) {
            int op = input.get(i);
            if (op == 99) {
                //System.out.println("exit program i = " + i);
                break;
            }
            int first = input.get(i + 1);
            int second = input.get(i + 2);
            int to = input.get(i + 3);

            if (op == 1) {
                input.set(to, input.get(first) + input.get(second));
            } else if (op == 2){
                input.set(to, input.get(first) * input.get(second));
            } else {
                System.out.println("Unknown op = " + op);
            }
        }
    }

    private String part2() {
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                List<Integer> input = init();
                input.set(1, noun);
                input.set(2, verb);
                runIntProg(input);
                if (input.get(0) == 19690720) {
                    return "part2: " + ((100 * noun) + verb);
                }
            }

        }
        return "part2";
    }

    private List<Integer> init() {
        String input = "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,9,19,1,19,5,23,1,13,23,27,1,27,6,31,2,31,6,35,2,6,35,39,1,39,5,43,1,13,43,47,1,6,47,51,2,13,51,55,1,10,55,59,1,59,5,63,1,10,63,67,1,67,5,71,1,71,10,75,1,9,75,79,2,13,79,83,1,9,83,87,2,87,13,91,1,10,91,95,1,95,9,99,1,13,99,103,2,103,13,107,1,107,10,111,2,10,111,115,1,115,9,119,2,119,6,123,1,5,123,127,1,5,127,131,1,10,131,135,1,135,6,139,1,10,139,143,1,143,6,147,2,147,13,151,1,5,151,155,1,155,5,159,1,159,2,163,1,163,9,0,99,2,14,0,0";
        String inputAdjusted = "1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,9,19,1,19,5,23,1,13,23,27,1,27,6,31,2,31,6,35,2,6,35,39,1,39,5,43,1,13,43,47,1,6,47,51,2,13,51,55,1,10,55,59,1,59,5,63,1,10,63,67,1,67,5,71,1,71,10,75,1,9,75,79,2,13,79,83,1,9,83,87,2,87,13,91,1,10,91,95,1,95,9,99,1,13,99,103,2,103,13,107,1,107,10,111,2,10,111,115,1,115,9,119,2,119,6,123,1,5,123,127,1,5,127,131,1,10,131,135,1,135,6,139,1,10,139,143,1,143,6,147,2,147,13,151,1,5,151,155,1,155,5,159,1,159,2,163,1,163,9,0,99,2,14,0,0";
        String test0 = "1,0,0,0,99"; // becomes 2,0,0,0,99 (1 + 1 = 2).
        String test1 = "2,3,0,3,99"; // becomes 2,3,0,6,99 (3 * 2 = 6).
        String test2 = "2,4,4,5,99,0"; // becomes 2,4,4,5,99,9801 (99 * 99 = 9801).
        String test3 = "1,1,1,4,99,5,6,0,99"; // becomes 30,1,1,4,2,5,6,0,99.
        return Arrays.asList(inputAdjusted.split(",")).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
    }
}

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 {
    public static void main(String[] args) {
        Day05 day = new Day05();
    }

    public Day05() {
        String input = "3,225,1,225,6,6,1100,1,238,225,104,0,2,171,209,224,1001,224,-1040,224,4,224,102,8,223,223," +
                "1001,224,4,224,1,223,224,223,102,65,102,224,101,-3575,224,224,4,224,102,8,223,223,101,2,224,224,1," +
                "223,224,223,1102,9,82,224,1001,224,-738,224,4,224,102,8,223,223,1001,224,2,224,1,223,224,223,1101," +
                "52,13,224,1001,224,-65,224,4,224,1002,223,8,223,1001,224,6,224,1,223,224,223,1102,82,55,225,1001," +
                "213,67,224,1001,224,-126,224,4,224,102,8,223,223,1001,224,7,224,1,223,224,223,1,217,202,224,1001," +
                "224,-68,224,4,224,1002,223,8,223,1001,224,1,224,1,224,223,223,1002,176,17,224,101,-595,224,224,4," +
                "224,102,8,223,223,101,2,224,224,1,224,223,223,1102,20,92,225,1102,80,35,225,101,21,205,224,1001," +
                "224,-84,224,4,224,1002,223,8,223,1001,224,1,224,1,224,223,223,1101,91,45,225,1102,63,5,225,1101," +
                "52,58,225,1102,59,63,225,1101,23,14,225,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999," +
                "1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105," +
                "1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0," +
                "0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999," +
                "1008,677,677,224,1002,223,2,223,1006,224,329,101,1,223,223,1108,226,677,224,1002,223,2,223,1006," +
                "224,344,101,1,223,223,7,677,226,224,102,2,223,223,1006,224,359,1001,223,1,223,8,677,226,224,102," +
                "2,223,223,1005,224,374,1001,223,1,223,1107,677,226,224,102,2,223,223,1006,224,389,1001,223,1,223," +
                "1008,226,226,224,1002,223,2,223,1005,224,404,1001,223,1,223,7,226,677,224,102,2,223,223,1005,224," +
                "419,1001,223,1,223,1007,677,677,224,102,2,223,223,1006,224,434,1001,223,1,223,107,226,226,224,1002," +
                "223,2,223,1005,224,449,1001,223,1,223,1008,677,226,224,102,2,223,223,1006,224,464,1001,223,1,223," +
                "1007,677,226,224,1002,223,2,223,1005,224,479,1001,223,1,223,108,677,677,224,1002,223,2,223,1006," +
                "224,494,1001,223,1,223,108,226,226,224,1002,223,2,223,1006,224,509,101,1,223,223,8,226,677,224,102," +
                "2,223,223,1006,224,524,101,1,223,223,107,677,226,224,1002,223,2,223,1005,224,539,1001,223,1,223,8," +
                "226,226,224,102,2,223,223,1005,224,554,101,1,223,223,1108,677,226,224,102,2,223,223,1006,224,569," +
                "101,1,223,223,108,677,226,224,102,2,223,223,1006,224,584,1001,223,1,223,7,677,677,224,1002,223,2," +
                "223,1005,224,599,101,1,223,223,1007,226,226,224,102,2,223,223,1005,224,614,1001,223,1,223,1107,226," +
                "677,224,102,2,223,223,1006,224,629,101,1,223,223,1107,226,226,224,102,2,223,223,1005,224,644,1001," +
                "223,1,223,1108,677,677,224,1002,223,2,223,1005,224,659,101,1,223,223,107,677,677,224,1002,223,2,223," +
                "1006,224,674,1001,223,1,223,4,223,99,226";
        List<Integer> integers = Arrays.asList(input.split(",")).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(integers)); // 9006673
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(integers));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(List<Integer> input) {
        runIntProg(input);
        return "part1";
    }

    private void runIntProg(List<Integer> input) {
        int indexDiff = 0;
        for (int i = 0; i < input.size(); i += indexDiff) {
            System.out.println(input.subList(i, input.size()));
            int instruction = input.get(i);
            System.out.println("Processing instruction: " + instruction);
            List<Integer> instMode = Arrays.stream(Integer.toString(instruction).split(""))
                    //.filter(s -> !"-".equals(s)) // should not happen for a real op code?
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    //.sorted(Collections.reverseOrder())
                    .collect(Collectors.toList());
            Collections.reverse(instMode);
            System.out.println("instList: " + instMode);
            int op = instMode.get(0);
            op = instMode.size() > 1 ? instMode.get(1) * 10 + op : op;
            System.out.println("OP: " + op);
            int opMode1 = instMode.size() > 2 ? instMode.get(2) : 0;
            int opMode2 = instMode.size() > 3 ? instMode.get(3) : 0;
            int opMode3 = instMode.size() > 4 ? instMode.get(4) : 0;
            if (opMode3 > 0) {
                System.out.println("whatsup with opmode3: " + opMode3);
            }

            if (op == 1) {
                int first = input.get(i + 1);
                int second = input.get(i + 2);
                int to = input.get(i + 3);

                first = opMode1 == 0 ? input.get(first) : first;
                second = opMode2 == 0 ? input.get(second) : second;

                input.set(to, first + second);
                indexDiff = 4;
            } else if (op == 2) {
                int first = input.get(i + 1);
                int second = input.get(i + 2);
                int to = input.get(i + 3);

                first = opMode1 == 0 ? input.get(first) : first;
                second = opMode2 == 0 ? input.get(second) : second;

                input.set(to, first * second);
                indexDiff = 4;
            } else if (op == 3) {
                // Opcode 3 takes a single integer as input and saves it to the position given by its only parameter.
                // For example, the instruction 3,50 would take an input value and store it at address 50.
                System.out.println("Only one input, giving it 1");
                int address = input.get(i + 1);
                input.set(address, 1);
                // do something!
                indexDiff = 2;
            } else if (op == 4) {
                // Opcode 4 outputs the value of its only parameter.
                // For example, the instruction 4,50 would output the value at address 50.
                int address = input.get(i + 1);
                int value = opMode1 == 0 ? input.get(address) : address;
                // do something!
                System.out.println("OUT: " + value);
                if (value != 0) {
                    System.out.println("Breaking for non 0 output, are we done or is something wrong?");
                    //break;
                }
                indexDiff = 2;
            } else if (op == 99) {
                //System.out.println("exit program i = " + i);
                break;
            } else {
                System.out.println("Unknown op = " + op);
            }
        }
    }

    private String part2(List<Integer> input) {
        return "part2";
    }

}

import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day16 {
    public static void main(String[] args) {
        Day16 day = new Day16();
    }

    public Day16() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        input = init();
        System.out.println("Part two: " + part2(input));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private long part1(List<Sample> input) {
        //input.forEach(sample -> sample.print());

        // Set up list of opcodes
        List<Op> ops = getOps();

        for (Sample sample : input) {
            for (Op op : ops) {
                List<Integer> tmpList = new ArrayList<>(sample.before);
                op.execute(tmpList, sample.A, sample.B, sample.C);
                if (tmpList.equals(sample.after)) {
                    //System.out.println(sample.before + " => " + sample.after);
                    sample.nbrWorkingOpCodes++;
                }
            }
        }
        return input.stream().filter(sample -> sample.nbrWorkingOpCodes >= 3).count();
    }

    private List<Op> getOps() {
        List<Op> ops = new ArrayList<>();
        ops.add(new Addr());
        ops.add(new Addi());
        ops.add(new Multr());
        ops.add(new Multi());
        ops.add(new Banr());
        ops.add(new Bani());
        ops.add(new Borr());
        ops.add(new Bori());
        ops.add(new Setr());
        ops.add(new Seti());
        ops.add(new Gtir());
        ops.add(new Gtri());
        ops.add(new Gtrr());
        ops.add(new Eqir());
        ops.add(new Eqri());
        ops.add(new Eqrr());
        return ops;
    }

    private String part2(List<Sample> input) {
        List<Op> ops = getOps();
        Map<Integer, Set<Op>> opSetMap = new HashMap<>();

        for (int i = 1; i <= 16; i++) {
            final int opNbr = i;
            List<Sample> samples = input.stream().filter(sample -> sample.opNbr == opNbr).collect(Collectors.toList());

            for (Op op : ops) {
                boolean matches = true;
                for (Sample sample : samples) {
                    List<Integer> tmpList = new ArrayList<>(sample.before);
                    op.execute(tmpList, sample.A, sample.B, sample.C);
                    if (!tmpList.equals(sample.after)) {
                        matches = false;
                        break;
                    }
                }

                if (matches) {
                    opSetMap.putIfAbsent(opNbr, new HashSet<>());
                    opSetMap.get(opNbr).add(op);
                    //System.out.println(opNbr + " = " + op.getClass().getSimpleName());
                }
            }
            // run through it agin for each and assume it fits? dont use it for any other op!
        }

        Map<Integer, Op> opMap = new HashMap<>();
        while (!opSetMap.isEmpty()) {
            Map.Entry<Integer, Set<Op>> entry = opSetMap.entrySet().stream().filter(e -> e.getValue().size() == 1).findAny().get();
            Op op = entry.getValue().stream().findFirst().get();
            opMap.put(entry.getKey(), op);
            System.out.println(entry.getKey() + " -> " + entry.getValue().stream().map(op2 -> op2.getClass().getSimpleName()).collect(Collectors.toList()));
            for (Map.Entry<Integer, Set<Op>> entry2 : opSetMap.entrySet()) {
                System.out.println(entry2.getKey() + " -> " + entry2.getValue().stream().map(op2 -> op2.getClass().getSimpleName()).collect(Collectors.toList()));
                entry2.getValue().remove(op);
                System.out.println(entry2.getKey() + " -> " + entry2.getValue().stream().map(op2 -> op2.getClass().getSimpleName()).collect(Collectors.toList()));
                System.out.println();
            }
            opSetMap.remove(entry.getKey());

        }
        for (Map.Entry<Integer, Set<Op>> entry : opSetMap.entrySet()) {

            /*Op tmpOp = entry.getValue()
                    .stream()
                    .filter(op -> 1 == opSetMap.values().stream().flatMap(Collection::stream).filter(op::equals).count())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("nope " + entry.getKey()));
            opMap.put(entry.getKey(), tmpOp);
            */
            System.out.println(entry.getKey() + " -> " + entry.getValue().stream().map(op -> op.getClass().getSimpleName()).collect(Collectors.toList()));
        }

        //System.out.println(opMap);
        List<Integer> reg = new ArrayList<>();

        List<Instruction> instructions = init2();

        return "part2";
    }

    private List<Sample> init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        List<Sample> samples = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            List<String> lines = stream.collect(Collectors.toList()); //.forEach(System.out::println);
            Sample tmpSample = null;
            for (String line : lines) {
                //System.out.println(line);
                if (line.isBlank()) {

                } else if (line.startsWith("Before")) {
                    tmpSample = new Sample();
                    tmpSample.before = Arrays.stream(line.substring(line.indexOf('[') + 1, line.indexOf(']')).split(", "))
                            .mapToInt(Integer::parseInt)
                            .boxed()
                            .collect(Collectors.toList());
                } else if (line.startsWith("After")) {
                    tmpSample.after = Arrays.stream(line.substring(line.indexOf('[') + 1, line.indexOf(']')).split(", "))
                            .mapToInt(Integer::parseInt)
                            .boxed()
                            .collect(Collectors.toList());
                    samples.add(tmpSample);
                    //tmpSample.print();
                } else {
                    String[] op = line.split(" ");
                    tmpSample.opNbr = Integer.parseInt(op[0]);
                    tmpSample.A = Integer.parseInt(op[1]);
                    tmpSample.B = Integer.parseInt(op[2]);
                    tmpSample.C = Integer.parseInt(op[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return samples;
    }

    private List<Instruction> init2() {
        String path = "input_2.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        List<Instruction> instructions = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            List<String> lines = stream.collect(Collectors.toList());

            for (String line : lines) {
                if (line.isBlank()) {

                } else {
                    Instruction tmpInstruction = new Instruction();
                    String[] op = line.split(" ");
                    tmpInstruction.opNbr = Integer.parseInt(op[0]);
                    tmpInstruction.A = Integer.parseInt(op[1]);
                    tmpInstruction.B = Integer.parseInt(op[2]);
                    tmpInstruction.C = Integer.parseInt(op[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instructions;
    }

    class Instruction {
        int opNbr;
        int A;
        int B;
        int C;
    }

    class Sample {
        List<Integer> before;
        List<Integer> after;
        int opNbr;
        int A;
        int B;
        int C;
        int nbrWorkingOpCodes = 0;

        private void print() {
            System.out.println(before);
            System.out.println(opNbr + " " + A + " " + B + " " + C);
            System.out.println(after);
            System.out.println();
        }
    }

    interface Op {
        void execute(List<Integer> reg, int A, int B, int C);
    }

    /**
     * addr (add register) stores into register C the result of adding register A and register B.
     */
    class Addr implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A) + reg.get(B));
        }
    }

    /**
     * addi (add immediate) stores into register C the result of adding register A and value B.
     */
    class Addi implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A) + B);
        }
    }

    /**
     * mulr (multiply register) stores into register C the result of multiplying register A and register B.
     */
    class Multr implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A) * reg.get(B));
        }
    }

    /**
     * muli (multiply immediate) stores into register C the result of multiplying register A and value B.
     */
    class Multi implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A) * B);
        }
    }

    /**
     * banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
     */
    class Banr implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A) & reg.get(B));
        }
    }

    /**
     * bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
     */
    class Bani implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A) & B);
        }
    }

    /**
     * borr (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
     */
    class Borr implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A) | reg.get(B));
        }
    }

    /**
     * bori (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.
     */
    class Bori implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A) | B);
        }
    }

    /**
     * setr (set register) copies the contents of register A into register C. (Input B is ignored.)
     */
    class Setr implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, reg.get(A));
        }
    }

    /**
     * seti (set immediate) stores value A into register C. (Input B is ignored.)
     */
    class Seti implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, A);
        }
    }

    /**
     * gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
     */
    class Gtir implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, (A > reg.get(B)) ? 1 : 0);
        }
    }

    /**
     * gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
     */
    class Gtri implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, (reg.get(A) > B) ? 1 : 0);
        }
    }

    /**
     * gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
     */
    class Gtrr implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, (reg.get(A) > reg.get(B)) ? 1 : 0);
        }
    }

    /**
     * eqir (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
     */
    class Eqir implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, (A == reg.get(B)) ? 1 : 0); // equals or auto unbox?
        }
    }

    /**
     * eqri (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
     */
    class Eqri implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, (reg.get(A) == B) ? 1 : 0);
        }
    }

    /**
     * eqrr (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
     */
    class Eqrr implements Op {
        @Override
        public void execute(List<Integer> reg, int A, int B, int C) {
            reg.set(C, (reg.get(A) == reg.get(B)) ? 1 : 0);
        }
    }
}

import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day07 {
    public static void main(String[] args) {
        Day07 day = new Day07();
    }

    public Day07() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(init()));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(List<Instruction> input) {
        StringBuilder done = new StringBuilder();

        List<String> avail = getStartAvail(input);

        while (!avail.isEmpty()) {
            Collections.sort(avail);

            String workString = avail.get(0);
            done.append(workString);

            List<String> e = input.stream()
                    .filter(instruction -> workString.equals(instruction.inst))
                    .map(instruction -> instruction.enables)
                    .collect(Collectors.toList());

            input.removeIf(instruction -> instruction.inst.equals(workString));
            avail.removeIf(instruction -> instruction.equals(workString));

            for (String s : e) {
                if (!input.stream().anyMatch(instruction -> s.equals(instruction.enables))) {
                    avail.add(s);
                }
            }
        }

        return done.toString();
    }

    private List<String> getStartAvail(List<Instruction> input) {
        List<String> avail = new LinkedList<>();
        List<String> start = input.stream()
                .filter(instruction -> !input.stream().anyMatch(instruction1 -> instruction1.enables.equals(instruction.inst)))
                .map(instruction -> instruction.inst)
                .distinct()
                .collect(Collectors.toList());

        avail.addAll(start);
        return avail;
    }

    private int part2(List<Instruction> input) {

        Map<Integer, String[]> secToWork = new HashMap<>();

        List<String> avail = getStartAvail(input);
        int currsec = 0;
        int workers = 5;
        secToWork.put(currsec, new String[workers]);
        Map<Integer, List<String>> enablesAtSec = new HashMap<>();

        while (!avail.isEmpty() || !enablesAtSec.isEmpty()) {
            if (!secToWork.containsKey(currsec)) {
                secToWork.put(currsec, new String[workers]);
            }

            if (currsec > 0){
                List<String> last = Arrays.asList(secToWork.get(currsec - 1));
                List<String> now = Arrays.asList(secToWork.get(currsec));
                input.removeIf(instruction -> last.contains(instruction.inst) && !now.contains(instruction.inst));
            }

            if (enablesAtSec.containsKey(currsec)) {
                List<String> e = enablesAtSec.get(currsec);
                enablesAtSec.remove(currsec);

                for (String s : e) {
                    if (!input.stream().anyMatch(instruction -> s.equals(instruction.enables))) {
                        avail.add(s);
                    }
                }
            }

            for (int i = 0; i < workers; i++) {
                if (secToWork.get(currsec)[i] == null && !avail.isEmpty()) {
                    Collections.sort(avail);
                    String workString = avail.get(0);

                    int workCompleteSec = currsec + workString.charAt(0) - 'A' + 1 + 60;

                    if (!enablesAtSec.containsKey(workCompleteSec)) {
                        enablesAtSec.put(workCompleteSec, new ArrayList<>());
                    }
                    avail.removeIf(instruction -> instruction.equals(workString));

                    enablesAtSec.get(workCompleteSec)
                            .addAll(input.stream()
                                    .filter(instruction -> workString.equals(instruction.inst))
                                    .map(instruction -> instruction.enables)
                                    .collect(Collectors.toList()));


                    for (int j = currsec; j < workCompleteSec; j++) { // +60
                        if (!secToWork.containsKey(j)) {
                            secToWork.put(j, new String[workers]);
                        }
                        secToWork.get(j)[i] = workString;
                    }
                }
            }

            //System.out.println(currsec + " " + Arrays.asList(secToWork.get(currsec)));
            currsec++;
        }

        return secToWork.size() - 1;
    }

    private List<Instruction> init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.map(Instruction::new).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private class Instruction {
        String inst;
        String enables;

        public Instruction(String line) {
            //System.out.println(line);
            inst = line.substring(5, 6);
            enables = line.substring(36, 37);
        }

        @Override
        public String toString() {
            return inst + " -> " + enables;
        }
    }
}

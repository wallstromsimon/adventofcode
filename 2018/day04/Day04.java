import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day04 {
    public static void main(String[] args) {
        Day04 day = new Day04();
    }

    public Day04() {
        List<String> log = init();
        //log.forEach(System.out::println);
        System.out.println("Part one: " + part1(log)); // 20859

        System.out.println("Part two: " + part2(log)); // 76576
    }

    private String part1(List<String> log) {
        Map<Integer, Sleep> sleep2 = getIntegerSleepMap(log);
        int sleepyId2 = Collections.max(sleep2.entrySet(), Comparator.comparingInt(entry -> entry.getValue().dur)).getKey();
        int mostMin = getMostSleptMin(sleep2, sleepyId2);
        return "" + sleepyId2 * mostMin;
    }

    private String part2(List<String> log) {
        Map<Integer, Sleep> sleep2 = getIntegerSleepMap(log);
        int sleepyId2 = Collections.max(sleep2.entrySet(), Comparator.comparingInt(entry -> IntStream.of(entry.getValue().minutes).max().getAsInt())).getKey();
        int mostMin = getMostSleptMin(sleep2, sleepyId2);
        return "" + sleepyId2 * mostMin;
    }

    private Map<Integer, Sleep> getIntegerSleepMap(List<String> log) {
        Map<Integer, Sleep> sleep2 = new HashMap<>();
        int currentGuardID = 0;
        int asleepMin = -1;
        for (String l : log) {
            if (l.contains("Guard")) {
                String tmp = l.substring(l.indexOf('#'));
                tmp = tmp.substring(1, tmp.indexOf(' '));
                currentGuardID = Integer.parseInt(tmp);
            } else {
                int min = Integer.parseInt(l.substring(l.indexOf(']') - 2, l.indexOf(']')));
                if (asleepMin == -1) {
                    asleepMin = min;
                } else {
                    Sleep tmpSleep;
                    if (sleep2.get(currentGuardID) == null) {
                        tmpSleep = new Sleep();
                        tmpSleep.minutes = new int[60];
                    } else {
                        tmpSleep = sleep2.get(currentGuardID);
                    }

                    int slept = min - asleepMin;
                    tmpSleep.dur += slept;

                    for (int i = asleepMin; i < min; i++) {
                        tmpSleep.minutes[i]++;
                    }

                    sleep2.put(currentGuardID, tmpSleep);
                    asleepMin = -1;
                }
            }
        }
        return sleep2;
    }

    private int getMostSleptMin(Map<Integer, Sleep> sleep2, int sleepyId2) {
        int mostSleep = IntStream.of(sleep2.get(sleepyId2).minutes).max().getAsInt();

        for (int i = 0; i < 60; i++) {
            if (sleep2.get(sleepyId2).minutes[i] == mostSleep) {
                return i;
            }
        }
        return -1;
    }

    private List<String> init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.sorted().collect(Collectors.toList());//.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return Collections.emptyList();
    }

    private class Sleep {
        int dur;
        int[] minutes;
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        System.out.println("Part one: " + part1(log)); // 20859

        System.out.println("Part two: " + part2(log)); // 76576
    }

    private int part1(List<String> log) {
        Map<Integer, Sleep> sleep = getIntegerSleepMap(log);
        int sleepyId = Collections.max(sleep.entrySet(), Comparator.comparingInt(entry -> entry.getValue().dur)).getKey();
        int mostMin = getMostSleptMin(sleep, sleepyId);
        return sleepyId * mostMin;
    }

    private int part2(List<String> log) {
        Map<Integer, Sleep> sleep = getIntegerSleepMap(log);
        int sleepyId = Collections.max(sleep.entrySet(), Comparator.comparingInt(entry -> IntStream.of(entry.getValue().minutes).max().getAsInt())).getKey();
        int mostMin = getMostSleptMin(sleep, sleepyId);
        return sleepyId * mostMin;
    }

    private Map<Integer, Sleep> getIntegerSleepMap(List<String> log) {
        Map<Integer, Sleep> sleep = new HashMap<>();
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
                    if (sleep.get(currentGuardID) == null) {
                        tmpSleep = new Sleep();
                        tmpSleep.minutes = new int[60];
                    } else {
                        tmpSleep = sleep.get(currentGuardID);
                    }

                    int slept = min - asleepMin;
                    tmpSleep.dur += slept;

                    for (int i = asleepMin; i < min; i++) {
                        tmpSleep.minutes[i]++;
                    }

                    sleep.put(currentGuardID, tmpSleep);
                    asleepMin = -1;
                }
            }
        }
        return sleep;
    }

    private int getMostSleptMin(Map<Integer, Sleep> sleep, int sleepyId) {
        int mostSleep = IntStream.of(sleep.get(sleepyId).minutes).max().getAsInt();

        for (int i = 0; i < 60; i++) {
            if (sleep.get(sleepyId).minutes[i] == mostSleep) {
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day01 {
    public static void main(String[] args) {
        Day01 day = new Day01();
    }

    public Day01() {
        List<Integer> frequencyChanges = init();
        System.out.println("Part one: " + frequencyChanges.stream().mapToInt(Integer::intValue).sum());

        Set<Integer> frequencies = new HashSet<>();
        int sum = 0;
        for (int i = 0;; i = (i+1) % frequencyChanges.size()) {
            Integer freq = frequencyChanges.get(i);
            sum += freq;
            if (frequencies.contains(sum)) {
               System.out.println("Part two: " + sum);
               break;
            } else {
                frequencies.add(sum);
            }
        }
    }
    
    private List<Integer> init(){ // inherit instead? :O
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.map(Integer::parseInt).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day02 {
    public static void main(String[] args) {
        Day02 day = new Day02();
    }

    public Day02() {
        var boxIds = init();
        System.out.println("Part one: " + part1(boxIds));
        System.out.println("Part two: " + part2(boxIds));
    }

    private String part1(Set<String> boxIds) {
        Map<Integer, Integer> check = new HashMap<>();
        check.put(2, 0);
        check.put(3, 0);

        for (String id : boxIds) {
            Map<String, Integer> counts = new HashMap<>();
            for (int i = 0; i < id.length(); i++) {
                String character = id.substring(i, i + 1);
                int count = counts.containsKey(character) ? counts.get(character) + 1 : 1;
                counts.put(character, count);
            }
            if (counts.containsValue(2)) {
                check.put(2, check.get(2) + 1);
            }
            if (counts.containsValue(3)) {
                check.put(3, check.get(3) + 1);
            }
        }

        return "" + check.get(2) * check.get(3);
    }

    private String part2(Set<String> boxIds) {
        for (String id : boxIds) {
            for (String innerId : boxIds) {
                if (id.equals(innerId)) {
                    continue;
                }
                boolean diff = false;
                boolean diff1 = false;
                int diffId = 0;
                for (int i = 0; i < id.length(); i++) {
                    String character = id.substring(i, i + 1);
                    String character1 = innerId.substring(i, i + 1);
                    boolean innerDiff = !character.equals(character1);
                    if (diff && innerDiff) {
                        diff1 = true;
                        break;
                    } else if (innerDiff) {
                        diff = true;
                        diffId = i;
                    }
                }
                if (diff && !diff1) {
                    StringBuilder sb = new StringBuilder(id);
                    sb.deleteCharAt(diffId);
                    return sb.toString();
                }
            }
        }
        return "";
    }

    private Set<String> init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }
}

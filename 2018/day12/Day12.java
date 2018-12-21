import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {
    public static void main(String[] args) {
        Day12 day = new Day12();
    }

    public Day12() {
        String initialState = "##.#.#.##..#....######..#..#...#.#..#.#.#..###.#.#.#..#..###.##.#..#.##.##.#.####..##...##..#..##.#.";
        List<Pot> pots = initPots(initialState);

        var rules = init();

        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(rules, pots));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(rules, pots));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private List<Pot> initPots(String initialState) {
        List<Pot> pots = new LinkedList<>();
        for (int i = -5; i < 0; i++) {
            pots.add(new Pot(i, false));
        }

        int index = 0;
        for (String pot : initialState.split("")) {
            pots.add(new Pot(index++, "#".equals(pot)));
        }

        for (int i = initialState.length(); i < initialState.length() + 50; i++) {
            pots.add(new Pot(i, false));
        }
        return pots;
    }

    private int part1(List<Rule> rules, List<Pot> pots) {
        for (int i = 0; i < 20; i++) {
            pots = getNextGenPots(rules, pots);
        }

        return pots.stream().filter(pot -> pot.hasPlant).mapToInt(pot -> pot.index).sum();
    }

    private List<Pot> getNextGenPots(List<Rule> rules, List<Pot> pots) {

        // Add 5 to start and to the end
        List<Pot> nextGenPots = new LinkedList<>();
        int firstIndex = pots.get(0).index;
        int lastIndex = pots.get(pots.size() -1).index;
        for (int i = 0; i < 5; i++) {
            pots.add(0, new Pot(--firstIndex, false));
            pots.add(new Pot(++lastIndex, false));
        }

        for (int i = 2; i < pots.size() - 2; i++) {
            List<Pot> potsToCheck = pots.subList(i - 2, i + 3);
            boolean shouldHavePlant = rules.stream().anyMatch(rule -> rule.check(potsToCheck));
            nextGenPots.add(new Pot(pots.get(i).index, shouldHavePlant));
        }

        // strip head and tail ...
        while (!nextGenPots.get(0).hasPlant){
            nextGenPots.remove(0);
        }
        while (!nextGenPots.get(nextGenPots.size() - 1).hasPlant) {
            nextGenPots.remove(nextGenPots.size() - 1);
        }

        return nextGenPots;
    }

    private long part2(List<Rule> rules, List<Pot> pots) {
        long l = 50000000000L;
        long lastSum = pots.stream().filter(pot -> pot.hasPlant).mapToLong(pot -> pot.index).sum();
        long lastDiff = lastSum;
        long sameDiffCount = 0;
        for (long i = 0; i < l; i++) {
            pots = getNextGenPots(rules, pots);
            long sum = pots.stream().filter(pot -> pot.hasPlant).mapToLong(pot -> pot.index).sum();
            long diff = sum - lastSum;
            lastSum = sum;

            if (diff == lastDiff) {
                sameDiffCount++;
            } else {
                lastDiff = diff;
                sameDiffCount = 0;
            }

            if (sameDiffCount > 100) {
                return sum + diff * (l-i-1);
            }
        }

        return Long.MIN_VALUE;
    }

    private List<Rule> init() {
        String path = "rules.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.map(Rule::new).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private class Pot {
        int index;
        boolean hasPlant;

        private Pot(int index, boolean hasPlant) {
            this.index = index;
            this.hasPlant = hasPlant;
        }

        @Override
        public String toString() {
            return (hasPlant ? "#" : ".");
        }
    }

    private class Rule {
        List<Boolean> combinations;
        boolean centerGrows;

        private Rule(String input) {
            combinations = new ArrayList<>();
            for (String pot : input.substring(0, input.indexOf(" ")).split("")) {
                combinations.add("#".equals(pot));
            }

            centerGrows = "#".equals(input.substring(input.lastIndexOf(" ") + 1));
        }

        private boolean check(List<Pot> pots) {
            //System.out.println("Checking " + pots + " on " + toString());
            if (pots.size() != combinations.size()) {
                System.out.println("Check: Size miss match");
                return false;
            }

            boolean matches = true;
            for (int i = 0; i < combinations.size(); i++) {
                if (!combinations.get(i).equals(pots.get(i).hasPlant)) {
                    matches = false;
                    break;
                }
            }
            return matches && centerGrows;
        }

        @Override
        public String toString() {
            return combinations.stream().map(pot -> (pot ? "#" : ".")).collect(Collectors.joining()) + " => " + (centerGrows ? "#" : ".");
        }
    }
}

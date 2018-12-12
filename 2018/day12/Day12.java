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
        //String initialState = "#..#.#..##......###...###"; // TEST
        System.out.println(initialState);
        List<Pot> pots = initPots(initialState);

        pots.forEach(System.out::print);
        System.out.println();

        pots.stream().map(pot -> pot.index + " ").forEach(System.out::print);
        System.out.println();

        var rules = init();
        //rules.forEach(System.out::println);

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
        pots.forEach(System.out::print);
        System.out.println();
        for (int i = 0; i < 20; i++) {
            System.out.println(i + " " + pots.size());
            pots = getNextGenPots(rules, pots);
            pots.forEach(System.out::print);
            System.out.println();
            pots.stream().map(pot -> pot.index + " ").forEach(System.out::print);
            System.out.println();
        }

        System.out.println("...#....#...#....#....#..#..##...#..#..##.##..##.##..##..##..##..##....#....#...#...#....#...#....#....#....#....#...............................");
        return pots.stream().filter(pot -> pot.hasPlant).mapToInt(pot -> pot.index).sum();
    }

    private List<Pot> getNextGenPots(List<Rule> rules, List<Pot> pots) {
        int initialPotSize = pots.size();
        List<Pot> nextGenPots = new LinkedList<>();
        /*
        List<Pot> firstPart = pots.subList(0, 5);
        boolean hasPlant = firstPart.stream().anyMatch(pot -> pot.hasPlant);
        if (hasPlant) {
            int lowestIndex = firstPart.get(0).index;
            // add empty to start
            System.out.println("adding at " + lowestIndex);
            for (int i = lowestIndex - 1; i > lowestIndex - 7; i--) {
                pots.add(0, new Pot(i, false));
            }
        }
        */

        for (int i = 0; i < 2; i++) {
            //List<Pot> potsToCheck = pots.subList(i, i + 5);
            //potsToCheck.forEach(System.out::print);
            //hasPlant = potsToCheck.stream().anyMatch(pot -> pot.hasPlant);
            //if (hasPlant) {
            //int index = hasPlant ? i + 5 : i;
            nextGenPots.add(new Pot(pots.get(i).index, pots.get(i).hasPlant));

            //}
            //boolean needsMore = i + 10 < pots.size() && pots.subList(i, i + 10).stream().anyMatch(pot -> pot.hasPlant);
            //if (needsMore) {
            //nextGenPots.add(new Pot(pots.get(i).index, pots.get(i).hasPlant));
            //}
        }

        boolean foundPlant = false;
        for (int i = 2; i < pots.size() - 2; i++) {
            List<Pot> potsToCheck = pots.subList(i - 2, i + 3);
            //potsToCheck.forEach(System.out::print);
            boolean shouldHavePlant = rules.stream().anyMatch(rule -> rule.check(potsToCheck));
            //System.out.println(" -> " + shouldHavePlant);
            //boolean hasPlant = potsToCheck.stream().anyMatch(pot -> pot.hasPlant);
            //if (shouldHavePlant || hasPlant || foundPlant) {
                nextGenPots.add(new Pot(pots.get(i).index, shouldHavePlant));
                //if (i < 5) {
                  //  nextGenPots.add(0, new Pot(nextGenPots.get(0).index - 1, false));
                //}
                foundPlant = true;
            //}
        }

        for (int i = initialPotSize - 2; i < initialPotSize; i++) {
            nextGenPots.add(new Pot(pots.get(i).index, pots.get(i).hasPlant));
        }

        boolean needsMore = 15 < nextGenPots.size() && nextGenPots.subList(nextGenPots.size() - 15, nextGenPots.size()).stream().anyMatch(pot -> pot.hasPlant);
        if (needsMore) {
            for (int i = initialPotSize; i < initialPotSize + 5; i++) {
                nextGenPots.add(new Pot(i, false));
            }
        }
        return nextGenPots;
    }

    private long part2(List<Rule> rules, List<Pot> pots) {
        pots.forEach(System.out::print);
        System.out.println();
        long l = 50000000000L;
        long lastSum = pots.stream().filter(pot -> pot.hasPlant).mapToLong(pot -> pot.index).sum();
        long lastDiff = lastSum;
        long sameDiffCount = 0;
        for (long i = 0; i < 400; i++) {
            //System.out.println(i + " " + pots.size());
            pots = getNextGenPots(rules, pots);
            long sum = pots.stream().filter(pot -> pot.hasPlant).mapToLong(pot -> pot.index).sum();
            long diff = sum - lastSum;
            lastSum = sum;
            //System.out.println(i + " sum: " + sum + " diff: " + diff);

            if (diff == lastDiff) {
                sameDiffCount++;
            } else {
                lastDiff = diff;
                sameDiffCount = 0;
            }


            /*
            pots.forEach(System.out::print);
            System.out.println();
            pots.stream().map(pot -> pot.index + " ").forEach(System.out::print);
            System.out.println();


            if (sameDiffCount > 100) {
                //return sum + diff * (l-i-1);
            }
            */
        }

        return pots.stream().filter(pot -> pot.hasPlant).mapToLong(pot -> pot.index).sum() + lastDiff * (l-2000);
        // not 1900000000574 to high
        //     1900000000612
        //     1900000000650
        //     1899999939736
        //     1899999939774
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

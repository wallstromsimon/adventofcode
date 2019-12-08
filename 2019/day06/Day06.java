import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day06 {
    public static void main(String[] args) {
        Day06 day = new Day06();
    }

    public Day06() {
        Map<String, Planet> allPlanets = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(allPlanets)); // 110190
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(allPlanets));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(Map<String, Planet> allPlanets) {
        String COM = "COM";
        Planet rootPlanet = allPlanets.get(COM);
        return "part1: " + calcNbrOfOrbits(rootPlanet);
    }

    private int calcNbrOfOrbits(Planet rootPlanet) {
        int count = 0;
        for (Planet child : rootPlanet.orbitors) {
            child.orbitCount = rootPlanet.orbitCount + 1;
            count += child.orbitCount + calcNbrOfOrbits(child);
        }
        return count;
    }

    private String part2(Map<String, Planet> allPlanets) {
        return "part2";
    }

    private Map<String, Planet> init() {
        String path = "input.txt";
        //String path = "input_test.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            var input = stream.collect(Collectors.toList()); //forEach(System.out::println);
            Map<String, Planet> allPlanets = new HashMap<>();
            for (String mapRow : input) {
                String planetString1 = mapRow.substring(0, mapRow.indexOf(')'));
                String planetString2 = mapRow.substring(mapRow.indexOf(')') + 1);
                //System.out.println(mapRow + " -> " + planetString1 + " ) " + planetString2);

                if (!allPlanets.containsKey(planetString1)) {
                    allPlanets.put(planetString1, new Planet(planetString1));
                }

                if (!allPlanets.containsKey(planetString2)) {
                    allPlanets.put(planetString2, new Planet(planetString2));
                }

                Planet planet1 = allPlanets.get(planetString1);
                Planet planet2 = allPlanets.get(planetString2);

                planet1.orbitors.add(planet2);
                planet2.orbits = planet1;
            }
            return allPlanets;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    private class Planet {
        String name;
        Planet orbits;
        List<Planet> orbitors;
        int orbitCount;

        Planet(String name) {
            this.name = name;
            orbitors = new ArrayList<>();
        }
    }
}

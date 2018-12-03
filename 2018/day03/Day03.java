import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day03 {
    public static void main(String[] args) {
        Day03 day = new Day03();
    }

    public Day03() {
        List<Claim> claims = init();
        System.out.println("Part one: " + part1(claims));

        System.out.println("Part two: " + part2(claims));
    }

    private int part1(List<Claim> claims) {
        int[][] fabric = populateFabric(claims);
        int sqClaimed = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (fabric[i][j] > 1) {
                    sqClaimed++;
                }
            }
        }
        return sqClaimed;
    }

    private int[][] populateFabric(List<Claim> claims) {
        int[][] fabric = new int[1000][1000];

        for (Claim claim : claims) {
            for (int i = claim.left; i < claim.left + claim.wide; i++) {
                for (int j = claim.top; j < claim.top + claim.tall; j++) {
                    fabric[i][j]++;
                }
            }
        }
        return fabric;
    }

    private int part2(List<Claim> claims) {
        int[][] fabric = populateFabric(claims);

        for (Claim claim : claims) {
            boolean overlap = false;
            for (int i = claim.left; i < claim.left + claim.wide; i++) {
                for (int j = claim.top; j < claim.top + claim.tall; j++) {
                    if (fabric[i][j] > 1) {
                        overlap = true;
                    }
                }
            }
            if (!overlap) {
                return claim.id;
            }
        }

        return 0;
    }

    private List<Claim> init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.map(Claim::new).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private class Claim {
        int id;
        int left;
        int top;
        int wide;
        int tall;

        public Claim(String row) {
            String[] input = row.split(" ");
            id = Integer.parseInt(input[0].substring(1));
            left = Integer.parseInt(input[2].split(",")[0]);
            top = Integer.parseInt(input[2].split(",")[1].replaceAll(":", ""));
            wide = Integer.parseInt(input[3].split("x")[0]);
            tall = Integer.parseInt(input[3].split("x")[1]);
        }
    }
}

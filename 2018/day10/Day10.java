import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 {
    public static void main(String[] args) {
        Day10 day = new Day10();
    }

    public Day10() {
        var input = init();
        long start = System.currentTimeMillis();
        checkSky(input);
        long one = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms ");
        /*
        Maybe i = 10101
-****---*****---******--*****---*****---*****---******-----***
*----*--*----*--*-------*----*--*----*--*----*--*-----------*-
*-------*----*--*-------*----*--*----*--*----*--*-----------*-
*-------*----*--*-------*----*--*----*--*----*--*-----------*-
*-------*****---*****---*****---*****---*****---*****-------*-
*--***--*-------*-------*-------*-------*-------*-----------*-
*----*--*-------*-------*-------*-------*-------*-----------*-
*----*--*-------*-------*-------*-------*-------*-------*---*-
*---**--*-------*-------*-------*-------*-------*-------*---*-
-***-*--*-------******--*-------*-------*-------******---***--
         */
    }

    private void checkSky(List<Light> input) {
        int minXd = Integer.MAX_VALUE;
        int minYd = Integer.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0; i < 100000; i++) {
            for (Light light : input) {
                light.x = light.x + light.vx;
                light.y = light.y + light.vy;
            }
            int maxX = input.stream().mapToInt(l -> l.x).max().getAsInt();
            int minX = input.stream().mapToInt(l -> l.x).min().getAsInt();
            int maxY = input.stream().mapToInt(l -> l.y).max().getAsInt();
            int minY = input.stream().mapToInt(l -> l.y).min().getAsInt();

            int xDiff = maxX - minX;
            int yDiff = maxY - minY;

            // Check how "dense" the points are
            // If not work, how manny points in a line, or how manny connected or close points
            if (xDiff < minXd && yDiff < minYd) {
                minIndex = i;
                minXd = xDiff;
                minYd = yDiff;
            }

        }

        // print around the "densest" point
        for (int i = minIndex - 3; i < minIndex + 3; i++) {
            System.out.println("Maybe i = " + i);
            // Clean input!
            input = init();
            for (Light light : input) {
                light.x = light.x + light.vx * i;
                light.y = light.y + light.vy * i;
            }

            print(input);

        }
    }

    private void print(List<Light> input) {
        int maxX = input.stream().mapToInt(l -> l.x).max().getAsInt();
        int minX = input.stream().mapToInt(l -> l.x).min().getAsInt();
        int maxY = input.stream().mapToInt(l -> l.y).max().getAsInt();
        int minY = input.stream().mapToInt(l -> l.y).min().getAsInt();

        // normalize
        maxX = maxX - minX;
        maxY = maxY - minY;

        String[][] sky = new String[maxX + 1][maxY + 1];

        for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                sky[i][j] = " ";
            }
        }

        for (Light l : input) {
            sky[l.x - minX][l.y - minY] = "*";
        }

        // flip the print,
        for (int i = 0; i <= maxY; i++) {
            for (int j = 0; j <= maxX; j++) {
                System.out.print(sky[j][i]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
    }

    private List<Light> init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.map(Light::new).collect(Collectors.toList());//.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private class Light {
        int x;
        int y;
        int vx;
        int vy;

        private Light(String inputLine) {
            // position=< 1,  7> velocity=< 1,  0>
            String[] p = inputLine.substring(inputLine.indexOf('<') + 1, inputLine.indexOf('>')).split(", ");
            x = Integer.parseInt(p[0].strip());
            y = Integer.parseInt(p[1].strip());
            String[] v = inputLine.substring(inputLine.lastIndexOf('<') + 1, inputLine.lastIndexOf('>')).split(", ");
            vx = Integer.parseInt(v[0].strip());
            vy = Integer.parseInt(v[1].strip());
            //System.out.println(inputLine);
            //System.out.println(toString());
        }

        @Override
        public String toString() {
            return "p: (" + x + ", " + y + ") v: (" + vx + ", " + vy + ")";
        }
    }
}

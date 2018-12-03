import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day03 {
    public static void main(String[] args) {
        Day03 day = new Day03();
    }

    public Day03() {
        init();
        System.out.println("Part one: ");
        
        System.out.println("Part two: ");
    }
    
    private void init(){
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

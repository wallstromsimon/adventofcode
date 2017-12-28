import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day11 {
    public static void main(String[] args) {
        Day11 day11 = new Day11();
    }

    public Day11() {
        init();
        System.out.println("Part one: ");
        
        System.out.println("Part two: ");
    }
    
    private void init(){
        try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

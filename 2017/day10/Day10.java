import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day10 {
    public static void main(String[] args) {
        Day10 day10 = new Day10();
    }

    public Day10() {
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

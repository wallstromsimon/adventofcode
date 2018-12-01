import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day01 {
    public static void main(String[] args) {
        Day01 day = new Day01();
    }

    public Day01() {
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

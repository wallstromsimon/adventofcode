import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day09 {
    public static void main(String[] args) {
        Day09 day09 = new Day09();
    }
    
    private String dirtyStream;
    private long score = 0;

    public Day09() {
        init();
        //dirtyStream = "{{<a!>},{<a!>},{<a!>},{<ab>}}"; // 3
        //dirtyStream = "{{<!!>},{<!!>},{<!!>},{<!!>}}"; // 9
        calculateScore();
        System.out.println("Part one: " + score);
        
        System.out.println("Part two: ");
    }
    
    private void init(){
        try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
            dirtyStream = stream.collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateScore() {
        char lastCh = 'a';
        int depth = 0;
        boolean garbage = false;
        for (char ch : dirtyStream.toCharArray()) {
            System.out.println("last: " + lastCh + " current: " + ch + " depth: " + depth + " garbage: " + garbage + " score: " + score);
            boolean ignore = lastCh == '!';
            lastCh = ignore ? 'a' : ch;
            // if < activate garbage mode
            if (!ignore && !garbage && ch == '<') {
                garbage = true;
            }
            // if > deactivate garbage mode
            if (!ignore && garbage && ch == '>') {
                garbage = false;
            }
            // if { depth ++
            if (!ignore && !garbage && ch == '{') {
                depth++;
            }
            // if } score ++ and dept--
            if (!ignore && !garbage && ch == '}') {
                score += depth;
                depth--;
            }
        }
    }
}

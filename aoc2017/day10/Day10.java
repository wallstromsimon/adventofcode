import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day10 {
    public static void main(String[] args) {
        Day10 day10 = new Day10();
    }
    private List<Integer> hash;
    private List<Integer> lengths;
    private int currentPos;
    private int skipSize;

    public Day10() {
        initPartOne();
        hash();
        System.out.println("Part one: " + hash.get(0) * hash.get(1));
        
        initPartTwo();
        for (int i = 0; i < 64; i++) {
            hash();
        }
        denseHash();
        System.out.println("Part two: " + hashHex());
    }
    
    private String init(){
        currentPos = 0;
        skipSize = 0;
        hash = IntStream.range(0, 256).boxed().collect(Collectors.toList());
        //hash = IntStream.range(0, 5).boxed().collect(Collectors.toList());
        try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
            return stream.collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void initPartOne() {
        String input = init();
        lengths = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        //lengths = Arrays.asList(3, 4, 1, 5);
    }

    private void initPartTwo() {
        String input = init();
        lengths = input.chars().boxed().collect(Collectors.toList());
        lengths.addAll(Arrays.asList(17, 31, 73, 47, 23));
    }
    
    private void hash() {
        for (int length : lengths) {
            //Reverse the order of that length of elements in the list, starting with the element at the current position.
            reverse(currentPos, length);
            //Move the current position forward by that length plus the skip size.
            currentPos = (currentPos + length + skipSize) % (hash.size());
            //Increase the skip size by one.
            skipSize++;
        }
    }
    private void reverse(int currentPos, int length) {
        int endPos = currentPos + length;
        int wrap = endPos >= hash.size() ? endPos - hash.size() : 0;
        endPos = endPos >= hash.size() ? hash.size() : endPos;
        //System.out.println("Reversing " + length + " from pos " + currentPos + " to " + endPos + " and wrap 0 to " + wrap);
        List<Integer> tmpList = new ArrayList<>(hash.subList(currentPos, endPos));
        if (wrap != 0) {
            tmpList.addAll(hash.subList(0, wrap));
        }
        Collections.reverse(tmpList);
        for (int i = currentPos; (i - currentPos) < tmpList.size(); i++) {
            hash.set(i % hash.size(), tmpList.get(i - currentPos)); 
        }
    }
    
    private void denseHash() {
        List<Integer> tmpList = new ArrayList<>();
        for (int i = 0; i < hash.size() / 16; i++) {
            int denseHash = hash.get(i * 16);
            for (int j = 1; j < hash.size() / 16; j++) {
                denseHash ^= hash.get(j + (i * 16));
            }
            tmpList.add(denseHash);
        }
        hash = tmpList;
    }

    private String hashHex() {
        return hash.stream().map(i -> Integer.toHexString(i)).collect(Collectors.joining());
    }
}

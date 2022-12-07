import java.util.*;

public class Day06 {
    public static void main(String[] args) {
        List<Integer> mem = Arrays.asList(2,8,8,5,4,2,3,1,5,5,1,2,15,13,5,14);
        Set<String> seen = new HashSet<>();
        int steps = 0;
        do {
            seen.add(mem.toString());
            int max = mem.stream().mapToInt(Integer::intValue).max().getAsInt();
            int index = mem.indexOf(max);
            int reAlloc = mem.get(index);
            mem.set(index, 0);
            while (reAlloc > 0) {
                index = (index + 1) % mem.size();
                mem.set(index, mem.get(index) + 1);
                reAlloc--;
            }
            steps++;
        } while (!seen.contains(mem.toString()));
        System.out.println(steps);
    }
}

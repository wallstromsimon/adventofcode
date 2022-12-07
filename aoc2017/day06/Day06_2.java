import java.util.*;

public class Day06_2 {
    public static void main(String[] args) {
        List<Integer> mem = Arrays.asList(2,8,8,5,4,2,3,1,5,5,1,2,15,13,5,14);
        List<String> seen = new ArrayList<>();
        int steps = 0;
        String lastState = mem.toString();
        do {
            seen.add(lastState);
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
            lastState = mem.toString();
        } while (!seen.contains(lastState));
        System.out.println(steps - seen.indexOf(lastState));
    }
}

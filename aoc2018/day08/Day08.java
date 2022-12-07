import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day08 {
    public static void main(String[] args) {
        Day08 day = new Day08();
    }

    public Day08() {
        var input = init();
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private int part1(Node root) {
        return metadataSum(root);
    }

    private int metadataSum(Node node) {
        int metaSum = node.meta.stream().mapToInt(Integer::intValue).sum();
        if (node.nbrChilds > 0) {
            for (Node child : node.childs) {
                metaSum += metadataSum(child);
            }
        }
        return metaSum;
    }

    private int part2(Node root) {
        return metadataSum2(root);
    }

    private int metadataSum2(Node node) {
        int metaSum = 0;
        if (node.nbrChilds == 0) {
            metaSum = node.meta.stream().mapToInt(Integer::intValue).sum();
        } else {
            for (Integer index : node.meta) {
                index--; // to match index of array
                if (index >= 0 && index < node.childs.size()) {
                    metaSum += metadataSum2(node.childs.get(index));
                } else {
                    metaSum += 0;
                }
            }
        }
        return metaSum;
    }

    private Node init() {
        String path = "input.txt";
        if (System.getProperty("user.dir").endsWith("adventofcode")) { // executed from the proj root dir
            path = "tmp/production/adventofcode/" + this.getClass().getSimpleName().toLowerCase() + "/" + path;
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            String s = stream.findFirst().get(); // only one line
            List<Integer> input = Stream.of(s.split(" ")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

            Node root = new Node();
            populateNodes(input, 0, root);
            //System.out.println(root);
            return root;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int populateNodes(List<Integer> input, int index, Node node) {
        // remove headers
        node.nbrChilds = input.get(index++);
        node.nbrMeta = input.get(index++);

        // Create nodes
        for (int i = 0; i < node.nbrChilds; i++) {
            Node child = new Node();
            node.childs.add(child);
            index = populateNodes(input, index, child);
        }

        // Assign meta from the "inside" out
        for (int i = 0; i < node.nbrMeta; i++) {
            node.meta.add(input.get(index + i));
        }

        // return the index where the nex node begins
        return index + node.nbrMeta;
    }

    private class Node {
        int nbrChilds;
        int nbrMeta;
        List<Node> childs = new ArrayList<>();
        List<Integer> meta = new ArrayList<>();

        @Override
        public String toString() {
            return "NODE: {nbrc:" + nbrChilds + ", nbrm:" + nbrMeta + ", m:" + meta + ", cs:" + childs + "}";
        }
    }
}

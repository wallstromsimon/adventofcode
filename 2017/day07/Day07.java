import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day07 {
    private Map<String, Node> nodes;
    public static void main(String[] args) {
        Day07 day07 = new Day07();
    }

    public Day07() {
        initNodes();
        //nodes.values().stream().forEach(System.out::println);
        Node root = findRoot();
        System.out.println("Part one: " + root.name);
        
        System.out.println("Part two: (check last and second to last output line)");
        nodeSum(root);
        checkDisc(root);
    }
    
    private void initNodes(){
        nodes = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
            stream.forEach(line -> createAndAddNodeFromLine(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAndAddNodeFromLine(String line) {
        Node node = new Node();
        node.name = line.substring(0,line.indexOf(" "));
        node.weight = Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(")")));
        node.neighbours = new HashSet<>();
        if (line.contains(" -> ")) {
            String names = line.substring(line.indexOf(" -> ") + 4);
            Arrays.stream(names.split(", ")).forEach(name -> node.neighbours.add(name));
        }
        nodes.put(node.name, node);
    }
    
    private Node findRoot() {
        // find a node that no one is pointing on.
        Set<String> innerNodes = nodes.values().stream().flatMap(n -> n.neighbours.stream()).collect(Collectors.toSet());
        for (Node node : nodes.values()) {
            if (!innerNodes.contains(node.name)) {
                return node;
            }
        }
        return null;
    }
    
    private int nodeSum(Node node) {
        node.totWeight = node.weight;
        if (node.neighbours != null && !node.neighbours.isEmpty()) {
            node.totWeight += node.neighbours.stream().mapToInt(name -> nodeSum(nodes.get(name))).sum();
        }
        return node.totWeight;
    }

    private void checkDisc(Node node) {
        System.out.println("Chcking: " + node + " " + node.neighbours.stream().map(name -> (name + " " + nodes.get(name).totWeight)).collect(Collectors.toList()));
        // If a neighbour is alone with its weight, call this method recusrively.
        Set<Node> neighbours = node.neighbours.stream().map(name -> nodes.get(name)).collect(Collectors.toSet());
        for (Node neighbour : neighbours) {
            if (!(neighbours.stream().mapToInt(n -> n.totWeight).filter(i -> i == neighbour.totWeight).count() > 1)) {
                checkDisc(neighbour);
            }
        }
    }

    private class Node {
        String name;
        int weight;
        int totWeight;
        Set<String> neighbours;

        @Override
        public String toString() {
            return name + " (" + weight + ")";
        }
    }
}

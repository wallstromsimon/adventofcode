import java.util.*;

public class Day09 {
    public static void main(String[] args) {
        Day09 day = new Day09();
    }

    public Day09() {
        Game input = new Game(418, 70769); // 402398
        Game test = new Game(9, 25); // 32
        Game test0 = new Game(10, 1618); // 8317
        Game test1 = new Game(13, 7999); // 146373
        Game test2 = new Game(17, 1104); // 2764
        Game test3 = new Game(21, 6111); // 54718
        Game test4 = new Game(30, 5807); // 37305
        Game input100 = new Game(418, 7076900); // 3426843186

        long start = System.currentTimeMillis();
        System.out.println("Part one: " + findWinningScore(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + findWinningScore(input100));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private long findWinningScore(Game game) {
        int marble = 0;

        int player = 1;
        Map<Integer, Long> score = new HashMap<>();

        Circle circle = new Circle();
        circle.add(marble);

        while (marble <= game.marbles) {
            //c.print();
            //c.printB();
            marble++;

            if (marble % 23 == 0) {
                // current marble added to score
                // 7 marbles counter-clockwise added to score
                long tmpScore = marble + circle.removeAndGetMinus7();
                if (score.containsKey(player)) {
                    score.put(player, score.get(player) + tmpScore);
                } else {
                    score.put(player, tmpScore);
                }
            } else {
                circle.addPlus2(marble);
            }
            player = (player) % game.players + 1;
        }
        return score.values().stream().mapToLong(Long::longValue).max().getAsLong();
    }

    private class Circle {
        Node currentNode;
        Node firstNode;

        private void add(int marble) {
            if (currentNode == null) {
                currentNode = new Node(marble);
                currentNode.next = currentNode;
                currentNode.prev = currentNode;
                firstNode = currentNode;
            } else {
                addAfterCurrentNode(marble);
            }
        }

        private void addAfterCurrentNode(int marble) {
            Node tmp = new Node(marble);
            tmp.next = currentNode.next;
            tmp.prev = currentNode;
            currentNode.next.prev = tmp;
            currentNode.next = tmp;
            currentNode = tmp;
        }

        private void addPlus2(int marble) {
            currentNode = currentNode.next;
            addAfterCurrentNode(marble);
        }

        private int removeAndGetMinus7() {
            currentNode = currentNode.prev.prev.prev.prev.prev.prev.prev;
            currentNode.prev.next = currentNode.next;
            currentNode.next.prev = currentNode.prev;
            int marble = currentNode.marble;
            currentNode = currentNode.next;
            return marble;
        }

        private void print() {
            Node tmpNode = firstNode;
            do {
                System.out.print(tmpNode.marble + " -> ");
                tmpNode = tmpNode.next;
            } while (tmpNode.marble != firstNode.marble);
            System.out.println();
        }

        private void printB() {
            Node tmpNode = firstNode;
            do {
                System.out.print(tmpNode.marble + " -> ");
                tmpNode = tmpNode.prev;
            } while (tmpNode.marble != firstNode.marble);
            System.out.println();
        }
    }

    private class Node {
        int marble;
        Node next;
        Node prev;
        private Node(int marble) {
            this.marble = marble;
        }
    }

    private class Game {
        int players;
        int marbles;

        public Game(int players, int marbles) {
            this.players = players;
            this.marbles = marbles;
        }
    }
}

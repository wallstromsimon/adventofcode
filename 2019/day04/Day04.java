import java.util.Arrays;
import java.util.List;

public class Day04 {
    public static void main(String[] args) {
        Day04 day = new Day04();
    }

    public Day04() {
        // input 307237-769058
        int min = 307237;
        int max = 769058;
        test();

        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(min, max)); // 889
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(min, max)); // 589
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");

    }

    private void test() {
        // valid 1
        System.out.println("111111:  true == " + isValidPassword(111111));
        System.out.println("223450: false == " + isValidPassword(223450));
        System.out.println("123789: false == " + isValidPassword(123789));

        // valid 2
        System.out.println("123444: false == " + isValidPassword2(123444));
        System.out.println("112233:  true == " + isValidPassword2(112233));
        System.out.println("111122: false == " + isValidPassword2(111122));
        System.out.println("111111: false == " + isValidPassword2(111111));
        System.out.println("223450: false == " + isValidPassword2(223450));
        System.out.println("123789: false == " + isValidPassword2(123789));
    }

    private String part1(int min, int max) {
        long count = 0;
        for (int i = min; i <= max; i++) {
            if (isValidPassword(i)) {
                count++;
            }
        }
        return "part1: " + count;
    }

    private boolean isValidPassword(int i) {
        String number = Integer.toString(i);
        List<String> splittedNumber = Arrays.asList(number.split(""));
        //System.out.println(splittedNumber);

        boolean neverDescending = true;
        boolean twoAdjacentTheSame = false;
        int prevDigit = Integer.MIN_VALUE;

        for (String digit : splittedNumber) {
            int currentDigit = Integer.parseInt(digit);
            if (currentDigit < prevDigit) {
                neverDescending = false;
            }

            if (currentDigit == prevDigit) {
                twoAdjacentTheSame = true;
            }

            prevDigit = currentDigit;
        }

        return neverDescending && twoAdjacentTheSame;
    }

    private String part2(int min, int max) {
        long count = 0;
        for (int i = min; i <= max; i++) {
            if (isValidPassword2(i)) {
                count++;
            }
        }
        return "part2: " + count;
    }

    private boolean isValidPassword2(int i) {
        String number = Integer.toString(i);
        List<String> splittedNumber = Arrays.asList(number.split(""));

        boolean neverDescending = true;
        int prevDigit = Integer.MIN_VALUE;
        boolean twoAdjacentTheSame = false;
        for (int j = 0; j < splittedNumber.size(); j++) {
            String current = splittedNumber.get(j);
            int currentDigit = Integer.parseInt(current);
            if (currentDigit < prevDigit) {
                neverDescending = false;
            }
            prevDigit = currentDigit;

            int sameCount = 0;
            // before
            for (int k = j; k >= 0; k--) { // count current as well
                String num = splittedNumber.get(k);
                if (current.equals(num)) {
                    sameCount++;
                } else {
                    break;
                }
            }

            // after
            for (int k = j + 1; k < splittedNumber.size(); k++) {
                String num = splittedNumber.get(k);
                if (current.equals(num)) {
                    sameCount++;
                } else {
                    break;
                }
            }

            if (sameCount == 2) {
                twoAdjacentTheSame = true;
            }
        }

        return neverDescending && twoAdjacentTheSame;
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day14 {
    public static void main(String[] args) {
        Day14 day = new Day14();
    }

    public Day14() {
        int input = 190221;
        //int input = 9; // test1: 5158916779
        //int input = 5; // 0124515891
        //int input = 51589; // 9
        //int input = 92510; // 18
        long start = System.currentTimeMillis();
        System.out.println("Part one: " + part1(input));
        long one = System.currentTimeMillis();
        System.out.println("Part two: " + part2(input));
        long two = System.currentTimeMillis();
        System.out.println("one: " + (one - start) + "ms two: " + (two - one) + "ms");
    }

    private String part1(int input) {
        List<Integer> recipes = initRecipes();

        int elf1Index = 0;
        int elf2Index = 1;

        while (recipes.size() <= input + 10) {
            int e1 = recipes.get(elf1Index);
            int e2 = recipes.get(elf2Index);

            int newRecipie = e1 + e2;

            if (newRecipie > 9) {
                recipes.add(newRecipie / 10);
                recipes.add(newRecipie % 10);
            } else {
                recipes.add(newRecipie);
            }

            //recipes.addAll(Arrays.stream(Integer.toString(newRecipie).split("")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));

            elf1Index = (elf1Index + 1 + e1) % recipes.size();
            elf2Index = (elf2Index + 1 + e2) % recipes.size();
        }

        return recipes.subList(input, input + 10).stream().map(integer -> "" + integer).collect(Collectors.joining());
    }

    private String part2(int input) {
        List<Integer> recipes = initRecipes();

        int elf1Index = 0;
        int elf2Index = 1;
        while (recipes.size() < 5) {
            int e1 = recipes.get(elf1Index);
            int e2 = recipes.get(elf2Index);

            int newRecipie = e1 + e2;
            if (newRecipie > 9) {
                recipes.add(newRecipie / 10);
                recipes.add(newRecipie % 10);
            } else {
                recipes.add(newRecipie);
            }

            elf1Index = (elf1Index + 1 + e1) % recipes.size();
            elf2Index = (elf2Index + 1 + e2) % recipes.size();
        }

        int i = 0;
        while (i == 0) {
            //print(recipes, elf1Index, elf2Index);
            int e1 = recipes.get(elf1Index);
            int e2 = recipes.get(elf2Index);

            int newRecipie = e1 + e2;
            if (newRecipie > 9) {
                recipes.add(newRecipie / 10);
                recipes.add(newRecipie % 10);
            } else {
                recipes.add(newRecipie);
            }

            elf1Index = (elf1Index + 1 + e1) % recipes.size();
            elf2Index = (elf2Index + 1 + e2) % recipes.size();
            i = matchingRecipe(Integer.toString(input), recipes);
        }

        return "" + i; //+ matchingRecipe(Integer.toString(input), recipes);
    }

    private List<Integer> initRecipes() {
        List<Integer> recipies = new ArrayList<>();
        recipies.add(3);
        recipies.add(7);
        return recipies;
    }

    Set<Integer> matches = new HashSet<>();

    private int matchingRecipe(String input, List<Integer> recipes) {
        int offset = input.length();
        for (int i = matches.size(); i < recipes.size() - offset; i++) {
            if (!matches.contains(i)) {
                boolean match = input.equals(recipes.subList(i, i + offset).stream().map(integer -> "" + integer).collect(Collectors.joining()));
                matches.add(i);
                if (match) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void print(List<Integer> recipies, int eIndex1, int eIndex2) {
        for (int i = 0; i < recipies.size(); i++) {
            if (i == eIndex1) {
                System.out.print("(");
            } else if (i == eIndex2) {
                System.out.print("[");
            }

            System.out.print(recipies.get(i));

            if (i == eIndex1) {
                System.out.print(")");
            } else if (i == eIndex2) {
                System.out.print("]");
            } else {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}

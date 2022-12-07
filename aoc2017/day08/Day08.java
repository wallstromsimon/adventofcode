import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.io.*;

public class Day08 {
    public static void main(String[] args) {
        Day08 day08 = new Day08();
    }

    private Map<String, Integer> registers;
    private List<String> instructions;
    private int maxValue = 0; 

    public Day08() {
        init();
        executeInstructions();

        System.out.println("Part one: " + registers.values().stream().mapToInt(Integer::intValue).max().getAsInt());
        
        System.out.println("Part two: " + maxValue);
    }
    
    private void init(){
        registers = new HashMap<>();
        instructions = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
            stream.forEach(line -> {
                    instructions.add(line);
                    registers.put(line.substring(0, line.indexOf(" ")), 0);
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeInstructions() {
        for (String instruction : instructions) {
            executeInstruction(instruction);
        }
    }

    private void executeInstruction(String instruction) {
        String[] instructionParts = instruction.split(" ");
        String registerToModify = instructionParts[0];
        String incOrDec = instructionParts[1];
        Integer value = Integer.parseInt(instructionParts[2]);
        String conditionReg = instructionParts[4];
        String conditionOp = instructionParts[5];
        String conditionVal = instructionParts[6];
        //System.out.println(registerToModify + " " + incOrDec + " " + value + " if " + conditionReg + " " + conditionOp + " " + conditionVal);

        if (checkCondition(conditionReg, conditionOp, Integer.parseInt(conditionVal))) {
            int registerValue = registers.get(registerToModify);
            if (incOrDec.equals("inc")) {
                registerValue += value;
            } else {
                registerValue -= value;
            }
            maxValue = registerValue > maxValue ? registerValue : maxValue;
            registers.put(registerToModify, registerValue);
        }
    }

    private boolean checkCondition(String conditionReg, String conditionOp, int conditionVal) {
        switch (conditionOp) {
            case "==": return registers.get(conditionReg).intValue() == conditionVal;
            case "!=": return registers.get(conditionReg).intValue() != conditionVal;
            case ">=": return registers.get(conditionReg).intValue() >= conditionVal;
            case ">": return registers.get(conditionReg).intValue() > conditionVal;
            case "<=": return registers.get(conditionReg).intValue() <= conditionVal;
            case "<": return registers.get(conditionReg).intValue() < conditionVal;
        }
        throw new RuntimeException();
    }
}

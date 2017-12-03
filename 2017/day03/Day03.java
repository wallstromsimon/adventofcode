import java.util.*;

public class Day03 {
    public static void main(String[] args) {
        int target = 368078;
        int side = (int)Math.round(Math.sqrt(target)) + 1;
        System.out.println(side);
        int mem[][] = new int[side][side];

        int x = side / 2;
        int y = x;
        int startCoord = x;
        int dir = 0; // 0 right, 1 up, 2 left, 3 down
        int foldLen = 1;
        int foldCount = 0;
        for (int i = 1; i <= target; i++) {
            //System.out.println(i + " at [" + x + "][" + y + "] dir: " + dir + " foldC: " + foldCount + " foldLength: " + foldLen);
            foldCount++;
            mem[x][y] = i;
            if (i == target) { break;}
            switch (dir){
                case 0: y++; break;
                case 1: x--; break;
                case 2: y--; break;
                case 3: x++; break;
            }
            if (foldCount >= foldLen) {
                dir = (dir + 1)%4;
                foldCount = 0;
                if (dir == 2 || dir == 0) {
                    foldLen++;
                }
            }
        }
        double distance = (Math.abs(x-startCoord) + Math.abs(y-startCoord));
        System.out.println("Start: ["+startCoord+"]["+startCoord+"]:"+mem[startCoord][startCoord] + ", End: ["+x+"]["+y+"]:"+mem[x][y]+" Len: " + distance);
    }
}

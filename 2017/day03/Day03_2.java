import java.util.*;

public class Day03_2 {
    private static int calcValue(int[][] mem, int x, int y) {
        int res = 0;
        boolean maxX = x >= mem.length - 1;
        boolean minX = x <= 0;
        boolean maxY = y >= mem[x].length - 1;
        boolean minY = y <= 0;
        
        res += maxX ? 0 : mem[x+1][y];
        res += minX ? 0 : mem[x-1][y];
        res += maxY ? 0 : mem[x][y+1];
        res += minY ? 0 : mem[x][y-1];
        res += maxX || maxY ? 0 : mem[x+1][y+1];
        res += maxX || minY ? 0 : mem[x+1][y-1];
        res += minX || maxY ? 0 : mem[x-1][y+1];
        res += minX || minY ? 0 : mem[x-1][y-1];
        return res;
    }

    public static void main(String[] args) {
        int target = 368078;
        int side = (int)Math.round(Math.sqrt(target)) + 1;
        //System.out.println(side);
        int mem[][] = new int[side][side];

        int x = side / 2;
        int y = x;
        int startCoord = x;
        int dir = 0; // 0 right, 1 up, 2 left, 3 down
        int foldLen = 1;
        int foldCount = 0;
        int val = 1;
        while (val <= target) {
            //System.out.println(val + " at [" + x + "][" + y + "] dir: " + dir + " foldC: " + foldCount + " foldLength: " + foldLen);
            foldCount++;
            mem[x][y] =  val;
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
            val = calcValue(mem,x,y);
        }
        System.out.println("Val: " + val);
    }
}

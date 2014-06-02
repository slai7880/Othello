/* This is one of the most important class in this project. It offers a powerful
   checking method for almost all other classes. The two major testing methods
   do not return boolean values. Instead they return integers. The programs calling
   that call the two methods must do the boolean.
   
Sha Lai
   
*/

import java.util.*;

public class Analyzer {
   private static Map<Integer, Point> board;
   private String player;
   private String oppo;
   private int key;
   
   // The numbers indicates the direction the method is checking.
   private static final int[] DIRECTIONS = {-10, 10, -1, 1, -9, 9, -11, 11};
   
   // Marks if the class is used by AI.
   private boolean aiOn;
   
   // True if the real_world board is to be changed.
   private boolean flip;
   
   private boolean getWeight;
   private boolean assume;
   private int level;
   
   public Analyzer(Map<Integer, Point> board) {
      this.board = board;
   }
   
   // Examines whether or the human's move is legal. True if the number is positive.
   public int testMove(String player, int key, boolean flip) {
      aiOn = false;
      this.player = player;
      this.key = key;
      this.flip = flip;
      setPlayer(player);
      return computeSum();
   }
   
   // Let the AI makes an assumption.
   public int testAssumption(String player, int key, boolean getWeight, boolean assume, int level) {
      aiOn = true;
      this.player = player;
      this.key = key;
      this.getWeight = getWeight;
      this.assume = assume;
      this.level = level;
      setPlayer(player);
      return computeSum();
   }
   
   private void setPlayer(String player) {
      if (player.equals("X")) {
         oppo = "O";
      } else {
         oppo = "X";
      }
   }
   
   private int computeSum() {
      int sum = 0;
      for (int i: DIRECTIONS) {
         sum += checkAndCount(getList(i));
      }
      return sum;
   }
   
   // fills the list with elements in the map
   private List<Point> getList(int d) {
      List<Point> list = new ArrayList<Point>();
      int i = key + d;
      while (checkKey(i)) {
         list.add(board.get(i));
         i += d;
      }
      return list;
   }
   
   // checks whether the int is in the range of the map
   public boolean checkKey(int key) {
      int d1 = key % 10;
      int d2 = key / 10;
      return d1 > 0 && d1 < 9 && d2 > 0 && d2 < 9;
   }
   
   // checks and flips(if required to)
   private int checkAndCount(List<Point> list) {
      int runningSum = 0;
      int c = 0;
      if (list.size() > 1) {
         int i = 0;
         if (aiOn) {
            while (i < list.size() && list.get(i).getAssumption(level).equals(oppo)) {
               i++;
            }
            if (i > 0 && i < list.size() && list.get(i).getAssumption(level).equals(player)) {
               c = i;
               if (assume) {
                  for (int j = i; j >= 0; j--) {
                     runningSum += list.get(j).getWeight();
                     list.get(j).assume(player, level);
                  }
                  if (getWeight) {
                     c = runningSum;
                  }
               }
            }
         } else {
            while (i < list.size() && list.get(i).getPlayer().equals(oppo)) {
               i++;
            }
            if (i > 0 && i < list.size() && list.get(i).getPlayer().equals(player)) {
               c = i;
               if (flip) {
                  while (i >= 0) {
                     list.get(i).place(player);
                     i--;
                  }
               }
            }
         }
      }
      return c;
   }
}
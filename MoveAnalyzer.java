/* This class is used to analyze 8 directions of a particular spot on the board.
   If needed, it can also do the flipping job. The return type of the analyzatoin
   part is not boolean but int in order to fit in most cases. 
   
   Each toDirection() method constructs a list to store all spots in that direcion
   (excluding the original point) and then check how many disks are about to be fliped.
   Or it will just flip when the client class set the boolean "flip" to be true.
   
   Even though I could have set the type to be boolean, but in that case I need to write
   very similar code for the AI part since it needs to consider with the upcoming number
   of disks on the board before actually place the next move. Returning an int would 
   probably make it flexible. */
   
import java.util.*;
import java.io.*;

public class MoveAnalyzer extends Analyzer {
   private Map<Integer, Point> board;
   private String player;
   private String oppo;
   private int key;
   private boolean flip;
   
   public MoveAnalyzer(Map<Integer, Point> board) {
      super(board);
      this.board = board;
   }
   
   ////////////////////////////////////////////////////////////////////////////////////////////////
   
   public int testMove(String player, int key, boolean flip) {
      if (!checkKey(key)) {
         throw new IllegalArgumentException("Invalid move.");
      }
      this.player = player;
      this.key = key;
      this.flip = flip;
      if (this.player.equals("X")) {
         oppo = "O";
      } else {
         oppo = "X";
      }
      int cross = testCross();
      int X = testX();
      return cross + X;
   }
   
   ////////////////////////////////////////////////////////////////////////////////////////////////
   
   /*public int testCross() {
      return testVertical() + testHorizontal();
   }
   
   ////////////////////////////////////////////////////////////////////////////////////////////////
   
   public int testVertical() {
      return toNorth() + toSouth();
   }
   
   public int toNorth() {
      List<Point> toNorth = new ArrayList<Point>();
      getList(toNorth, -10);
      return checkAndCount(toNorth);
   }
   
   public int toSouth() {
      List<Point> toSouth = new ArrayList<Point>();
      getList(toSouth, 10);
      return checkAndCount(toSouth);
   }
   
   ////////////////////////////////////////////////////////////////////////////////////////////////
   
   public int testHorizontal() {
      return toWest() + toEast();
   }
   
   public int toWest() {
      List<Point> toWest = new ArrayList<Point>();
      getList(toWest, -1);
      return checkAndCount(toWest);
   }
   
   public int toEast() {
      List<Point> toEast = new ArrayList<Point>();
      getList(toEast, 1);
      return checkAndCount(toEast);
   }
   
   ////////////////////////////////////////////////////////////////////////////////////////////////
   
   public int testX() {
      return testSlash() + testBackSlash();
   }
   
   ////////////////////////////////////////////////////////////////////////////////////////////////
   
   public int testSlash() {
      return toNE() + toSW();
   }
   
   public int toNE() {
      List<Point> toNE = new ArrayList<Point>();
      getList(toNE, -9);
      return checkAndCount(toNE);
   }
   
   public int toSW() {
      List<Point> toSW = new ArrayList<Point>();
      getList(toSW, 9);
      return checkAndCount(toSW);
   }
   
   ////////////////////////////////////////////////////////////////////////////////////////////////
   
   public int testBackSlash() {
      return toNW() + toSE();
   }
   
   public int toNW() {
      List<Point> toNW = new ArrayList<Point>();
      getList(toNW, -11);
      return checkAndCount(toNW);
   }
   
   public int toSE() {
      List<Point> toSE = new ArrayList<Point>();
      getList(toSE, 11);
      return checkAndCount(toSE);
   }
   
   ////////////////////////////////////////////////////////////////////////////////////////////////
   
   // shared methods
   
   // fills the list with elements in the map
   public void getList(List<Point> list, int d) {
      int i = key + d;
      while (checkKey(i)) {
         list.add(board.get(i));
         i += d;
      }
   }
   
   // checks whether the int is in the range of the map
   public boolean checkKey(int key) {
      int d1 = key % 10;
      int d2 = key / 10;
      return d1 > 0 && d1 < 9 && d2 > 0 && d2 < 9;
   }*/
   
   // checks and flips(if required to)
   public int checkAndCount(List<Point> list) {
      int runningSum = 0;
      int c = 0;
      if (list.size() > 1) {
         int i = 0;
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
      return c;
   }
}
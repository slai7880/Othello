/* 

This class constructs a point on the board. It tells the client class almost everything
that is required. The field level and point[] are connected. The number of level indicates
the largest index of the point[]. In terms of point[], the 0 index indicates what will be
displayed to the user, the others are for AI, with the indexes indicating the level the
AI is currently on. 

Sha Lai

*/

import java.util.*;

public class Point implements BasicInfo {
   private int x;           // stores the x coordinate
   private int y;           // stores the y coordinate
   private int w;           // stores the weight of the spot
   private int level;       // stores the AI's level
   private String[] point;  // stores the displayed and the hidden (for AI only) board
   
///////////////////////////////////////////////////////////////////////////////////////////////////
/*                         Fields Used to Assign Weight for Spots                                */
   
   /*
   
   The Othello uses a 8 * 8 chess board, so considering it's symmetric about the origin, I choose
   only 10 spots to analyze, and then apply the values to the rest. The chosen ones:
   
   11 12 13 14
      22 23 24
         33 34
            44
   
   */
   
   
   private static final int[] C = {11, 18, 81, 88};
   private static final int[] P_C = {12, 17, 21, 22, 27, 28, 71, 72, 77, 78, 82, 87};
   private static final int[] S_1 = {13, 16, 31, 38, 61, 68, 83, 86};
   private static final int[] S_2 = {14, 15, 41, 48, 51, 58, 84, 85};
   private static final int[] P_S = {23, 24, 25, 26, 32, 37, 42, 47, 52, 57, 62, 67, 73, 74, 75, 76};
   private static final int[] I = {33, 33, 34, 35, 36, 43, 46, 53, 56, 63, 64, 65, 66};
   private static final int[] O = {44, 45, 54, 55};
   public static final int[][] SPOT_GROUP = {C, P_C, S_1, S_2, P_S, I, O};
   
   // store weights of each area
   private static final int CORNER = 99;
   private static final int PRECORNER = -50;
   private static final int SIDE_1 = 40;
   private static final int SIDE_2 = 20;
   private static final int PRESIDE = 1;
   private static final int INNER = 10;
   private static final int ORIGIN = 4;
   public static final int[] WEIGHT_GROUP = {CORNER, PRECORNER, SIDE_1, SIDE_2, PRESIDE, INNER, ORIGIN};
   
   static {
      for (int[] i: SPOT_GROUP) {
         Arrays.sort(i);
      }
   }
   
///////////////////////////////////////////////////////////////////////////////////////////////////

   // Constructs a point with no AI's level, for PvP (not included yet) only.
   public Point(int y, int x) {
      this(y, x, 0);
   }
   
   // Constructs a point with a given level.
   public Point(int y, int x, int level) {
      this.x = x;
      this.y = y;
      this.level = level;
      point = new String[level + 1];
      point[0] = "[" + P0 + "]";
      
      setWeight();
   }
   
///////////////////////////////////////////////////////////////////////////////////////////////////

/*                              Private Methods for Grouping Spots                               */
   
   private void setWeight() {
      int k = y * 10 + x;
      for (int i = 0; i < SPOT_GROUP.length; i++) {
         if (Arrays.binarySearch(SPOT_GROUP[i], k) > -1) {
            w = WEIGHT_GROUP[i];
         }
      }
   }

///////////////////////////////////////////////////////////////////////////////////////////////////
   
   // Changes the current spot (assigning a decision).
   public void place(String player) {
      point[0] = "[" + player + "]";
   }
   
   // Returns the current player (either "X" or "O" or " ") on this spot.
   public String getPlayer() {
      return "" + point[0].charAt(1);
   }
   
   public String toString() {
      return point[0];
   }
   
   // Returns the weight of the spot.
   public int getWeight() {
      return w;
   }
   
///////////////////////////////////////////////////////////////////////////////////////////////////

/*                                    Methods for AI                                             */
   
   // Makes an assumption.
   public void assume(String p, int lvl) {
      for (int i = lvl; i <= level; i++) {
         point[i] = "[" + p + "]";
      }
   }
   
   // Returns the state at the given level, excluding brackets.
   public String getAssumption(int lvl) {
      return "" + point[lvl].charAt(1);
   }
   
   // Returns the state at the given level, including brackets.
   public String assumedPoint(int lvl) {
      return point[lvl];
   }
   
   // Resets the spot at the current level to the previous state.
   public void reset(int lvl) {
      point[lvl] = point[lvl - 1];
   }
   
   // Resets all assumptions to the current(real-world) state.
   public void resetAll() {
      for (int i = 1; i <= level; i++) {
         point[i] = point[0];
      }
   }
   
///////////////////////////////////////////////////////////////////////////////////////////////////

/*                             Methods for Other or Future Usages                                */

   public String weightToString() {
      if (w > 0) {
         if (w < 10) {
            return "[+0" + w + "]";
         } else {
            return "[+" + w + "]";
         }
      } else {
         return "[" + w + "]";
      }
   }
   
   public void setWeight(int w) {
      this.w = w;
   }
}
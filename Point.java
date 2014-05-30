/* 

This class constructs a point on the board. It tells the client class almost everything
that is required. The field level and point[] are connected. The number of level indicates
the largest index of the point[]. In terms of point[], the 0 index indicates what will be
displayed to the user, the others are for AI, with the indexes indicating the level the
AI is currently on. 

*/

import java.util.*;

public class Point {
   private int x;           // stores the x coordinate
   private int y;           // stores the y coordinate
   private int w;           // stores the weight of the spot
   private int level;       // stores the AI's level
   private String[] point;  // stores the displayed and the hidden (for AI only) board
   
///////////////////////////////////////////////////////////////////////////////////////////////////
/*                         Fields Used to Assign Weight for Spots                                */

   private static Set<Integer> corners;
   private static Set<Integer> preCorners;
   private static Set<Integer> sides;
   private static Set<Integer> preSides;
   private static Set<Integer> inner;
   
   private static final int[] C = {11, 18, 81, 88};
   private static final int[] P_C = {12, 17, 21, 22, 27, 28, 71, 72, 77, 78, 82, 87};
   private static final int[] S = {13, 14, 15, 16, 38, 48, 58, 68, 86, 85, 84, 83, 61, 51, 41, 31};
   private static final int[] P_S = {23, 24, 25, 26, 37, 47, 57, 67, 76, 75, 74, 73, 62, 52, 42, 32};
   private static final int[] I = {33, 34, 35, 36, 46, 56, 66, 65, 64, 63, 53, 43, 33};
   
   // store weights of each area
   private static final int CORNER = 99;
   private static final int PRECORNER = -20;
   private static final int SIDE = 5;
   private static final int PRESIDE = 1;
   private static final int INNER = 2;
   
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
      point[0] = "[ ]";
      
      // initializes 5 areas
      corners = new TreeSet<Integer>();
      preCorners = new TreeSet<Integer>();
      sides = new TreeSet<Integer>();
      preSides = new TreeSet<Integer>();
      inner = new TreeSet<Integer>();
      
      assignGroups();
   }
   
///////////////////////////////////////////////////////////////////////////////////////////////////

/*                              Private Methods for Grouping Spots                               */

   private void assignGroups() {     
      add(corners, C);
      add(preCorners, P_C);
      add(sides, S);
      add(preSides, P_S);
      add(inner, I);
      setWeight();
   }
   
   private void add(Set<Integer> set, int[] i) {
      for (int j: i) {
         set.add(j);
      }
   }
   
   private void setWeight() {
      int k = y * 10 + x;
      if (corners.contains(k)) {
         w = CORNER;
      } else if (preCorners.contains(k)) {
         w = PRECORNER;
      } else if (sides.contains(k)) {
         w = SIDE;
      } else if (preSides.contains(k)) {
         w = PRESIDE;
      } else {
         w = INNER;
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
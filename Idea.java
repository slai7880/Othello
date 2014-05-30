/*

Actually this is the hardcore of AI. It stores most of part of the AI while the AICore only
examines the very first step. The basic approach for this AI is the min-max approach. But
that is just an abstract approach. What really makes building AI hard is to determine how many
variables should the computer consider, and how to assign weights for each one. For now this
AI s*cks, it only consider the weight of each spot and the opponent's mobility. Plus it does not
prune the tree. If the user sets the level higher than 5, the system will be too busy(the AI record
file will be like 10M). Therefore I will keep updating it.

*/

import java.util.*;
import java.io.*;

public class Idea implements Comparable<Idea> {
   public List<Idea> nextLevel;
   private int key;
   private static Map<Integer, Point> board;
   private String player;
   private String oppo;
   private List<Integer> oppoSpaces;
   private static Analyzer analyzer;
   private Random r;
   private static PrintStream output;
   
   // strategy variables
   private static int setLevel;    // the maximum level
   private int currentLevel;
   private int pieceWeightSum;     // the sum of weight of all occupied spots for the player
   private int oppoMobility;       // the opponent's mobility (number of spots available)
   private int value;              // value = pieceWeightSum - oppoMObility
   private int nextValue;          // the highest value in the next level
   public int utility;             // utility = value + nextValue
   
   // Constructs a new Idea object, and automatically builds up a tree structure.
   public Idea(PrintStream output, int key, Map<Integer, Point> board, String player, int currentLevel, int setLevel) {
      this.output = output;
      this.key = key;
      this.board = board;
      this.player = player;
      this.currentLevel = currentLevel;
      this.setLevel = setLevel;
      r = new Random();
      nextLevel = new ArrayList<Idea>();
      if (player.equals("X")) {
         oppo = "O";
      } else {
         oppo = "X";
      }
      analyzer = new Analyzer(this.board);
      oppoMobility = 0;
      
      reset();
      
      board.get(key).assume(player, currentLevel);
      analyzer.testAssumption(player, key, false, true, currentLevel);
      pieceWeightSum = computeWeightSum();
      oppoMobility = computeOppoMobility();
      value = pieceWeightSum - oppoMobility;
      
      // the tree is growing
      if (currentLevel < setLevel) {
         for (int i: oppoSpaces) {
            nextLevel.add(new Idea(output, i, this.board, oppo, this.currentLevel + 1, this.setLevel));
         }
         Collections.sort(nextLevel);
         
         if (nextLevel.size() > 0) {
            nextValue = nextLevel.get(nextLevel.size() - 1).computeValue();
         } else {
            nextValue = 0;
         }
      }
      utility = value + nextValue;
      recordAI();
   }
   
   // Returns the sum of occupied spots for the player.
   private int computeWeightSum() {
      int sum = 0;
      for (Point point: board.values()) {
         if (point.getPlayer().equals(player)) {
            sum += point.getWeight();
         }
      }
      return sum;
   }
   
   // Returns the spots available for the opponent.
   private int computeOppoMobility() {
      oppoSpaces = availableSpots();
      return oppoSpaces.size();
   }
   
   // Minus applies for the opponent.
   private int computeValue() {
      return (int) Math.pow(-1, currentLevel) * value;
   }
   
   // Returns the key of this idea.
   public int getKey() {
      return key;
   }
   
   public int compareTo(Idea other) {
      if (currentLevel == 1) {
         return utility - other.utility;
      } else {
         return value - other.value;
      }
   }
   
   public String toString() {
      return "key: " + key + "  weight sum: " + pieceWeightSum;
   }
   
   private List<Integer> availableSpots() {
      List<Integer> list = new ArrayList<Integer>();
      for (int i: board.keySet()) {
         if (board.get(i).getAssumption(currentLevel).equals(" ")) {
            if (analyzer.testAssumption(oppo, i, false, false, currentLevel) > 0) {
               list.add(i);
            }
         }
      }
      return list;
   }
   
   public int getValue() {
      return value;
   }
   
   public int getUtility() {
      return utility;
   }
   
   // Records the AI.
   private void recordAI() {
      output.println();
      output.println("Considering spot:      " + key);
      output.println("Current player:        " + player);
      output.println("Current level:         " + currentLevel);
      output.println("pieceWeightSum =       " + pieceWeightSum);
      output.println("Oppo's Mobility =      " + oppoMobility);
      output.println("Value =                " + value);
      output.println("nextLevel.size() =     " + nextLevel.size());
      output.println("utility =              " + value + " + " + nextValue + " = " + utility);
      List<Integer> list = new ArrayList<Integer>();
         for (Idea idea: nextLevel) {
            list.add(idea.computeValue());
         }
      for (int i = 1; i <= 8; i++) {
         for (int j = 1; j <= 8; j++) {
            //System.out.print(board.get(i * 10 + j).assumedPoint(currentLevel));
            output.print(board.get(i * 10 + j).assumedPoint(currentLevel));
         }
         //System.out.println();
         output.println();
      }
      output.println("============================");
      output.flush();
   }
   
   private void reset() {
      for (int i: board.keySet()) {
         board.get(i).reset(currentLevel);
      }
   }
}
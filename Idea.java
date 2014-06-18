/*

Actually this is the hardcore of AI. It stores most of part of the AI while the AICore only
examines the very first step. The basic approach for this AI is the min-max approach. But
that is just an abstract approach. What really makes building AI hard is to determine how many
variables should the computer consider, and how to assign weights for each one. For now this
AI s*cks, it only consider the weight of each spot and the opponent's mobility. Plus it does not
prune the tree. If the user sets the level higher than 5, the system will be too busy. Therefore
I will keep updating it.

Sha Lai

*/

import java.util.*;
import java.io.*;

public class Idea implements Comparable<Idea>, BasicInfo {
   // general fields
   public List<Idea> nextLevel;
   private int key;
   private static Map<Integer, Point> board;
   private String[][] currentBoard;
   private String player;
   private String oppo;
   private List<Integer> oppoSpaces;
   private List<Integer> nextUtilityList;
   private List<Integer> nextSortedUtilityList;
   private static Analyzer analyzer;
   private static PrintStream output;
   private static String givenPlayer; // stores the side the computer is holding
   
   // strategy variables
   private static int setLevel;    // the maximum level
   public int currentLevel;
   private int pieceWeightSum;     // the sum of weight of all occupied spots for the player
   private int oppoMobility;       // the opponent's mobility (number of spots available)
   private int utility;             // utility = value + nextValue
   private int nextUtility;
   
   // fields for pruning the tree
   private static final boolean AB_PRUNING = true;  // applies alpha-beta pruning?
   private boolean stop;
   private int alpha, beta;         // floor, ceiling
   private int oldAlpha, oldBeta;
   private String node;
   private String nextNode;
   private List<Integer> cutOff;  // stores the keys of points that are cut off
   
   // Constructs a new Idea object, and automatically builds up a tree structure.
   public Idea(PrintStream output, int key, Map<Integer, Point> board, String player,
               int currentLevel, int setLevel, String givenPlayer, String node, int alpha, int beta) {
      stop = false;
      this.output = output;
      this.key = key;
      this.board = board;
      this.player = player;
      this.currentLevel = currentLevel;
      this.setLevel = setLevel;
      this.givenPlayer = givenPlayer;
      this.node = node;
      this.alpha = alpha;
      this.beta = beta;
      oldAlpha = alpha;
      oldBeta = beta;
      cutOff = new ArrayList<Integer>();
      nextLevel = new ArrayList<Idea>();
      if (player.equals(P1)) {
         oppo = P2;
      } else {
         oppo = P1;
      }
      analyzer = new Analyzer(this.board);
      oppoMobility = 0;
      
      nextUtilityList = new ArrayList<Integer>();
      nextSortedUtilityList = new ArrayList<Integer>();
      
      board.get(key).assume(player, currentLevel);
      analyzer.attemp(player, key, currentLevel);
      recordBoard();
      
      develop();
   }
   
   private void develop() {
      // the tree is growing
      if (currentLevel < setLevel) {
         oppoMobility = computeOppoMobility();
         if (oppoMobility != 0) { // opponent has a spot to move, switch to the opponent
            nextNode = getNextNodeName();
            int i = 0;
            while (!stop && i < oppoSpaces.size()) {
               int spot = oppoSpaces.get(i);
               Idea temp = new Idea(output, spot, board, oppo, currentLevel + 1, setLevel, givenPlayer, nextNode, alpha, beta);
               if (AB_PRUNING) {  // can be disabled at the top
                  pruneTheTree(temp, i);
               }
               nextLevel.add(temp);
               reset(currentLevel + 1);
               i++;
            }
         } else {                 // oppoent doesn't have a spot to move, do not switch
            List<Integer> playerSpaces = availableSpots(player);
            for (int i: playerSpaces) {
               nextLevel.add(new Idea(output, i, board, player, currentLevel, setLevel, givenPlayer, node, alpha, beta));
               reset(currentLevel + 1);
            }
         }
         
         saveUtilities(nextUtilityList);
         Collections.sort(nextLevel);
         saveUtilities(nextSortedUtilityList);
         
         if (nextLevel.size() > 0) {
            utility = nextLevel.get(nextLevel.size() - 1).utility;
         }
      } else {    // reaches the end of the tree
         pieceWeightSum = computeWeightSum();
         oppoMobility = computeOppoMobility();
         utility = pieceWeightSum - oppoMobility / 2;
      }
   }
   
   // Returns the sum of occupied spots for the player.
   private int computeWeightSum() {
      int sum = 0;
      for (Point point: board.values()) {
         if (point.getAssumption(currentLevel).equals(givenPlayer)) {
            sum += point.getWeight();
         } else if(!point.getAssumption(currentLevel).equals(P0)) {
            sum -= point.getWeight();
         }
      }
      return sum;
   }
   
   // Returns the spots available for the opponent.
   private int computeOppoMobility() {
      oppoSpaces = availableSpots(oppo);
      return oppoSpaces.size();
   }
   
   // Minus applies for the oppoent.
   private int computeUtility() {
      return (int) Math.pow(-1, currentLevel) * utility;
   }
   
   // Returns the key of this idea.
   public int getKey() {
      return key;
   }
   
   public int compareTo(Idea other) {
      if (node.equals(MIN)) {
         return utility - other.utility;
      } else {
         return other.utility - utility;
      }
   }
   
   public String toString() {
      return "" + key;
   }
   
   private List<Integer> availableSpots(String s) {
      List<Integer> list = new ArrayList<Integer>();
      for (int i: board.keySet()) {
         if (board.get(i).getAssumption(currentLevel).equals(" ")) {
            if (analyzer.testAssumption(s, i, currentLevel) > 0) {
               list.add(i);
            }
         }
      }
      return list;
   }
   
   
   public int getUtility() {
      return utility;
   }
   
   // Gets a copy of the board at current level.
   private void recordBoard() {
      currentBoard = new String[HEIGHT][WIDTH];
      for (int y = 0; y < HEIGHT; y++) {
         for (int x = 0; x < WIDTH; x++) {
            currentBoard[y][x] = board.get((y + 1) * 10 + (x + 1)).assumedPoint(currentLevel);
         }
      }
   }
   
   // Records the AI.
   public void recordAI() {
      output.println();
      output.println("Current node:          " + node);
      output.println("Considering spot:      " + key);
      output.println("Current player:        " + player);
      output.println("Current level:         " + currentLevel);
      String tempMessa = "";
      if (currentLevel < setLevel) {
         tempMessa = "N/A";
      } else {
         tempMessa += pieceWeightSum;
      }
      output.println("pieceWeightSum =       " + tempMessa);
      output.println("Oppo's Mobility =      " + oppoMobility);
      output.println("Oppo's Spaces =        " + oppoSpaces);
      output.println("nextLevel.size() =     " + nextLevel.size());
      output.println("nextLevel:             " + nextLevel);
      output.println("nextLevel's utility:   " + nextUtilityList);
      output.println("nextLevel's sorted uti:" + nextSortedUtilityList);
      output.println("utility =              " + utility);
      output.println("oldAlpha =             " + oldAlpha);
      output.println("oldBeta =              " + oldBeta);
      output.println("alpha =                " + alpha);
      output.println("beta =                 " + beta);
      output.println("stop =                 " + stop);
      output.println("cut-off list:          " + cutOff);
      for (int y = 0; y < HEIGHT; y++) {
         for (int x = 0; x < WIDTH; x++) {
            //System.out.print(board.get(i * 10 + j).assumedPoint(currentLevel));
            output.print(currentBoard[y][x]);
         }
         //System.out.println();
         output.println();
      }
      output.println("============================");
      output.flush();
   }
   
   private void reset(int level) {
      for (Point p: board.values()) {
         p.reset(level);
      }
   }
   
   public int getAlpha() {
      return alpha;
   }
   
   public int getBeta() {
      return beta;
   }
   
///////////////////////////////////////////////////////////////////////////////////////////////////
/*                                       Bagging Methods                                         */
   
   // The following methods are pulled out in order to make it a little bit cozier.
   
   private String getNextNodeName() {
      if (node.equals(MAX)) {
         return MIN;
      }
      return MAX;
   }
   
   /*
   
   Alpha_beta pruning:
   
   If this is a MAX node, check if the current beta is less than the next node's utility,
   stops exploring if so, in any case updates the alpha.
   
   If this is a MIN node, check if the current alpha is greater than the next node's utility,
   stops exploring if so, in any case updates the beta.
   
   */
   private void pruneTheTree(Idea temp, int i) {
      if (node.equals(MAX)) {  // if this is a max node
         stop = beta < temp.utility;
         alpha = Math.max(alpha, temp.utility);
      } else {                 // if this is a min node
         stop = alpha > temp.utility;
         beta = Math.min(beta, temp.utility);
      }
      if (stop) {
         for (int j = i + 1; j < oppoSpaces.size(); j++) {
            cutOff.add(oppoSpaces.get(j));
         }
      }
   }
   
   private void saveUtilities(List<Integer> list) {
      for (Idea i: nextLevel) {
         list.add(i.utility);
      }
   }
}
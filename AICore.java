/*

This class manages the first step of AI's work. It first checks all available spots,
and then constructs an Idea object for each one. At last it picks the Idea with the highest
utility to assign. If there is a tie, it would randomly choose one. Also it generates a file
to help further study of AI's strategy.

The appproaches that this AI has so far:
1. (0.6)Min-Max Search
2. (0.4)Computing the Opponent's Mobility

Sha Lai

*/

import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AICore implements BasicInfo {
   private static Map<Integer, Point> board;
   private String player;
   private String oppo;
   private List<Integer> possibleSpots;
   private Analyzer analyzer;
   private Random r;
   private static final int STARTING_LEVEL = 1; // stores the default starting level
   
   private List<Idea> ideas;
   private static int level;
   
   private static PrintStream output;
   private static final String FILE_DIRECTORY = System.getProperty("user.dir") + File.separator + "AI Record" + File.separator;
   
   private static Date date;
   private static final String DATE_FORMAT = "yyyyMMddHHmmss";
   
   public AICore(Map<Integer, Point> board, String player, int level) throws FileNotFoundException, IOException {
      analyzer = new Analyzer(board);
      this.board = board;
      r = new Random();
      this.level = level;
      
      DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
      String currentDate = dateFormat.format(new Date());
      
      String fileName = dateFormat.format(new Date()) + ".record";
      File dir = new File(FILE_DIRECTORY);
      if (!dir.isDirectory()) {
         dir.mkdir();
      }
      File file = new File(FILE_DIRECTORY + fileName);

      output = new PrintStream(file);
      
      board.get(44).assume(P1, STARTING_LEVEL);
      board.get(45).assume(P2, STARTING_LEVEL);
      board.get(55).assume(P1, STARTING_LEVEL);
      board.get(54).assume(P2, STARTING_LEVEL);
   }
   
   /* Returns the key of the idea with the highest utility. Returns -1 if no idea exists
      (which is basically impossible since the game is alreay over in that case). */
   public int bestIdea() {
      if (ideas.size() > 1) {
         int i = ideas.size() - 1;
         while (i > 0 && ideas.get(i - 1).getUtility() == ideas.get(i).getUtility()) {
            i--;
         }
         int n = i + r.nextInt(ideas.size() - i);
         return ideas.get(n).getKey();
      } else if (ideas.size() == 1) {
         return ideas.get(0).getKey();
      } else {
         return -1;
      }
   }
   
   // Considers all available spots on the current board, and stores a list of ideas.
   public void consider(String player) {
      this.player = player;
      if (player.equals(P1)) {
         oppo = P2;
      } else {
         oppo = P1;
      }
      ideas = new ArrayList<Idea>();
      possibleSpots = availableSpots();
      Iterator<Integer> itr = possibleSpots.iterator();
      while (itr.hasNext()) {
         int key = itr.next();
         int c = analyzer.testMove(player, key, false);
         if (c > 0) {
            resetAll();
            try {
               /* As a matter of fact I only construct one list of ideas, but eventually there will
                  be a tree, because the Idea class does the job. Seems like I let the tree "grow". */
               Idea temp = new Idea(output, key, board, player, STARTING_LEVEL, level);
               System.out.println("1 idea added, key: " + key + " utility: " + temp.getUtility());
               ideas.add(temp);
            } catch (Exception error) {
               System.out.println(error);
            }
         }
      }
      Collections.sort(ideas);
      recordAI(ideas);
   }
   
   // Cleans all assumptions so far.
   private void resetAll() {
      for (int i: board.keySet()) {
         board.get(i).resetAll();
      }
   }
   
   // Returns a list of available spots.
   public List<Integer> availableSpots() {
      List<Integer> temp = new ArrayList<Integer>();
      for (int k: board.keySet()) {
         if (board.get(k).getPlayer().equals(" ")) {
            temp.add(k);
         }
      }
      return temp;
   }
   
   // Records the AI tree.
   private void recordAI(List<Idea> list) {
      if (!list.isEmpty()) {
         for (Idea i: list) {
            i.recordAI();
            recordAI(i.nextLevel);
         }
      }
   }
   
}
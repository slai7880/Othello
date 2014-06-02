/* 

This class interacts with the user to play the Reversi / Othello. It has 3 modes: PvP(not available yet), PvAI, and AIvP.
What's disturbing is the thread issues. Honestly I've never learned about any thing about threads. And what hurts more is
that Java API tells me that swing tools are bound to be thread unsafe(> . <#). Since I barely know about how to deal with
those issues, anytime I bump into a thread problem, I just set a 10ms delay. Still this class contains a lot of bad style
codes. And maybe I should combine the three panels together.

Sha Lai

*/

import java.util.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.BorderLayout;

public class PlayGame {
   private Board b;
   private Scanner console;
   private int stepCount;
   private String player;
   private static final String P1 = "X";
   private static final String P2 = "O";
   private Analyzer analyzer;
   private static Map<Integer, Point> board;
   private static final int MAX_LEVEL = 10;
   private static final int DEFAULT_LEVEL = 3;
   private boolean keepPlaying;
   private boolean start;
   private boolean swap;
   
   private AICore ai;
   private int level;
   
   private static TitlePanel tp;
   private static OutputPanel op;
   private static MessagePanel mp;
   private JFrame frame;
   
   public PlayGame() {
   
      level = DEFAULT_LEVEL;
      
      
      keepPlaying = true;
      player = P1;
      
      initPanel();
      start = tp.start;
      for (;;) {
         while (!start) {
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            start = tp.start;
         }
         tp.start = false;
         String chosen = tp.chosenPlayer();
         level = tp.getLevel();
         System.out.println(level);
         if (chosen.equals("X")) {
            //setLevel();
            initialize(true);
            //playPvAI();
            playAIvP(false, false);
         } else if (chosen.equals("O")) {
            //setLevel();
            initialize(true);
            playAIvP(false, true);
         } else {
            System.exit(1);
         }
         start = false;
         startTitlePanel(true);
      }
   }
   
   private void initialize(boolean ai) {
      startGamePanel();
      if (ai) {
         b = new Board(level, op);
      } else {
         b = new Board(op);
      }
      board = b.board;
      stepCount = 0;
      analyzer = new Analyzer(board);
   }
   
   // for PvP
   public void playPvP() {
      while (hasNextMove(true)) {
         b.printBoard(false);
         System.out.println("Current player: " + player + "    Step count: " + stepCount);
         System.out.print("Please give your next move(y): ");
         int y = console.nextInt();
         System.out.print("Please give your next move(x): ");
         int x = console.nextInt();
         assignMove(y, x);
         player = getPlayer(player);
      }
      termination();
   }
   
   // for AIvP && PvAI
   public void playAIvP(boolean clicked, boolean aiFirst) {
      try {
         try { Thread.sleep(10); } catch (InterruptedException e) {}
         
         ai = new AICore(board, player, level);
         op.setClicked(clicked);
         boolean aiGo = aiFirst;
         b.printBoard(false);
         while (hasNextMove(true)) {
            boolean humanGo = true;
            if (aiGo) {
               String message = "Current player: " + player + "    Step count: " + stepCount;
               showScores();
               System.out.println(message);
               ai.consider(player);
               int idea = ai.bestIdea();
               int y = idea / 10;
               System.out.println("AI move(y): " + y);
               int x = idea % 10;
               System.out.println("AI move(x): " + x);
               assignMove(y, x);
               player = getPlayer(player);
               aiGo = false;
               if (!hasNextMove(false)) {
                  aiGo = true;
                  humanGo = false;
                  player = getPlayer(player);
               }
               b.printBoard(false);
               System.out.println();
               message = "Current player: " + player + "    Step count: " + stepCount;
               showScores();
               System.out.println(message);
            }
            if (op.clickedState() && humanGo) {
               int x = op.getX();
               int y = op.getY();
               int temp = stepCount;
               assignMove(y, x);
               aiGo = stepCount > temp;
               op.setClicked(false);
               player = getPlayer(player);
               if (!hasNextMove(false)) {
                  player = getPlayer(player);
                  aiGo = false;
               }
               b.printBoard(false);
            }
         }
         termination();
         System.out.println("termination() passed");
      } catch (Exception e) {
         System.out.println(e);
      }
   }
   
   public void showScores() {
      int xScore = b.XScore();
      int oScore = b.OScore();
      mp.updateScore(xScore, oScore);
   }
   
   public void assignMove(int y, int x) {
      int key = y * 10 + x;
      if (analyzer.checkKey(key) && board.get(key).getPlayer().equals(" ") && analyzer.testMove(player, key, true) > 0) {
         board.get(key).place(player);
         stepCount++;
      } else {
         String message = "Invalid move, please type again.";
         System.out.println(message);
         player = getPlayer(player);
      }
   }
   
   public boolean hasNextMove(boolean doubleTest) {
      List<Integer> possibleSpots = b.availableSpots();
      boolean swap = false;
      boolean playerGo = false;
      if (!possibleSpots.isEmpty()) {
         for (int i: possibleSpots) {
            if (analyzer.testMove(player, i, false) > 0) {
               swap = false;
               playerGo = true;
            }
         }
      }
      if (doubleTest && !playerGo) {
         String oppo;
         if (player.equals(P1)) {
            oppo = P2;
         } else {
            oppo = P1;
         }
         for (int i: possibleSpots) {
            if (analyzer.testMove(oppo, i, false) > 0) {
               swap = true;
            }
         }
      }
      return (playerGo || swap) && b.XScore() != 0 && b.OScore() != 0;
   }
   
   private String getPlayer(String current) {
      if (current.equals(P1)) {
         return P2;
      } else {
         return P1;
      }
   }
   
   private void termination() {
      b.printBoard(true);
      String result = b.finalResult();
      mp.updateFinal(result);
      try { Thread.sleep(10); } catch (InterruptedException e) {}
      while (!mp.again()) {
         try { Thread.sleep(10); } catch (InterruptedException e) {}
      }
      try { Thread.sleep(10); } catch (InterruptedException e) {}
      mp.reset();
      System.out.println("mp.reset() reached");
      try { Thread.sleep(10); } catch (InterruptedException e) {}
   }
   
   private void setLevel() {
      boolean passed = false;
      while (!passed) {
         System.out.print("Please set the AI level(1 - 10): ");
         int n = console.nextInt();
         if (n > 0 && n <= MAX_LEVEL) {
            passed = true;
            level = n;
         } else {
            System.out.println("Level out of bound, please set again.");
         }
      }
   }
   
   private void initPanel() {
      frame = new JFrame("Othello");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      startTitlePanel(false);
      frame.setVisible(true);
   }
   
   private void startTitlePanel(boolean anotherGame) {
      if (anotherGame) {
         frame.getContentPane().removeAll();
         tp = new TitlePanel();
         frame.add(tp);
         frame.pack();
      } else {
         tp = new TitlePanel();
         frame.add(tp);
         frame.pack();
      }
   }
   
   private void startGamePanel() {
      frame.getContentPane().removeAll();
      op = new OutputPanel();
      mp = new MessagePanel();
      frame.setLayout(new BorderLayout());
      frame.add(op, BorderLayout.NORTH);
      frame.add(mp, BorderLayout.SOUTH);
      frame.pack();
      frame.revalidate();
   }
}
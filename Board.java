/* This class stores the board, including the methods that tell the status
   of the board. 
   
   Sha Lai
   
   */

import java.util.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.Color;

public class Board implements BasicInfo {
   // stores the basic info about the board
   private static final int HEIGHT = 8;
   private static final int WIDTH = 8;
   // the key is conctructed as y * 10 + x
   public static Map<Integer, Point> board;
   
   //private OutputPanel panel;
   private static OutputPanel op;
   private Graphics g;
   
   // for....some testing codes
   public Board() {
      this(null);
   }
   
   // for PvP
   public Board(OutputPanel op) {
      this(0, op);
   }
   
   // for games with AI
   public Board(int level, OutputPanel op) {
      board = new HashMap<Integer, Point>();
      for (int y = 1; y <= HEIGHT; y++) {
         for (int x = 1; x <= WIDTH; x++) {
            int yx = y * 10 + x;
            if (level != 0) {
               board.put(yx, new Point(y, x, level));
            } else {
               board.put(yx, new Point(y, x));
            }
         }
      }
      // initializes board
      board.get(44).place(P1);
      board.get(45).place(P2);
      board.get(55).place(P1);
      board.get(54).place(P2);
      for (int i: board.keySet()) {
         board.get(i).resetAll();
      }
      this.op = op;
      if (op != null) {
         g = op.getGraphics();
      }
   }
   
   
   
   // Displays the board.
   public void printBoard(boolean end) {
      for (int i = 1; i <= HEIGHT; i++) {
         for (int j = 1; j <= WIDTH; j++) {
            System.out.print(board.get(i * 10 + j));
            //op.showBoard(i, j, board.get(i * 10 + j).getPlayer());
            //System.out.print("[  ]");
         }
         System.out.println();
      }
      try { Thread.sleep(10); } catch (InterruptedException e) {}
      showBoard();
      try { Thread.sleep(10); } catch (InterruptedException e) {}
      printScore(end);
   }
   
   private void printScore(boolean end) {
      int xScore = 0;
      int oScore = 0;
      for (int i: board.keySet()) {
         if (board.get(i).getPlayer().equals(P1)) {
            xScore++;
         } else if (board.get(i).getPlayer().equals(P2)) {
            oScore++;
         }
      }
      System.out.println(P1 + "  " + xScore + "    " + P2 + ":  " + oScore);
      if (end) {
         if (xScore > oScore) {
            System.out.println("Player " + P1 + " wins, scoring " + xScore + ".");
         } else if (xScore < oScore) {
            System.out.println("Player " + P2 + " wins, scoring " + oScore + ".");
         } else {
            System.out.println("Draw.");
         }
      }
   }
   
   public List<Integer> availableSpots() {
      List<Integer> temp = new ArrayList<Integer>();
      for (int k: board.keySet()) {
         if (board.get(k).getPlayer().equals(P0)) {
            temp.add(k);
         }
      }
      return temp;
   }
   
   private void showBoard() {
      for (int i: board.keySet()) {
         if (board.get(i).getPlayer().equals(P1)) {
            g.setColor(Color.BLACK);
            int iy = i / 10;
            int ix = i % 10;
            g.fillOval((ix - 1) * op.BLOCK_SIZE + op.BOARD_CORNER_X, (iy - 1) * op.BLOCK_SIZE + op.BOARD_CORNER_Y, op.BLOCK_SIZE, op.BLOCK_SIZE);
         } else if (board.get(i).getPlayer().equals(P2)) {
            g.setColor(Color.WHITE);
            int iy = i / 10;
            int ix = i % 10;
            g.fillOval((ix - 1) * op.BLOCK_SIZE + op.BOARD_CORNER_X, (iy - 1) * op.BLOCK_SIZE + op.BOARD_CORNER_Y, op.BLOCK_SIZE, op.BLOCK_SIZE);
         }
      }
   }
   
   public int XScore() {
      int xScore = 0;
      for (Point p: board.values()) {
         if (p.getPlayer().equals(P1)) {
            xScore++;
         }
      }
      return xScore;
   }
   
   public int OScore() {
      int oScore = 0;
      for (Point p: board.values()) {
         if (p.getPlayer().equals(P2)) {
            oScore++;
         }
      }
      return oScore;
   }
   
   // Returns a string of the message when the game is over.
   public String finalResult() {
      int xScore = 0;
      int oScore = 0;
      for (int i: board.keySet()) {
         if (board.get(i).getPlayer().equals(P1)) {
            xScore++;
         } else if (board.get(i).getPlayer().equals(P2)) {
            oScore++;
         }
      }
      String result;
      if (xScore > oScore) {
         result = "Player 1 wins, scoring " + xScore + ".";
      } else if (xScore < oScore) {
         result = "Player 2 wins, scoring " + oScore + ".";
      } else {
         result = "Draw.";
      }
      return result;
   }
   
///////////////////////////////////////////////////////////////////////////////////////////////////

//                                       Special Methods
   
   // These methods are for debugging.
   
   
   // Prints the blank board.
   public void printMatrix() {
      for (int i = 1; i <= HEIGHT; i++) {
         for (int j = 1; j <= WIDTH; j++) {
            System.out.print("[" + (i * 10 + j) + "]");
         }
         System.out.println();
      }
   }
   
   // Prints the weight map.
   public void printWeightMap() {
      for (int i = 1; i <= HEIGHT; i++) {
         for (int j = 1; j <= WIDTH; j++) {
            System.out.print(board.get(i * 10 + j).weightToString());
         }
         System.out.println();
      }
   }
}
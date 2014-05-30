import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.util.*;
import javax.swing.*;
import java.awt.Container;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import java.io.*;


public class OutputPanel extends JPanel implements MouseListener {
   private static boolean clicked;

   private Graphics g;
   public static final int PANEL_SIZE = 400;
   public static final int BLOCK_SIZE = 40;
   public static final int BOARD_CORNER_X = 40;
   public static final int BOARD_CORNER_Y = 40;
   public static final int BOARD_EDGE = 8;
   public static final Color BG_COLOR = Color.BLUE;
   
   public static final int TEXT_CORNER_X = 160;
   public static final int TEXT_CORNER_Y = 512;
   
   private int x, y;

   
   public OutputPanel() {
      clicked = false;
      addMouseListener(this);
      setBorder(BorderFactory.createLineBorder(Color.black));
      setBackground(BG_COLOR);
      x = 0;
      y = 0;
   }
   
   public Dimension getPreferredSize() {
      return new Dimension(PANEL_SIZE, PANEL_SIZE);
   }
   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      drawBlocks(g);
   }
   
   private void drawBlocks(Graphics g) {
      for (int x = 0; x < BOARD_EDGE; x++) {
         int i = x;
         for (int y = 0; y < BOARD_EDGE; y++) {
            g.setColor(Color.CYAN);
            g.fillRect(BOARD_CORNER_X + x * BLOCK_SIZE, BOARD_CORNER_Y + y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(BOARD_CORNER_X + x * BLOCK_SIZE, BOARD_CORNER_Y + y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
         }
      }
   }
   
   
   public void mousePressed(MouseEvent e) {
   }
   
   public void mouseReleased(MouseEvent e) {
   }
   
   public void mouseClicked(MouseEvent e) {
      y = (e.getY() - BOARD_CORNER_Y) / BLOCK_SIZE + 1;
      x = (e.getX() - BOARD_CORNER_X) / BLOCK_SIZE + 1;
      System.out.println("x: " + x + ",  y: " + y);
      clicked = true;
  
   }
   public void mouseMoved(MouseEvent e) {
   }
   public void mouseEntered(MouseEvent e){
  
   }
   public void mouseExited(MouseEvent e) {
  
   }
   public void mouseDragged(MouseEvent e) {
   }
   
   public int getX() {
      return x;
   }
   
   public int getY() {
      return y;
   }
   
   public boolean clickedState() {
      return clicked;
   }
   
   public void setClicked(boolean c) {
      clicked = c;
   }
   
}
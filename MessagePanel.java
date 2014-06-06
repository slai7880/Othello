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
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.*;
import javax.swing.*;
import java.awt.Container;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import java.io.*;

public class MessagePanel extends JPanel {
   public static final int PANEL_HEIGHT = 128;
   public static final int PANEL_WIDTH = 320;
   public static final Color BG_COLOR = Color.CYAN;
   
   public JLabel player1;
   public JLabel player2;
   
   private JButton again;
   private static final Font FONT = new Font("Serief", Font.BOLD, 8);
   private boolean playAgain;
   
   private boolean end;
   
   public MessagePanel() {
      end = false;
      playAgain = false;
      setBorder(BorderFactory.createLineBorder(Color.black));
      setBackground(BG_COLOR);
      setLayout(new FlowLayout(FlowLayout.CENTER, 120, 40));
      player1 = new JLabel(":    ");
      player2 = new JLabel(":    ");
      
      
      add(player1);
      add(player2);
   }
   
   public Dimension getPreferredSize() {
      return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
   }
   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      drawPlayers(g);
   }
   
   public void updateFinal(String message) {
      end = true;
      remove(player1);
      try { Thread.sleep(10); } catch (InterruptedException e) {}
      remove(player2);
      removeAll();
      try { Thread.sleep(10); } catch (InterruptedException e) {}
      JLabel finalMessage = new JLabel(message);
      again = new JButton("Play again?");
      add(finalMessage);
      add(again);
      
      again.setFont(FONT);
      again.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            playAgain = true;
         }
      });
      
   }
   
   public void updateScore(int xScore, int oScore) {
      player1.setText(":    " + xScore);
      player2.setText(":    " + oScore);
   }
   
   private void drawPlayers(Graphics g) {
      g.setColor(Color.BLACK);
      g.fillOval(56, 32, 36, 36);
      g.setColor(Color.WHITE);
      g.fillOval(212, 32, 36, 36);
      if (end) {
         g.setColor(BG_COLOR);
         g.fillRect(0, 32, PANEL_WIDTH, 36);
      }
   }
   
   public boolean again() {
      return playAgain;
   }
   
   public void reset() {
      playAgain = false;
   }
}
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
import java.awt.GridLayout;
import java.util.*;
import javax.swing.*;
import java.awt.Container;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import java.io.*;

public class TitlePanel extends JPanel {
   public static final int PANEL_HEIGHT = 528;
   public static final int PANEL_WIDTH = 400;
   public static final Color BG_COLOR = Color.CYAN;
   private String chosen;
   
   public boolean start;
   
   private static final String P1 = "X";
   private static final String P2 = "O";
   private static final int MAX_LEVEL = 7;
   private static final String MESSAGE_LEVEL = "Choose AI's level (1 - " + MAX_LEVEL + ") inclusive):";
   private static final String MESSAGE_DISK = "Choose disk (black goes first):";
   private static final String MESSAGE_ERROR = "You haven't chosen a level yet.";
   private static final Font FONT = new Font("Serief", Font.BOLD, 10);
   
   JTextField field;
   private int level;
   private boolean levelSet;
   
   public TitlePanel(int level) {
      this();
      this.level = level;
      levelSet = true;
   }
   
   public TitlePanel() {
      levelSet = false;
      start = false;
      setBorder(BorderFactory.createLineBorder(Color.black));
      setBackground(BG_COLOR);
      setLayout(new FlowLayout(FlowLayout.CENTER, 120, 40));
      pickLevel();
      pickPlayer();
   }
   
   public Dimension getPreferredSize() {
      return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
   }
   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
   }
   
   public void pickLevel() {
      addLabel(MESSAGE_LEVEL);
      field = new JTextField(4);
      add(field);
   }

   
   public void pickPlayer() {
      addLabel(MESSAGE_DISK);
      Icon icon1 = new ImageIcon("Black.jpg");
      JButton player1 = new JButton(icon1);
      Icon icon2 = new ImageIcon("White.jpg");
      JButton player2 = new JButton(icon2);
      
      player1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            chosen = P1;
            level = inputCheck();
            try { Thread.sleep(10); } catch (InterruptedException er) {}
            if (level > 0 && level <= MAX_LEVEL) {
               start = true;
            } else {
               addLabel(MESSAGE_ERROR);
            }
         }
      });
      
      player2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            chosen = P2;
            level = inputCheck();
            try { Thread.sleep(10); } catch (InterruptedException er) {}
            if (level > 0 && level <= MAX_LEVEL) {
               start = true;
            } else {
               addLabel(MESSAGE_ERROR);
            }
         }
      });
      
      add(player1);
      add(player2);
   }
   
   private void addLabel(String message) {
      JLabel label = new JLabel(message);
      label.setFont(FONT);
      add(label);
   }
   
   public String chosenPlayer() {
      return chosen;
   }
   
   public int getLevel() {
      return level;
   }
   
   private int inputCheck() {
      Scanner input = new Scanner(field.getText());
      if (input.hasNextInt()) {
         int n = input.nextInt();
         return n;
      }
      System.out.println("no next int");
      return -1;
   }
}
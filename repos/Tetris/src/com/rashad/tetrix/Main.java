package com.rashad.tetrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main extends JFrame {
	
	JLabel statusbar;

	public static void main(String[] args) {
		
		Main game = new Main();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
		
	}
	
    public Main() {

    	statusbar = new JLabel("score 0");
        add(statusbar, BorderLayout.SOUTH);
        Board board = new Board(this);
        
        setSize(600, 1200);
        setTitle("Tetris");
        board.setBackground(Board.background);
        
        add(board);
        board.start();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   }

   public JLabel getStatusBar() {
       return statusbar;
   }


}

package com.rashad.tetrix;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.rashad.tetrix.Block.TetrisBlock;

public class Board extends JPanel implements ActionListener{
	
	static Color background = Customize.Skin1.BACKGROUND;
	
	// Sets total number of shapes on the board
	final int boardWidth = Customize.BoardWidth.LARGE; //units: block
	final int boardHeight = Customize.BoardHeight.LARGE; //units: block

	Timer timedAction;
	
	// Initialize values
	boolean isBlockFalling = false;
    boolean isGamePaused = false;
    boolean isGameOver = false;
    
    // Score
    int clearedFullLines = 0;
    JLabel statusBar;
    
    // Current block
    int currentX = 0;
    int currentY = 0;
    Block currentBlock;
    
    TetrisBlock[] board;
    
	/**
	 * Creates a new game board
	 * @param game
	 */
	public Board(Main game) {
		setFocusable(true); // Set state of board to focusable for event handling

		timedAction = new Timer(Customize.Speed.NORMAL, this); // set game speed
		timedAction.start(); 
		addKeyListener(new KeyAdapterImpl());
	
		
		currentBlock = new Block();
		statusBar =  game.getStatusBar();
		board = new TetrisBlock[boardWidth * boardHeight]; // size = block(s) x block(s)
		clearBoard();  
	}

	/**
	 * When action event is heard, check if block is still falling and 
	 * either create a new piece of move one line down
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (isBlockFalling) {
	        isBlockFalling = false;
	        newPiece();
	    } else {
	        oneLineDown();
	    }
	}
	
	/**
	 * Get block at given coordinates
	 *  
	 * @param x coordinate on board
	 * @param y coorinate on board
	 * @return 
	 */
	public TetrisBlock getShapeAt(int x, int y) { 
		return board[(y * boardWidth) + x]; 
	}

	/**
	 * Move to input coordinates and update current block if coordinates within board boundaries
	 * 
	 * @param newPiece
	 * @param newX
	 * @param newY
	 * @return
	 */
	private boolean moveTo(Block newPiece, int newX, int newY)
	{
	    for (int i = 0; i < 4; ++i) {
	        int x = newX + newPiece.getX(i);
	        int y = newY - newPiece.getY(i);
	        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight)
	            return false;
	        if (getShapeAt(x, y) != TetrisBlock.NOSHAPE)
	            return false;
	    }
	
	    currentBlock = newPiece;
	    currentX = newX;
	    currentY = newY;
	    repaint();
	    return true;
	}
	
	public void start()
	{
	    if (isGameOver)
	        return;
	
	    isGamePaused = true;
	    isBlockFalling = false;
	    clearedFullLines = 0;
	    clearBoard();
	
	    newPiece();
	    timedAction.start();
	}

	private void pause()
	{
	    if (!isGamePaused)
	        return;
	
	    isGameOver = !isGameOver;
	    if (isGameOver) {
	        timedAction.stop();
	        statusBar.setText("paused");
	    } else {
	        timedAction.start();
	        statusBar.setText(String.valueOf("score " + clearedFullLines));
	    }
	    repaint();
	}
	
	// Get width and height of block square 
	public int getSquareWidth() { return (int) getSize().getWidth() / boardWidth; }
	public int getSquareHeight() { return (int) getSize().getHeight() / boardHeight; }
	
	/**
	 * Loops over all blocks on board and draw block unless the block is NOSHAPE
	 */
	public void paint(Graphics g)
	{ 
	    super.paint(g);
	
	    Dimension size = getSize();
	    int boardTop = (int) size.getHeight() - boardHeight * getSquareHeight();
	
	    //draw starting from the top left
	    for (int i = 0; i < boardHeight; ++i) {
	        for (int j = 0; j < boardWidth; ++j) {
	            TetrisBlock shape = getShapeAt(j, boardHeight - i - 1);
	            if (shape != TetrisBlock.NOSHAPE)
	                drawSquare(g, j * getSquareWidth(),
	                           boardTop + i * getSquareHeight(), shape);
	        }
	    }
	
	    // draw current piece
	    if (currentBlock.getShape() != TetrisBlock.NOSHAPE) {
	        for (int i = 0; i < 4; ++i) {
	            int x = currentX + currentBlock.getX(i);
	            int y = currentY - currentBlock.getY(i);
	            drawSquare(g, x * getSquareWidth(),
	                       boardTop + (boardHeight - y - 1) * getSquareHeight(),
	                       currentBlock.getShape());
	        }
	    }
	}

	/**
	 * Drop block instantly to the bottom when Space Bar is pressed
	 */
	private void instantDrop()
	{
	    int newY = currentY;
	    while (newY > 0) {
	        if (!moveTo(currentBlock, currentX, newY - 1))
	            break;
	        --newY;
	    }
	    reachedBottom();
	}

	private void oneLineDown()
	{
	    if (!moveTo(currentBlock, currentX, currentY - 1))
	        reachedBottom();
	}
	
	private void clearBoard()
	{
	    for (int i = 0; i < boardHeight * boardWidth; ++i)
	        board[i] = TetrisBlock.NOSHAPE;
	}

	private void reachedBottom()
	{
	    for (int i = 0; i < 4; ++i) {
	        int x = currentX + currentBlock.getX(i);
	        int y = currentY - currentBlock.getY(i);
	        board[(y * boardWidth) + x] = currentBlock.getShape();
	    }
	
	    clearCompleteRows();
	
	    if (!isBlockFalling)
	        newPiece();
	}

	private void newPiece()
	{
	    currentBlock.setRandomShape();
	    currentX = boardWidth / 2 + 1;
	    currentY = boardHeight - 1 + currentBlock.minY();
	
	    if (!moveTo(currentBlock, currentX, currentY)) {
	        currentBlock.setShape(TetrisBlock.NOSHAPE);
	        timedAction.stop();
	        isGamePaused = false;
	        statusBar.setText("game over");
	    }
	}


	private void clearCompleteRows()
	{
	    int numFullLines = 0;
	
	    for (int i = boardHeight - 1; i >= 0; --i) {
	        boolean lineIsFull = true;
	
	        for (int j = 0; j < boardWidth; ++j) {
	            if (getShapeAt(j, i) == TetrisBlock.NOSHAPE) {
	                lineIsFull = false;
	                break;
	            }
	        }
	
	        if (lineIsFull) {
	            ++numFullLines;
	            for (int k = i; k < boardHeight - 1; ++k) {
	                for (int j = 0; j < boardWidth; ++j)
	                     board[(k * boardWidth) + j] = getShapeAt(j, k + 1);
	            }
	        }
	    }
	
	    if (numFullLines > 0) {
	        clearedFullLines += numFullLines;
	        statusBar.setText("score " + String.valueOf(clearedFullLines));
	        isBlockFalling = true;
	        currentBlock.setShape(TetrisBlock.NOSHAPE);
	        repaint();
	    }
	}

	/**
	 * Draws square with color and borders
	 * 
	 * @param g java.awt.Graphics
	 * @param x int
	 * @param y int
	 * @param shape BlockTypes
	 */
	private void drawSquare(Graphics g, int x, int y, TetrisBlock shape)
	{
	    Color colors[] = Customize.Skin1.COLOR;
	
	    Color color = colors[shape.ordinal()];
	
	    // square
	    g.setColor(color);
	    g.fillRect(x + 1, y + 1, getSquareWidth() - 2, getSquareHeight() - 2);
	
	    // top and left border
	    g.setColor(color.brighter());
	    g.drawLine(x, y + getSquareHeight() - 1, x, y);
	    g.drawLine(x, y, x + getSquareWidth() - 1, y);
	
	    // bottom and right border
	    g.setColor(color.darker());
	    g.drawLine(x + 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + getSquareHeight() - 1);
	    g.drawLine(x + getSquareWidth() - 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + 1);
	}

	/**
	 * Implements method in abstract class key adapter to fire Key Events on key press
	 * 
	 * @author rasha
	 *
	 */
	class KeyAdapterImpl extends KeyAdapter {
	     public void keyPressed(KeyEvent e) {
	    	 int keycode = e.getKeyCode();
	
	    	 if (keycode == 'q' || keycode == 'Q') {
	    		 System.exit(0);
	    	 }
	    	 if (keycode == 'r' || keycode == 'R') {
	    		 start();
	    		 return;
	    	 }
	    	 
	         if (!isGamePaused || currentBlock.getShape() == TetrisBlock.NOSHAPE) {  
	             return;
	         }
	         
	         if (keycode == 'p' || keycode == 'P') {
	             pause();
	             return;
	         }
	
	         if (isGameOver)
	             return;
	
	         switch (keycode) {
	         case KeyEvent.VK_LEFT:
	             moveTo(currentBlock, currentX - 1, currentY);
	             break;
	         case KeyEvent.VK_RIGHT:
	             moveTo(currentBlock, currentX + 1, currentY);
	             break;
	         case KeyEvent.VK_DOWN:
	             moveTo(currentBlock.rotateRight(), currentX, currentY);
	             break;
	         case KeyEvent.VK_UP:
	             moveTo(currentBlock.rotateLeft(), currentX, currentY);
	             break;
	         case KeyEvent.VK_SPACE:
	             instantDrop();
	             break;
	         case 'd':
	             oneLineDown();
	             break;
	         case 'D':
	             oneLineDown();
	             break;
	         }
	
	     }
	 }

}

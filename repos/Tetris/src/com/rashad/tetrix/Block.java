package com.rashad.tetrix;

import java.util.Random;

public class Block {

	// All possible shapes
	enum TetrisBlock { NOSHAPE, ZSHAPE, SSHAPE, LINESHAPE, TSHAPE, SQUARESHAPE, JSHAPE, LSHAPE };

	private TetrisBlock thisBlock;
	private int squareCoordinates[][];
	private int[][][] coordinatesTable;

	public Block() {
	    squareCoordinates = new int[4][2];
	    setShape(TetrisBlock.NOSHAPE); // default is NOSHAPE
	}

	public void setShape(TetrisBlock block) {
	    
		// Coordinates defining the shape of the 7 types of tetris blocks
		coordinatesTable = new int[][][] {
	        { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
	        { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
	        { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
	        { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
	        { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
	        { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
	        { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
	        { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
	    };
	
	    // Set coordinates of block
	    for (int i = 0; i < 4 ; i++) {
	        for (int j = 0; j < 2; ++j) {
	            squareCoordinates[i][j] = coordinatesTable[block.ordinal()][i][j];
	        }
	    }
	    thisBlock = block;
	}
	
	// Getters and setters 
	private void setX(int index, int x) { squareCoordinates[index][0] = x; }
	private void setY(int index, int y) { squareCoordinates[index][1] = y; }
	public int getX(int index) { return squareCoordinates[index][0]; }
	public int getY(int index) { return squareCoordinates[index][1]; }
	public TetrisBlock getShape()  { return thisBlock; }

	public void setRandomShape()
	{
	    Random r = new Random();
	    int x = Math.abs(r.nextInt()) % 7 + 1;
	    TetrisBlock[] values = TetrisBlock.values(); 
	    setShape(values[x]);
	}
	
	public int minX()
	{
	  int m = squareCoordinates[0][0];
	  for (int i=0; i < 4; i++) {
	      m = Math.min(m, squareCoordinates[i][0]);
	  }
	  return m;
	}
	
	
	public int minY() 
	{
	  int m = squareCoordinates[0][1];
	  for (int i=0; i < 4; i++) {
	      m = Math.min(m, squareCoordinates[i][1]);
	  }
	  return m;
	}
	
	public Block rotateLeft() 
	{
	    if (thisBlock == TetrisBlock.SQUARESHAPE)
	        return this;
	
	    Block rotatedBlock = new Block();
	    rotatedBlock.thisBlock = thisBlock;
	
	    for (int i = 0; i < 4; ++i) {
	        rotatedBlock.setX(i, getY(i));
	        rotatedBlock.setY(i, -getX(i));
	    }
	    return rotatedBlock;
	}
	
	public Block rotateRight()
	{
	    if (thisBlock == TetrisBlock.SQUARESHAPE)
	        return this;
	
	    Block rotatedBlock = new Block();
	    rotatedBlock.thisBlock = thisBlock;
	
	    for (int i = 0; i < 4; ++i) {
	        rotatedBlock.setX(i, -getY(i));
	        rotatedBlock.setY(i, getX(i));
	    }
	    return rotatedBlock;
	}

}

package com.rashad.tetrix;

import java.awt.Color;


public class Customize{
	
	static class BoardHeight {
		static final int LARGE = 40;
	}
	
	static class BoardWidth {
		static final int LARGE = 21;
	}
	
	static class Speed {
		static final int NORMAL = 300;
		static final int FAST = 200;
		static final int SUPERFAST = 100;
	}
	
	static class Skin1 {
		static final Color COLOR[] = { 	
	    		new Color(0, 0, 0), 
				new Color(255, 69, 0), 
				new Color(0, 255, 127), 
				new Color(106, 90, 205), 
				new Color(220, 20, 60), 
				new Color(255, 215, 0), 
				new Color(255, 105, 180), 
				new Color(0, 191, 255)
	    };
		
		static Color BACKGROUND = Color.BLACK;
	}
	static class Skin2 {
		static final Color COLOR[] = { 	
				new Color(0, 0, 0), 
				new Color(255, 102, 102), 
				new Color(102, 204, 102), 
				new Color(102, 102, 204), 
				new Color(204, 204, 102), 
				new Color(204, 102, 204), 
				new Color(102, 204, 204), 
				new Color(218, 170, 0)
		};
		
		static Color BACKGROUND = Color.WHITE;
	}

}

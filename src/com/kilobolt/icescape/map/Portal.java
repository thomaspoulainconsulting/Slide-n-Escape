package com.kilobolt.icescape.map;

public class Portal {
	
	int x1, y1,x2, y2;

	public Portal(int[] position) {
		x1=position[0];
		y1=position[1];
		x2=position[2];
		y2=position[3];
	} 
	
	public int getX1(){return x1;}
	public int getY1(){return y1;}
	public int getX2(){return x2;}
	public int getY2(){return y2;}

}

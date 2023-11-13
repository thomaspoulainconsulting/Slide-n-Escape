package com.kilobolt.icescape.screens;

public class CreditCharacter {
	
	int y;
	String character;
	boolean operation;
	
	public CreditCharacter(String character, int y)
	{
		this.y=y;
		this.character=character;
		operation=true; //+
	}
	
	public int getY() { return y; }
	public void setY(int y){ this.y=y; }
	public String getCharacter() { return character; }
	public boolean getOperation() { return operation; }
	public void setOperation(boolean operation) { this.operation=operation; }

}

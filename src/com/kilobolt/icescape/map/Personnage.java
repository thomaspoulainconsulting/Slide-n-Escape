package com.kilobolt.icescape.map;

import android.content.Context;
import android.os.Vibrator;

import com.kilobolt.icescape.GameManager;
import com.kilobolt.icescape.screens.GameScreen;

public class Personnage {
	final int MOVESPEED = 40;

	private int x;
	private int y;

	private int speedX = 0;
	private int speedY = 0;
	
	private int direction;
	private int nbCoups=0;
	private int kilometrage=0;
	private boolean moving=false;
	Vibrator vibrator = (Vibrator) GameManager.context.getSystemService(Context.VIBRATOR_SERVICE);
	
	public Personnage(int[] coord)
	{
		this.x=coord[0];
		this.y=coord[1];
	}
	
	int GAUCHE=0;
	int DROITE=1;
	int HAUT=2;
	int BAS=3;
	
	public void update() {
		
		if(speedX != 0 || speedY != 0){
			GameScreen.getMap().moveBox(x+speedX, y+speedY, direction);
			boolean stateCase = GameScreen.getMap().isCollision(x+speedX, y+speedY, direction);
			
			
			
			if(!stateCase && moving)
			{
				GameScreen.getMap().checkSwitchs(x+speedX, y+speedY);
				x += speedX;
				y += speedY;
				kilometrage++;
			}
			else if(stateCase) { moving=false; speedX=0; speedY=0;}
			
		}
		
	}
	
	/* 0 : Gauche
	 * 1 : Droite
	 * 2 : Haut
	 * 3 : Bas
	 * */
	public boolean move(int direction)
	{
		if(!moving) //Si on n'est pas en train de bouger
		{
			switch(direction)
			{
				case 0: speedX = -MOVESPEED; speedY=0; this.direction=GAUCHE; break;
				case 1: speedX = MOVESPEED; speedY=0; this.direction =DROITE; break;
				case 2: speedY = -MOVESPEED; speedX=0; this.direction=HAUT; break;
				case 3: speedY = MOVESPEED; speedX=0; this.direction=BAS; break;
			}
			
			nbCoups++;
			moving=true;
			
			if(GameManager.gm.getSave().retrievePrefs("Vibration")) vibrator.vibrate(50);
			
			return true;
		} else return false;
	} 
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getNbCoups() {
		return nbCoups;
	}

	public void setX(int x) { 
		this.x=x; 
	} 
	
	public void setY(int y) { this.y=y; }
	
	public int getKilometrage()
	{
		return kilometrage;
	}
	
	
	
	

}

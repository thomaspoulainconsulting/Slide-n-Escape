package com.kilobolt.icescape.map;

import java.util.Date;
import java.util.Random;


public class Boss {
	private int xDebut, yDebut;
	private int MOVE=20;
	
	private int x, fin;
	private int y;
	private boolean show=false;
	private int plan;
	private boolean direction;
	Date date, timer;
	
	@SuppressWarnings("deprecation")
	Boss(int[] coord)
	{
		this.plan=coord[0];
		
		if(plan == 0) {
			this.x=coord[1];
			this.y=coord[2];
			this.fin=coord[3];
			xDebut=x;
			yDebut=y;
		} else {
			this.y=coord[1];
			this.x=coord[2];
			this.fin=coord[3];
			xDebut=x;
			yDebut=y;
		}
		
		timer = new Date();
		timer.setSeconds(timer.getSeconds()+new Random().nextInt(1)+1);
		direction = new Random().nextBoolean();
		
		if(!direction && plan == 0) x=fin;
		if(!direction && plan == 1) y=fin;
	}
	
	@SuppressWarnings("deprecation")
	public void update() {
		
		if(plan == 0 && show){ // Horizontal
			
			if(direction) {
				if(x+2 < fin) x+=MOVE;
				else if(show){
					show=false; 
					//on lance un timer
					timer = new Date();
					timer.setSeconds(timer.getSeconds()+new Random().nextInt(3)+1);
					direction = new Random().nextBoolean();
					x=xDebut;
					y=yDebut;
					if(!direction) x=fin;
				}
			}
			else 
			{
				if(x-2 > xDebut) x-=MOVE;
				else if(show){
					show=false; 
					//on lance un timer
					timer = new Date();
					timer.setSeconds(timer.getSeconds()+new Random().nextInt(3)+1);
					direction = new Random().nextBoolean();
					x=xDebut;
					y=yDebut;
					if(!direction) x=fin;
				}
			}
			
			
		}else if(plan == 1 && show) //Vertical
		{
			if(direction)
			{
				if(y+2 < fin) y+=MOVE;
				else if(show){
					show=false;
					timer = new Date();
					timer.setSeconds(timer.getSeconds()+new Random().nextInt(3)+1);
					direction = new Random().nextBoolean();
					x=xDebut;
					y=yDebut;
					if(!direction) y=fin;
				}
			}
			else  {
				if(y-2 > yDebut) y-=MOVE;
				else if(show){
					show=false;
					timer = new Date();
					timer.setSeconds(timer.getSeconds()+new Random().nextInt(3)+1);
					direction = new Random().nextBoolean();
					x=xDebut;
					y=yDebut;
					if(!direction) y=fin;
				}
			}
		}
		
		if(!show)
		{
			//si le timer est terminé on remontre
			if(new Date().compareTo(timer) == 1) show=true;
		}
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public void setX(int x) { this.x=x; }
	
	public boolean getShow(){return show;}
}

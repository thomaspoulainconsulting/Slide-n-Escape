package com.kilobolt.icescape.map;


import com.kilobolt.framework.Graphics;
import com.kilobolt.icescape.Assets;


public class Eclair {
	private int x, fin;
	private int y;
	private int plan;
	private boolean direction=false; //true = autresens
	
	Eclair(int[] coord)
	{
		this.plan=coord[0];
		
		
		if(plan == 2) { plan=0; direction=true; }
		else if(plan == 3) { plan=1; direction=true; }
		
		if(plan == 0) {
			this.x=coord[1];
			this.y=coord[2];
			this.fin=coord[3];
		} else {
			this.y=coord[1];
			this.x=coord[2];
			this.fin=coord[3];
		}
	}
	
	public void show(Graphics g, Map map)
	{
		//On affiche sur toute la ligne/colonne tant qu'on ne rencontre pas de caisse ou de bloc
		if(plan == 0)
		{
			if(!direction) //GAUCHE DROITE
			{
				for(int i=x;i<=fin;i++)
				{
					//Log.i("iceLog", "y"+String.valueOf(y)+"i"+String.valueOf(i));
					
					if((map.getMap()[y][i] != 'm' && map.getMap()[y][i+1] != '1' && map.getMap()[y][i+1] != 'm' && map.getMap()[y][i+1] != '5'))
						g.drawImage(Assets.eclair2, i*40, y*40);
					else if(i == x && map.getMap()[y][i+1] == 'm')
					{
						g.drawImage(Assets.eclair2, i*40, y*40);
						fin=i;
					}else 
						fin=i;
				}
				if(x!=fin) g.drawImage(Assets.eclair2, (fin)*40, y*40);
			}
			else { //DROITE GAUCHE
				
				if(x != fin) g.drawImage(Assets.eclair2, x*40, y*40);
				for(int i=fin;i>x;i--)
				{
					if((map.getMap()[y][i] != 'm' && map.getMap()[y][i-1] != '1' && map.getMap()[y][i-1] != 'm' && map.getMap()[y][i-1] != '5') || i==3 && map.getMap()[y][i-1] == 'm')
						g.drawImage(Assets.eclair2, i*40, y*40);
					else  x=i;
					
				}
				
				
			}
		}
		else
		{
			if(!direction) //HAUT BAS
			{
				
				for(int i=y;i<fin;i++)
				{
					if(map.getMap()[i][x] != 'm' && map.getMap()[i+1][x] != '1' && map.getMap()[i+1][x] != 'm' && map.getMap()[i+1][x] != '5')
						g.drawImage(Assets.eclair, x*40, i*40);
					else if(map.getMap()[i+1][x] == 'm' || map.getMap()[i+1][x] == '1' || map.getMap()[i+1][x] == '5' || map.getMap()[i][x] == 'm') fin=i;
				}
				if(y != fin || (y == 1 && map.getMap()[y][x] != 'm')) g.drawImage(Assets.eclair, x*40, fin*40);
			}
			else //BAS HAUT 
			{
				for(int i=y;i<fin;i++)
				{
					if(map.getMap()[i][x] != 'm' && map.getMap()[i+1][x] != '1' && map.getMap()[i+1][x] != 'm' && map.getMap()[i+1][x] != '5')
						g.drawImage(Assets.eclair, x*40, i*40);
					else if(map.getMap()[i+1][x] == 'm' || map.getMap()[i+1][x] == '1' || map.getMap()[i+1][x] == '5' || map.getMap()[i][x] == 'm') fin=i;
				}
				if(y != fin) g.drawImage(Assets.eclair, x*40, fin*40);
				
			}
			
			
			
			
			
		}
	}
	
	public int getX(){return x*40;}
	public int getY(){return y*40;}
	public int getPlan() { return plan; }
	public int getFin() { return fin*40; }
	public boolean getDirection() { return direction; }


}

package com.kilobolt.icescape.map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import com.kilobolt.framework.Graphics;
import com.kilobolt.icescape.Assets;
import com.kilobolt.icescape.GameManager;
import com.kilobolt.icescape.screens.GameScreen;

public class Map {
	
	private int startX, startY;
	private int width, height;
	private int nbSwitch=0, nbSwitchPassedOn=0;
	private Vector<Integer> CoordsSwitch = new Vector<Integer>();
	private Vector<Integer> CoordsCaisse = new Vector<Integer>();
	private Vector<Boolean> tabMovedBox = new Vector<Boolean>();
	private Vector<Boss> vectorBoss = new Vector<Boss>();
	private Vector<Portal> vectorPortal = new Vector<Portal>();
	private Vector<Eclair> vectorEclair = new Vector<Eclair>();
	
	
	private int nbCoupsEtoile[] = new int[3];
	private char map[][] = new char[12][20];
	private int world, level;
	private ArrayList<Tile> tilearray = new ArrayList<Tile>();
	
	private int ligneBoss, col1Boss, col2Boss, direction;
	
	int GAUCHE=0;
	int DROITE=1;
	int HAUT=2;
	int BAS=3;
	
	/*
	 * Structure du fichier level.world.level.txt
	 * 
	 * nbredecoup 3etoile
	 * nbredecoup 2etoile
	 * nbredecoup 1etoile
	 * map [tableau de x par y]
	 */
	
	public Map(int world, int level, ArrayList<Tile> tilearray)
	{
		try {
			this.world=world;
			this.level=level;
			this.tilearray=tilearray;
			
			BufferedReader buffer = new BufferedReader(new InputStreamReader(GameManager.context.getAssets().open("levels/"+world+"/level."+world+"."+level+".txt")));
			

			String ligne="";
			char value;
			int i=0, j=0;
			int index=0;
			int nbPortal=0;
			boolean boss=false;
			boolean portal=false;
			boolean eclair=false;
			String bossValue="", portalValue="", eclairValue="";
			
			while ((ligne=buffer.readLine())!=null){
				if(i == 0) nbCoupsEtoile[0]=Integer.parseInt(ligne);
				else if(i == 1) nbCoupsEtoile[1]=Integer.parseInt(ligne);
				else if(i == 2) nbCoupsEtoile[2]=Integer.parseInt(ligne);
				else {
					
					for(j=0;j<ligne.length();j++)
					{ 
						if(ligne.charAt(j) == ' ') value='o';
						else value = ligne.charAt(j);
						
						if(value == '#' || boss) //Boss
						{
							boss=true;
							if(value != '#') bossValue +=value;
						}
						else if(value == '!' || portal)
						{
							portal=true;
							if(value != '!') portalValue += value;  
						}
						else if(value == '%' || eclair)
						{
							eclair=true;
							if(value != '%') eclairValue +=value;
						}
						else{  
						
							//Lorsque l'on a un interrupteur, on inscrit l'indice qui correspond à l'ajout d'un tile d'interrupteur dans tilearray
							if(value == 'l') { nbSwitch++; CoordsSwitch.add(Integer.valueOf(index)); } 
							
							//Même chose avec les caisses
							if(value == 'm') { CoordsCaisse.add(Integer.valueOf(index)); tabMovedBox.add(Boolean.valueOf(false));}
							 
							map[i-3][j]=value;
							tilearray.add(new Tile(j, i-3, map[i-3][j]));
							
							
							if(map[i-3][j] == '9') { startY=i-3; startX=j; }
							
							index++;
						}
					}
					    
					if(boss)
					{
						String result[] = bossValue.split(":");
						
						direction=Integer.parseInt(result[0]);
						ligneBoss = Integer.parseInt(result[1]);
						
						String result2[] = result[2].split("-");
						
						col1Boss = Integer.parseInt(result2[0]);
						col2Boss = Integer.parseInt(result2[1]);
						
						int position[] = new int[4];
						
						position[0]=direction;
						position[1]=col1Boss*40;
						position[2]=ligneBoss*40;
						position[3]=col2Boss*40;
						
						  
						vectorBoss.add(new Boss(position));
						boss=false;
						bossValue="";
					}
					
					if(portal)
					{
						nbPortal++;
						String result[] = portalValue.split(":");
						
						String coord1[] = result[0].split("-");
						String coord2[] = result[1].split("-"); 
						
						int position[] = new int[4];
						
						position[0]=Integer.parseInt(coord1[0]);
						position[1]=Integer.parseInt(coord1[1]);
						position[2]=Integer.parseInt(coord2[0]);
						position[3]=Integer.parseInt(coord2[1]);
						
						vectorPortal.add(new Portal(position));
						
						//on parcourt les tile pour y mettre les images de portals
						for(int g=0;g<tilearray.size();g++)
						{
							if((tilearray.get(g).getTileX() == position[0]*40 && tilearray.get(g).getTileY() == position[1]*40) || (tilearray.get(g).getTileX() == position[2]*40 && tilearray.get(g).getTileY() == position[3]*40))
							{
								if(nbPortal == 1) tilearray.get(g).setTileImage(Assets.portal1);
								else if(nbPortal == 2) tilearray.get(g).setTileImage(Assets.portal2);
								else if(nbPortal == 3) tilearray.get(g).setTileImage(Assets.portal3);
								else if(nbPortal == 4) tilearray.get(g).setTileImage(Assets.portal4);
							}
						}
						
						portal=false;
						portalValue="";
					}
					
					if(eclair)
					{
						String result[] = eclairValue.split(":");
						
						direction=Integer.parseInt(result[0]);
						ligneBoss = Integer.parseInt(result[1]);
						
						String result2[] = result[2].split("-");
						
						col1Boss = Integer.parseInt(result2[0]);
						col2Boss = Integer.parseInt(result2[1]);
						
						int position[] = new int[4];
						
						position[0]=direction; 
						position[1]=col1Boss;
						position[2]=ligneBoss;
						position[3]=col2Boss;
						
						vectorEclair.add(new Eclair(position));
						eclair=false;
						eclairValue="";
					}
				
				}
				i++;
			}
			
			buffer.close();
			
			width=20;
			height=i-3;
			
			
		} catch (Exception e) {
			//Log.i("iceLog", "error loadlevel "+e.toString());
			//e.printStackTrace();
			
		}
	}
	
	public static int getEtoiles(int world, int level)
	{
		return GameManager.gm.getSave().retrieve(world+"-"+level+"-etoile");
	}
	
	public int getMinimalMoves()
	{
		return nbCoupsEtoile[0];
	}
	
	public static int[] getCoups(int world, int level)
	{
		int coups[] = new int[3];
		
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(GameManager.context.getAssets().open("levels/"+world+"/level."+world+"."+level+".txt")));
			
			String ligne="";
			int i=0;
			
			
			while ((ligne=buffer.readLine())!=null){
				if(i == 0) coups[0]=Integer.parseInt(ligne); // 3etoile
				else if(i == 1) coups[1]=Integer.parseInt(ligne); // 2etoile
				else if(i == 2) { coups[2]=Integer.parseInt(ligne); return coups; } // 2etoile
				i++;
			}
		}catch(Exception e){e.printStackTrace();}
		
		return coups;
	}
	
	public int[] getStartPosition()
	{
		int position[] = new int[2];
		
		position[0]=startX*40;
		position[1]=startY*40;
		
		return position;
	}
	
	public int[] getBossPosition()
	{
		int position[] = new int[4];
		
		position[0]=direction;
		position[1]=col1Boss*40;
		position[2]=ligneBoss*40;
		position[3]=col2Boss*40;
		
		return position;
	}
	
	
	public int getBeginningH()
	{
		int i=0;
		for(i=0;i<20;i++)
		{
			if(map[5][i] == 'o' || map[5][i] == '1' || map[5][i] == 'm' || map[5][i] == '9' || map[5][i] == '7' || map[5][i] == 'p' || map[5][i] == '3' || map[5][i] == 'l') break;
		}
		return i;
	}
	
	public int getEndH()
	{
		int i=0;
		for(i=10;i<20;i++)
		{
			if(map[5][i] == '4') break;
		}
		return i;
	}
	
	public int getBeginningV()
	{
		int i=0;
		for(i=0;i<5;i++)
		{
			if(map[i][6] == 'o' || map[i][6] == '1' || map[i][6] == 'm' || map[i][6] == '9' || map[i][6] == 'p' || map[i][6] == '3' || map[i][6] == 'l' || map[i][6] == '7') break;
		}
		return i;
	}
	
	public int getEndV()
	{
		int i=0;
		for(i=5;i<12;i++)
		{
			if(map[i][6] == '8') break;
		}
		return i;
	}
	
	public boolean isEclair()
	{
		if(vectorEclair.size() > 0) return true;
		else return false;
	}
	
	public void drawEclair(Graphics g)
	{
		for(int i=0;i<vectorEclair.size();i++)
		{
			vectorEclair.elementAt(i).show(g, this);
		}
	}
	
	
	public boolean isEclair(int x, int y)
	{ 
		x /= 40;
		y /= 40;
		
		for(int i=0;i<vectorEclair.size();i++)
		{
			if(vectorEclair.elementAt(i).getPlan() == 0){ //Ligne
				if(vectorEclair.elementAt(i).getDirection() == true && Math.floor(vectorEclair.elementAt(i).getY()/40) == y && x >= vectorEclair.elementAt(i).getX()/40 && x<= vectorEclair.elementAt(i).getFin()/40) return true;
				else if(vectorEclair.elementAt(i).getDirection() == false && Math.floor(vectorEclair.elementAt(i).getY()/40) == y && x >= vectorEclair.elementAt(i).getX()/40 && x <= vectorEclair.elementAt(i).getFin()/40) return true;
			}else if(vectorEclair.elementAt(i).getPlan() == 1){ //Colonne
				if(vectorEclair.elementAt(i).getDirection() == false  && Math.floor(vectorEclair.elementAt(i).getX()/40) == x && y >= Math.floor(vectorEclair.elementAt(i).getY()/40) && y <= vectorEclair.elementAt(i).getFin()/40) { //HAUT vers BAS
				
				return true;
				}
				else //BAS VERS HAUT 
				{
					if(vectorEclair.elementAt(i).getDirection() == true  && Math.floor(vectorEclair.elementAt(i).getX()/40) == x && y >= Math.floor(vectorEclair.elementAt(i).getY()/40) && y < vectorEclair.elementAt(i).getFin()/40)
						return true;
				}
				
			}
		}
		return false;
		
	}
	
	public char[][] getMap(){ return map; }   
	
	public boolean isPortal(int x, int y) {
		
		x/=40;
		y/=40;
		
		if(map[y][x] == 'p') return true;
		else return false;
	}
	
	public int[] getPositionPortal(int x, int y)
	{
		x/=40;
		y/=40;
		
		int position[] = new int[2];
		
		//on parcourt les portals, si on tombe sur un qui a la meme coord, on envoit le x, y associé
		
		for(int i=0;i<vectorPortal.size();i++)
		{
			if(vectorPortal.elementAt(i).getX1() == x && vectorPortal.elementAt(i).getY1() == y)
			{
				position[0]=vectorPortal.elementAt(i).getX2()*40;
				position[1]=vectorPortal.elementAt(i).getY2()*40;
			}
			else if(vectorPortal.elementAt(i).getX2() == x && vectorPortal.elementAt(i).getY2() == y)
			{
				position[0]=vectorPortal.elementAt(i).getX1()*40;
				position[1]=vectorPortal.elementAt(i).getY1()*40;
			}
		}
		
		return position;
		
	}
	
	public Vector<Boss> getBoss(){ return vectorBoss; }
	
	public boolean isBoss() { if(vectorBoss.size() > 0) return true; else return false; }
	
	public boolean isEnd()
	{
		int x=GameScreen.getPerso().getX()/40;
		int y=GameScreen.getPerso().getY()/40;
		
		if(x >= 0 && x < width && y >= 0 &&  y < height) {
			if(map[y][x] == '3' && nbSwitchPassedOn == nbSwitch) return true;
			else return false;
		}
		else return false;
	}
	
	public boolean isEnd(int x, int y)
	{
		x /= 40;
		y /= 40;
		
		if(x >= 0 && x < width && y >= 0 &&  y < height && map[y][x] == '3') return true;
		else return false;
	}
	
	public boolean isHole()
	{
		if(map[GameScreen.getPerso().getY()/40][GameScreen.getPerso().getX()/40] == '7') return true;
		else return false;
	}
	
	public boolean isHole(int x, int y)
	{
		x /= 40;
		y /= 40;
		
		if(x >= 0 && x < width && y >= 0 &&  y < height && map[y][x] == '7') return true;
		else return false;
	}
	
	public boolean isCollisionWall(int x, int y)
	{
		x/=40;
		y/=40;
		
		if(((world==6 && level==4) || (world==6 && level==12)) && x >= 0 && x < width && y >= 0 &&  y < height){ //Pour le level de gauthier
			if(map[y][x] == '1' || map[y][x] == '5' || map[y][x] == '2' || map[y][x] == '4' || map[y][x] == '6' || map[y][x] == '8')
				return true;
			else return false;
		} else if(x > 0 && x < width && y > 0 &&  y < height) {
			if(map[y][x] == '1' || map[y][x] == '5' || map[y][x] == '2' || map[y][x] == '4' || map[y][x] == '6' || map[y][x] == '8')
				return true;
			else return false;
		}
		else return true;
		
		
	}
	
	public boolean isCollisionCaisse(int x, int y, int direction)
	{
		x/=40;
		y/=40;
		
//		int x_o=x;
//		int y_o=y;
		int hori=0, verti=0;
		
		if(direction == GAUCHE && x-1 > 0) { x-=1; hori--; }
		else if(direction == DROITE && x+1 < width) { x+=1; hori++; }
		else if(direction == HAUT && y-1 > 0) { y-=1; verti--; }
		else if(direction == BAS && y+1 < height) { y+=1; verti ++; }
		
		//Log.i("iceLog", String.valueOf(map[y][x]) +"-"+ String.valueOf(x)+"--"+String.valueOf(y));
		
		for(int i=0;i<CoordsCaisse.size();i++) 
		{
			if(tabMovedBox.get(i) == true) {

				//Log.i("iceLog", String.valueOf(tilearray.get(CoordsCaisse.elementAt(i)).getTileY()/40+verti)+" -- "+String.valueOf(tilearray.get(CoordsCaisse.elementAt(i)).getTileX()/40+hori));
			if(x > 0 && x < width && y > 0 &&  y < height && tilearray.get(CoordsCaisse.elementAt(i)).getTileY()/40+verti == y+verti && tilearray.get(CoordsCaisse.elementAt(i)).getTileX()/40+hori == x+hori && map[tilearray.get(CoordsCaisse.elementAt(i)).getTileY()/40+verti][tilearray.get(CoordsCaisse.elementAt(i)).getTileX()/40+hori] == 'm')
				return true;
			else
				return false;
			}
		}
		
		return false;
		
	}
	
	public boolean isCollision(int x, int y, int direction)
	{
		x/=40;
		y/=40;  
		
		
		if(((world==6 && level==4) || (world==6 && level==12)) && x >= 0 && x < width && y >= 0 &&  y < height){ //Pour le level de gauthier
			if(map[y][x] == '1' || map[y][x] == '5' || map[y][x] == '2' || map[y][x] == '4' || map[y][x] == '6' || map[y][x] == '8' || map[y][x] == 'm')
				return true;
			else return false;
		} else if(x > 0 && x < width && y > 0 &&  y < height) {
			if(map[y][x] == '1' || map[y][x] == '5' || map[y][x] == '2' || map[y][x] == '4' || map[y][x] == '6' || map[y][x] == '8' || map[y][x] == 'm')
				return true;
			else return false;
		}
		else return true;
	} 
	
	 
	public boolean isSwitch(int x, int y)
	{
		x /=40;
		y /=40;
		
		
		if(x >= 0 && x < width && y >= 0 &&  y < height && map[y][x] == 'l') return true;
		else return false;
	}


	public void checkSwitchs(int x, int y)
	{
		x/=40;
		y/=40;
		
		if(x >= 0 && x < width && y >= 0 &&  y < height && map[y][x] == 'l') {
			
			//On change l'image du tile
			
			for(int i=0;i<CoordsSwitch.size();i++)
			{
				if(tilearray.get(CoordsSwitch.elementAt(i)).getTileX() == x*40 && tilearray.get(CoordsSwitch.elementAt(i)).getTileY() == y*40 && tilearray.get(CoordsSwitch.elementAt(i)).getTileImage() == Assets.switch_off) {
					nbSwitchPassedOn++;
					tilearray.get(CoordsSwitch.elementAt(i)).setTileImage(Assets.switch_on);
					break; 
				}
			}
		}
		
		
	}
	
	public void moveBox(int x, int y, int direction)
	{
		x/=40;
		y/=40;
		
		int x_o=x;
		int y_o=y;

		if(x >= 0 && x < width && y >= 0 &&  y < height) {
			for(int i=0;i<CoordsCaisse.size();i++)
			{
				if(tabMovedBox.get(i) == false && tilearray.get(CoordsCaisse.elementAt(i)).getTileX() == x*40 && tilearray.get(CoordsCaisse.elementAt(i)).getTileY() == y*40 && tilearray.get(CoordsCaisse.elementAt(i)).getTileImage() == Assets.caisse) {


			 		//on vérifie si on peut bien bouger la caisse (si ça ne sort pas du cadre
					boolean error=false;
					
					if(direction == GAUCHE && x-1 > 0) x-=1;
					else if(direction == DROITE && x+1 < width) x+=1;
			   		else if(direction == HAUT && y-1 > 0) y-=1;
					else if(direction == BAS && y+1 < height) y+=1;
					else error=true;
					
					
					
					if(!error && (map[y][x] == 'o' || map[y][x] == '3' || map[y][x] == '9' || map[y][x] == 'l' || map[y][x] == '7'))
					{
						tabMovedBox.set(i, true); //on ne peut bouger la caisse qu'une seule fois
						
						if(map[y][x] == 'l') nbSwitchPassedOn++; 
						
						//on change l'état de la caisse au niveau de la map
						map[y_o][x_o] = 'o';
						map[y][x] = 'm';
						
						//on remplace l'image de la caisse par de la glace
						tilearray.get(CoordsCaisse.elementAt(i)).setTileImage(Assets.ice);
												
						
						//on remplace la glace par la caisse
						for(int j=0;j<tilearray.size();j++)
						{
							if(tilearray.get(j).getTileX() == x*40 && tilearray.get(j).getTileY() == y*40)
							{
								tilearray.get(j).setTileImage(Assets.caisse);
								break;
							}
						}
						 
					}
					
					break; 
				}
			}
		}
		
	}
	
	public void saveScores(int score, int etoile, int coups, float temps)
	{
		
		GameManager.gm.getSave().save(world+"-"+level, 10);
		
		if(score > GameManager.gm.getSave().retrieve(world+"-"+level+"-score"))
			GameManager.gm.getSave().save(world+"-"+level+"-score", score);
		
		if(etoile > GameManager.gm.getSave().retrieve(world+"-"+level+"-etoile"))
			GameManager.gm.getSave().save(world+"-"+level+"-etoile", etoile);
		
		if(coups < GameManager.gm.getSave().retrieve(world+"-"+level+"-coups"))
			GameManager.gm.getSave().save(world+"-"+level+"-coups", coups);
		
		if(temps < GameManager.gm.getSave().retrieveTime(world+"-"+level+"-temps"))
			GameManager.gm.getSave().save(world+"-"+level+"-temps", temps);
	}
	
	public static boolean isCompleted(int world, int level)
	{
		if(GameManager.gm.getSave().retrieve(world+"-"+level) > 0) return true;
		else return false;
	}

}

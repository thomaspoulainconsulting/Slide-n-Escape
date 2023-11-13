package com.kilobolt.icescape.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Graphics.ImageFormat;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Input.TouchEvent;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.implementation.AndroidGraphics;
import com.kilobolt.icescape.Animation;
import com.kilobolt.icescape.Assets;
import com.kilobolt.icescape.ClicService;
import com.kilobolt.icescape.GameManager;
import com.kilobolt.icescape.R;
import com.kilobolt.icescape.map.Boss;
import com.kilobolt.icescape.map.Map;
import com.kilobolt.icescape.map.Personnage;
import com.kilobolt.icescape.map.Tile;



public class GameScreen extends Screen {
	enum GameState {Running, Paused, Finish, GameOver}
	GameState state = GameState.Running;
	
	private static Personnage perso;
	private Image currentSprite, character, flat_hand;
	private Image  currentSpriteBasReflet = Assets.bas_reflet, currentSpriteHautReflet=Assets.haut_reflet, currentSpriteGaucheReflet=Assets.gauche_reflet, currentSpriteDroiteReflet=Assets.droite_reflet;
	private Animation animBas, animHaut, animGauche, animDroite;
	
	private static Map map;
	long tempsStart, pauseStart, pauseEnd, tempsEnd;
	double finale;
	int etoileGagne;
	private ArrayList<Tile> tilearray = new ArrayList<Tile>();
	int world, level;
	int xPrec=0, yPrec=0;
	Paint paint, paint2;
	int x=220;
	private Vector<Boss> vectorBoss = new Vector<Boss>();
	boolean touchedByBoss=false, touchedByEclair=false;
	int opacity = 255;
	 
	int GAUCHE=0;
	int DROITE=1;
	int HAUT=2;
	int BAS=3;
	

	public GameScreen(Game game, int world, int level) {
		super(game);
		
		this.world=world;
		this.level=level;
		
		
		map = new Map(world, level, tilearray);
		perso = new Personnage(map.getStartPosition());
		if(map.isBoss()) vectorBoss = map.getBoss();
		  
		character = Assets.bas;
		animBas = new Animation();
		animBas.addFrame(character, 150);
		currentSprite = animBas.getImage();
		animGauche = new Animation();
		animGauche.addFrame(Assets.gauche, 50);
		animDroite = new Animation();
		animDroite.addFrame(Assets.droite, 50);
		animHaut = new Animation();
		animHaut.addFrame(Assets.haut, 50);
		
		Graphics g = game.getGraphics();
		flat_hand = g.newImage("sprites/flat_hand.png", ImageFormat.RGB565);
		 
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTypeface(Assets.font);
		paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
		
		
		tempsStart=System.nanoTime();
		
		//Utilisé pour les messages de défillement
		Typeface tf = Typeface.createFromAsset(GameManager.context.getAssets(), "fonts/Dimis.ttf");
		paint2 = new Paint();
		paint2.setColor(Color.BLACK); 
		paint2.setTypeface(tf);
		paint2.setStyle(Paint.Style.FILL); 
        paint2.setAntiAlias(true);
        paint2.setAlpha(255);
        
        if(GameManager.gm.getSave().retrievePrefs("Son")) Assets.playMusic();
        
        if(x > -800 && world == 2 && level == 4) {
        	x = 850;
        } else if(x > -800 && world == 4 && level == 1) {
        	x = 850;
        }
	}    
	

 
/* UPDATE 
 *  Running / Paused / Finish / Gameover
 *  
 *  Gestion des entrées et du dessin de l'UI
 * */
	
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == GameState.Running)
			updateRunning(touchEvents);
		else if (state == GameState.Paused)
			updatePaused(touchEvents);
		else if(state == GameState.Finish)
			updateFinish(touchEvents);
		else if(state == GameState.GameOver)
			updateGameOver(touchEvents);
	}


	private void updateRunning(List<TouchEvent> touchEvents) {

		int len = touchEvents.size();
		
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				 xPrec = event.x;
				 yPrec = event.y;
			}

			if (event.type == TouchEvent.TOUCH_UP) {

				if (AndroidGraphics.inBounds(event, 0, 0, 40, 40)) {
					ClicService.clic();
					pause();
				}
				else {
					
					int xprime = event.x;
					int yprime = event.y;
					if(xPrec == xprime && yPrec == yprime && xPrec != 0 && yPrec != 0) 
					{ 
						//si on touche seulement
					}
					else {
					 
						if(yprime == yPrec) yprime++;
						double angle = 90 - Math.toDegrees(Math.atan((Math.abs(xprime-xPrec))/(Math.abs(yprime-yPrec))));
						
					
						if(xprime > xPrec && angle >= 0 && angle < 45 && !map.isCollisionWall(perso.getX()+40, perso.getY()) /*&& !map.isCollisionCaisse(perso.getX(),  perso.getY(), DROITE)*/) 
							{ if(perso.move(DROITE)) currentSprite=animDroite.getImage(); }
						else if(xprime < xPrec && angle >= 0 && angle < 45  && !map.isCollisionWall(perso.getX()-40, perso.getY())/* && !map.isCollisionCaisse(perso.getX(), perso.getY(), GAUCHE)*/)
							{ if(perso.move(GAUCHE)) currentSprite=animGauche.getImage(); }
						else if(yprime > yPrec && angle >= 45  && !map.isCollisionWall(perso.getX(), perso.getY()+40) /*&& !map.isCollisionCaisse(perso.getX(), perso.getY(), BAS)*/)
							{ if(perso.move(BAS)) currentSprite=animBas.getImage(); }
						else if(yprime < yPrec && angle >= 45  && !map.isCollisionWall(perso.getX(), perso.getY()-40) /*&& !map.isCollisionCaisse(perso.getX(), perso.getY(), HAUT)*/)
							{ if(perso.move(HAUT)) currentSprite=animHaut.getImage(); }
					}
				}
				
			}
			

		}
		
		// On vérifie si le perso a atteint la sortie + Calcul du score
		if(map.isEnd())	{ 
			
			perso.setX(-40); // on cache le sprite 
			
			int etoile[] = Map.getCoups(world, level);
			
			if(perso.getNbCoups() > etoile[1] && perso.getNbCoups() <= etoile[2]) 
				etoileGagne=1;
			else if(perso.getNbCoups() > etoile[0] && perso.getNbCoups() <= etoile[1])
				etoileGagne=2;
			else if(perso.getNbCoups() <= etoile[0])
				etoileGagne=3;
			else etoileGagne=0;
			
			if(finale == 10) finale+=0.01;
			int nbCaseParcourue = perso.getKilometrage();
			if(nbCaseParcourue >= 100) nbCaseParcourue=99;
			
			int score = (int) (10/finale*8+etoileGagne*2.5*world*(10/(perso.getNbCoups()*perso.getNbCoups()))*(1/(Math.exp(nbCaseParcourue))))*10;
			
			
			state=GameState.Finish; 
			
			//Débloquage des achievements
			if(etoileGagne == 1) GameManager.gm.unlock(R.string.achievement_finish_a_level_with_1_star);
			if(etoileGagne == 2) GameManager.gm.unlock(R.string.achievement_finish_a_level_with_2_stars);
			if(etoileGagne == 3) GameManager.gm.unlock(R.string.achievement_finish_a_level_with_3_stars);
			
			/* On enregistre le score*/ 
			map.saveScores(score, etoileGagne, perso.getNbCoups(), (float)finale);
		}
		else if(map.isHole())
		{
			perso.setX(-40); // on cache le sprite
			state=GameState.GameOver;
			GameManager.gm.unlock(R.string.achievement_fall_in_a_hole);
		}
		else if(map.isPortal(perso.getX(), perso.getY()))
		{
			//on bouge le sprite vers le deuxieme portail
			int position[] = map.getPositionPortal(perso.getX(),  perso.getY());
			
			perso.setX(position[0]);
			perso.setY(position[1]);
		}
		else if(map.isEclair(perso.getX(), perso.getY())) 
		{
			state=GameState.GameOver;
			touchedByEclair=true;
			perso.setX(-40); // on cache le sprite
			
			GameManager.gm.unlock(R.string.achievement_toast_by_the_lightning);
		}
		
		
		perso.update();
		animate();
		
		//on vérifie si le perso à touché un boss et on update le boss
		for(int i=0;i<vectorBoss.size();i++) {
			if(vectorBoss.elementAt(i).getX()/40 == perso.getX()/40 && vectorBoss.elementAt(i).getY()/40 == perso.getY()/40 && vectorBoss.elementAt(i).getShow()) {
				
				perso.setX(-40); // on cache le sprite
				vectorBoss.elementAt(i).setX(-40);
				state=GameState.GameOver;
				touchedByBoss=true;
			} else
				vectorBoss.elementAt(i).update(); 
		}
		
	}  

	private void updatePaused(List<TouchEvent> touchEvents) {

		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				
				if (AndroidGraphics.inBounds(event, 480, 125, 120, 50)) { //Resume
					ClicService.clic();
					resume(); 
				}
				else if (AndroidGraphics.inBounds(event, 480, 205, 120, 50)) { //Retour
					ClicService.clic();
					nullify();
					game.setScreen(new LevelScreen(game, world));
				}
				else if(AndroidGraphics.inBounds(event, 480, 275, 120, 50)) { //Retry
					ClicService.clic();
					nullify();
					game.setScreen(new GameScreen(game, world, level));
				}
			}
		}
	}
	
	
	
	private void updateFinish(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				
				if (AndroidGraphics.inBounds(event, 470, 265, 130, 60)) {
					
					ClicService.clic();
					
					if(level+1 <= GameManager.NBLEVEL) {
						nullify();
						
						game.setScreen(new GameScreen(game, world, level+1));
						
						return;
					}else if(world <= 5) {
						
						switch(world) {
							case 1: GameManager.gm.unlock(R.string.achievement_world_1_finished);
							break;
							case 2: GameManager.gm.unlock(R.string.achievement_world_2_finished);
							break;
							case 3: GameManager.gm.unlock(R.string.achievement_world_3_finished);
							break;
							case 4: GameManager.gm.unlock(R.string.achievement_world_4_finished);
							break;
							case 5: GameManager.gm.unlock(R.string.achievement_world_5_finished);
							break;
							case 6: GameManager.gm.unlock(R.string.achievement_world_6_finished);
							break;
						}
						
						GameManager.gm.increment(R.string.achievement_finish_all_worlds);
						
						game.setScreen(new LevelScreen(game, world+1)); 
					}
					else if(world == 6) {
						game.setScreen(new ScenarioScreen(game, false));
					}
					
				}
				else if(AndroidGraphics.inBounds(event, 230, 280, 120, 60)) {
					ClicService.clic();
					nullify();
					game.setScreen(new LevelScreen(game, world));
					return;
				}
				else if(AndroidGraphics.inBounds(event, 480, 116, 129, 60)) {
					ClicService.clic();
					nullify();
					game.setScreen(new GameScreen(game, world, level));
					return;
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents)
	{
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				
				if(AndroidGraphics.inBounds(event, 220, 275, 126, 55)) {
					ClicService.clic();
					nullify();
					game.setScreen(new LevelScreen(game, world));
					return;
				}else if (AndroidGraphics.inBounds(event, 480, 275, 126, 50)) {
					ClicService.clic();
					nullify();
					game.setScreen(new GameScreen(game, world,level));
					return;
				}
			}
		}
	}


	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		paintTiles(g);
		
		//Reflet decor
		paintTilesReflect(g);
		
		g.drawImage(currentSprite, perso.getX()+4,perso.getY());
		
		  
		//Reflet perso
		if(!getMap().isEclair(perso.getX(), perso.getY()+40) && !getMap().isEnd(perso.getX(), perso.getY()+40) && !getMap().isHole(perso.getX(), perso.getY()+40) && !getMap().isCollision(perso.getX(), perso.getY()+40, BAS) && !getMap().isPortal(perso.getX(), perso.getY()+40) && !getMap().isSwitch(perso.getX(), perso.getY()+40))
		{
			if(currentSprite == animBas.getImage())	g.drawImage(currentSpriteBasReflet, perso.getX(),perso.getY()+40);
			else if(currentSprite == animHaut.getImage())	g.drawImage(currentSpriteHautReflet, perso.getX()+4,perso.getY()+39);
			else if(currentSprite == animGauche.getImage())	g.drawImage(currentSpriteGaucheReflet, perso.getX()+4,perso.getY()+39);
			else if(currentSprite == animDroite.getImage())	g.drawImage(currentSpriteDroiteReflet, perso.getX()+4,perso.getY()+39);
		} 
		
		if(map.isEclair()) map.drawEclair(g);
		
		if (state == GameState.Running)
		{
			drawRunningUI();
			if(x > -800 && world == 1 && level == 1 && opacity > 0) { //-800 = la taille du texte pour qu'il ai le temps de sortir de l'écrean à gauche
				g.drawImage(flat_hand, x, 120, opacity);
				x+=2;
				if(x > 300) {
					opacity-=3;
				} else if(x > 250) {
					opacity-=1;
				}
			}
			  
			if(x > -800 && world == 1 && level == 9) { 
				g.drawString(GameManager.res.getString(R.string.activeinterrupter)+" !", 100, 425, paint2, 50);
			}
			
			if(x > -800 && world == 2 && level == 4) { 
				g.drawString(GameManager.res.getString(R.string.moveboxes), x, 160, paint2, 50);
				if(x < 450 && x > 50) x-=2;
				else x-=25;
			} 
			   
			if(x > -800 && world == 2 && level == 9) {
				g.drawString(GameManager.res.getString(R.string.activeinterrupter), 100, 410, paint2, 50);
				g.drawString(GameManager.res.getString(R.string.withboxes), 190, 470, paint2, 50);
			}  
			
			if(x > -800 && world == 4 && level == 1) {
				g.drawString(GameManager.res.getString(R.string.useteleporter), x, 150, paint2, 50);
				if(x < 500 && x > 50) x-=2; 
				else x-=25;
			} 
			
			if(x > -800 && world == 5 && level == 1) {
			} 
		}
		else if (state == GameState.Paused) {
		    drawPausedUI();
		} else if(state == GameState.Finish) {
			drawFinishUI();
		} else if(state == GameState.GameOver) {
			drawGameOverUI();
		}
		
	}

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			
			g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY());
		}
		
		//grid
		if(GameManager.gm.getSave().retrievePrefs("Grid")) {
			int firstH = map.getBeginningH();
			int endH = map.getEndH();
			int firstV = map.getBeginningV();
			int endV = map.getEndV();
			
			//Log.i("iceLog", String.valueOf(firstV));
			
			
			for(int i=firstV;i<endV;i++) //lines
			{
				if(i == 1 || i == 4 || i == 7 || i == 10)
					g.drawRect(firstH*40, i*40, endH*40-firstH*40, 3, Color.GRAY);
				else
					g.drawRect(firstH*40, i*40, endH*40-firstH*40, 2, Color.GRAY);
			}
			
				
			for(int i=firstH+1;i<endH;i++) // column
				g.drawLine(i*40, firstV*40, i*40, endV*40, Color.GRAY);
		}
		
	}
	
	private void paintTilesReflect(Graphics g)
	{
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			
			if(t.getType() == '1' || t.getType() == '5') {
				if(!getMap().isEnd(t.getTileX()/40, t.getTileY()+40) && !getMap().isEnd(t.getTileX(), t.getTileY()+40)  && !getMap().isHole(t.getTileX(), t.getTileY()+40) && !getMap().isCollision(t.getTileX(), t.getTileY()+40, BAS) && !getMap().isPortal(t.getTileX(), t.getTileY()+40) && !getMap().isSwitch(t.getTileX(), t.getTileY()+40))
					if(t.getType() == '1') g.drawImage(Assets.bloc2_reflet, t.getTileX(), t.getTileY()+32); //Reflet bloc
					else if(t.getType() == '5') g.drawImage(Assets.bloc_reflet, t.getTileX(), t.getTileY()+32); //Reflet bloc
				}
			}
	}

	public void animate() {
		animBas.update(10);
		animGauche.update(10);
		animHaut.update(10);
		animDroite.update(10);
	}

	private void nullify() {
		
		paint = null;
		perso = null;
		currentSprite = null;
		character = null;
		animBas = null;
		animHaut = null;
		animGauche = null;
		animDroite = null;
		map=null;
		tilearray=null;  

		System.gc();
	}
	
	

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.button_pause, 0, 0);
		game.getAdMobHandler().sendEmptyMessage(View.GONE);
	}
	

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0); // Darken the entire screen so you can display the Paused screen.
		
		if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
			game.getAdMobHandler().sendEmptyMessage(View.VISIBLE);
		}		
		
		 
		//On affiche l'écran de pause
		g.drawImage(Assets.pause, 200, 100);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.pause), 299, 170, paint, 50);
		else
			g.drawString(GameManager.res.getString(R.string.pause), 290, 170, paint, 50);
		
		g.drawString(world+" - "+level, 300, 220, paint, 50);
		
		if(Locale.getDefault().getLanguage().equals("fr"))
			g.drawString(GameManager.res.getString(R.string.resume), 478, 157, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.resume), 480, 157, paint, 30);
		else
			g.drawString(GameManager.res.getString(R.string.resume), 490, 157, paint, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr"))
			g.drawString(GameManager.res.getString(R.string.back), 494, 231, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.back), 489, 231, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.back), 486, 231, paint, 34);
		else
			g.drawString(GameManager.res.getString(R.string.back), 502, 232, paint, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.retry), 480, 302, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.retry), 480, 302, paint, 30);
		else
			g.drawString(GameManager.res.getString(R.string.retry), 500, 302, paint, 34);
		
		int etoile = Map.getEtoiles(world, level);
		
		if(etoile == 1)
			g.drawImage(Assets.etoile, 260, 240);
		else if(etoile == 2){
			g.drawImage(Assets.etoile, 260, 240);
			g.drawImage(Assets.etoile, 260+50, 240);
		}else if(etoile == 3){
			g.drawImage(Assets.etoile, 260, 240);
			g.drawImage(Assets.etoile, 260+50, 240);
			g.drawImage(Assets.etoile, 260+100, 240);
		}
		
	}

	private void drawFinishUI() { 
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0);
		g.drawImage(Assets.finish, 200, 100);

		if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
			game.getAdMobHandler().sendEmptyMessage(View.VISIBLE);
		}
		
		if(etoileGagne == 1) {
			g.drawImage(Assets.etoile, 220, 210);
			g.drawImage(Assets.etoilevide, 220+50, 210);
			g.drawImage(Assets.etoilevide, 220+100, 210);
		} else if(etoileGagne == 2){
			g.drawImage(Assets.etoile, 220, 210);
			g.drawImage(Assets.etoile, 220+50, 210);
			g.drawImage(Assets.etoilevide, 220+100, 210);
		}else if(etoileGagne == 3){ 
			g.drawImage(Assets.etoile, 325, 210);
			g.drawImage(Assets.etoile, 325+50, 210);
			g.drawImage(Assets.etoile, 325+100, 210);
		}
		else {//Pas d'étoile
			g.drawImage(Assets.etoilevide, 220, 210);
			g.drawImage(Assets.etoilevide, 220+50, 210);
			g.drawImage(Assets.etoilevide, 220+100, 210);
		} 
		 
		//Affichage du score
		g.drawString(String.valueOf(perso.getNbCoups())+" "+GameManager.res.getString(R.string.moves), 250, 170, paint, 50);
		
		if(etoileGagne < 3) {
			g.drawString("3 "+GameManager.res.getString(R.string.stars)+" = "+String.valueOf(map.getMinimalMoves())+" "+GameManager.res.getString(R.string.moves), 380, 245, paint, 30);
			//g.drawString(St, 470, 260, paint,30);
		}
		//g.drawString(temps_final+" sec", 250, 210, paint, 50);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.retry), 480, 161, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.retry), 479, 161, paint, 30);
		else
			g.drawString(GameManager.res.getString(R.string.retry), 500, 161, paint, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.back), 245, 310, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.back), 235, 310, paint, 34);
		else
			g.drawString(GameManager.res.getString(R.string.back), 255, 310, paint, 34);
		
		if(world == 6 && level == 12) g.drawString(GameManager.res.getString(R.string.finished), 490, 310, paint, 34);
		else{
			if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
				g.drawString(GameManager.res.getString(R.string.next), 490, 310, paint, 34);
			else if(Locale.getDefault().getLanguage().equals("de"))
				g.drawString(GameManager.res.getString(R.string.next), 486, 310, paint, 34);
			else
				g.drawString(GameManager.res.getString(R.string.next), 505, 310, paint, 34);
		}
		 
	}

	private void drawGameOverUI()
	{
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0);

		if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
			game.getAdMobHandler().sendEmptyMessage(View.VISIBLE);
		}
		
		g.drawImage(Assets.gameover, 200, 100);
		g.drawString(GameManager.res.getString(R.string.gameover), 320, 160, paint, 50); 
		if(touchedByBoss)
		{
			if(Locale.getDefault().getLanguage().equals("fr"))
				g.drawString(GameManager.res.getString(R.string.touchedbyboss), 270, 220, paint, 40);
			else if(Locale.getDefault().getLanguage().equals("es"))
				g.drawString(GameManager.res.getString(R.string.touchedbyboss), 260, 220, paint, 40);
			else if(Locale.getDefault().getLanguage().equals("de"))
				g.drawString(GameManager.res.getString(R.string.touchedbyboss), 230, 220, paint, 29);
			else
				g.drawString(GameManager.res.getString(R.string.touchedbyboss), 280, 220, paint, 40);
		}else if(touchedByEclair)
			g.drawString(GameManager.res.getString(R.string.touchedbyeclair), 270, 220, paint, 40);
		else
		{
			if(Locale.getDefault().getLanguage().equals("fr"))
				g.drawString(GameManager.res.getString(R.string.feltinhole), 220, 220, paint, 40);
			else if(Locale.getDefault().getLanguage().equals("de"))
				g.drawString(GameManager.res.getString(R.string.feltinhole), 220, 220, paint, 33);
			else
				g.drawString(GameManager.res.getString(R.string.feltinhole), 255, 220, paint, 40);
		}
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.back), 240, 305, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.back), 240, 307, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.back), 230, 305, paint, 34);
		else
			g.drawString(GameManager.res.getString(R.string.back), 250, 306, paint, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.retry), 480, 305, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.retry), 476, 307, paint, 30);
		else
			g.drawString(GameManager.res.getString(R.string.retry), 500, 305, paint, 34);
	}
	
	@Override
	public void pause() {
		if (state == GameState.Running) {
			state = GameState.Paused;
			Assets.musloop.pause();
			pauseStart = System.nanoTime();
		}
	}

	@Override
	public void resume() {
		if (state == GameState.Paused){
			state = GameState.Running;
			pauseEnd = System.nanoTime();
			Assets.playMusic();
			tempsStart += (pauseEnd-pauseStart);
		}
	}

	@Override
	public void backButton() {
		if(state == GameState.Running)
			pause();
		else if(state == GameState.Paused)
			resume();
	}
	
	
	public static Personnage getPerso() {
		return perso;
	}

	public static Map getMap() {
		return map;
	}
	
	public ArrayList<Tile> getTiles() { return tilearray; }

	@Override
	public void dispose() {
	}

}


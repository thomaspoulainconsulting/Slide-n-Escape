package com.kilobolt.icescape.screens;

import java.util.List;
import java.util.Locale;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Graphics.ImageFormat;
import com.kilobolt.framework.Image;
import com.kilobolt.framework.Input.TouchEvent;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.implementation.AndroidGraphics;
import com.kilobolt.icescape.Assets;
import com.kilobolt.icescape.ClicService;
import com.kilobolt.icescape.GameManager;
import com.kilobolt.icescape.R;
import com.kilobolt.icescape.map.Map;
import com.kilobolt.icescape.screens.LevelScreen.states;

public class WorldScreen extends Screen {
	 
	private Paint paint, paint2;
	
	private Image locker;
	private int world_touched;
	private states state = states.NORMAL;
	private Image menu;
	
	public WorldScreen(Game game) {
		super(game);
		
		System.gc();

		if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
			game.getAdMobHandler().sendEmptyMessage(View.VISIBLE);
		}
		
		Graphics g = game.getGraphics();
		locker = g.newImage("sprites/lockerwhite.png", ImageFormat.RGB565);
		menu = g.newImage("menus/worlds.png", ImageFormat.RGB565);
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Assets.font);
		paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true); 
		 
		paint2 = new Paint();
		paint2.setTextSize(30); 
		paint2.setColor(Color.WHITE);
		paint2.setTypeface(Assets.font); 
		paint2.setStyle(Paint.Style.FILL); 
        paint2.setAntiAlias(true); 
        
        if(GameManager.gm.getSave().isOkToShowInterstitial() && GameManager.isConnected() && !GameManager.gm.getSave().retrievePrefs("no_ads")) {
        	
			GameManager.activity.runOnUiThread(new Runnable() {
				  public void run() {
					  GameManager.gm.getAdMobHandler().showInterstitial();
				  }
			});
    	
    	GameManager.gm.getSave().showingInterstitial();
	    }
	    GameManager.gm.getSave().showingLevels();
    
	    world_touched=0;
	}

	@Override 
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if(touchEvents.size() > 0) {
			for (int i = 0; i < touchEvents.size(); i++) {
				TouchEvent event = touchEvents.get(i);
				if (event.type == TouchEvent.TOUCH_UP) {
					
//					Log.i("iceLog", String.valueOf(event.x)+"-"+String.valueOf(event.y));
					
					if(state == states.NORMAL) {
	
						if (AndroidGraphics.inBounds(event, 145, 72, 110, 115)) { //World 1
							ClicService.clic();
							game.setScreen(new LevelScreen(game, 1)); 
						}
						
						if (AndroidGraphics.inBounds(event, 373, 72, 110, 115)) { //World 2
							ClicService.clic();
	
							if(Map.isCompleted(1, 12) ||  GameManager.gm.getSave().retrievePrefs("world_2")) {
								game.setScreen(new LevelScreen(game, 2));
							} else {
								
								if(GameManager.gm.getSave().retrieve("unlock_world_credit") > 0) {
									//Interface de demande de débloquage du monde 
									state=states.ASKING_TO_UNLOCK;
									world_touched=2;
								} else {
									//Interface de demande d'achat pour débloquer le monde
									state=states.ASKING_TO_BUY;
								}
								
							}
						}
						
						if (AndroidGraphics.inBounds(event, 625, 72, 110, 115)) { //World 3
							
							ClicService.clic();
	
							if(Map.isCompleted(2, 12) || GameManager.gm.getSave().retrievePrefs("world_3")) {
								game.setScreen(new LevelScreen(game, 3));
							} else {
								if(GameManager.gm.getSave().retrieve("unlock_world_credit") > 0) {
									//Interface de demande de débloquage du monde 
									state=states.ASKING_TO_UNLOCK;
									world_touched=3;
								} else {
									//Interface de demande d'achat pour débloquer le monde
									state=states.ASKING_TO_BUY;
								}
							}
						}
						
						if (AndroidGraphics.inBounds(event, 250, 220, 110, 115)) { //World 4
							ClicService.clic();
							
							if(Map.isCompleted(3, 12) || GameManager.gm.getSave().retrievePrefs("world_4")) {
								game.setScreen(new LevelScreen(game, 4)); 
							} else {
								if(GameManager.gm.getSave().retrieve("unlock_world_credit") > 0) {
									//Interface de demande de débloquage du monde 
									state=states.ASKING_TO_UNLOCK;
									world_touched=4;
								} else {
									//Interface de demande d'achat pour débloquer le monde
									state=states.ASKING_TO_BUY;
								}
							}
						}
						
						if (AndroidGraphics.inBounds(event, 505, 220, 110, 115)) { //World 5
							ClicService.clic();
							
							if(Map.isCompleted(4, 12) || GameManager.gm.getSave().retrievePrefs("world_5")) {
								game.setScreen(new LevelScreen(game, 5));
							}else {
								if(GameManager.gm.getSave().retrieve("unlock_world_credit") > 0) {
									//Interface de demande de débloquage du monde 
									state=states.ASKING_TO_UNLOCK;
									world_touched=5;
								} else {
									//Interface de demande d'achat pour débloquer le monde
									state=states.ASKING_TO_BUY;
								}
							}
						}
						
						if (AndroidGraphics.inBounds(event, 373, 340, 110, 115)) { //World 6
							ClicService.clic();
							
							if(Map.isCompleted(5, 12) || GameManager.gm.getSave().retrievePrefs("world_6")) {
								game.setScreen(new LevelScreen(game, 6)); 
							}else {
								if(GameManager.gm.getSave().retrieve("unlock_world_credit") > 0) {
									//Interface de demande de débloquage du monde 
									state=states.ASKING_TO_UNLOCK;
									world_touched=6;
								} else {
									//Interface de demande d'achat pour débloquer le monde
									state=states.ASKING_TO_BUY;
								}
							}
						} 
						
						if (AndroidGraphics.inBounds(event, 635, 387, 165, 60)) {  //retour World Choice
							ClicService.clic();
							game.setScreen(new MainMenuScreen(game)); 
						}
					}
					else if(state == states.ASKING_TO_BUY) {
						//OUI
						if(AndroidGraphics.inBounds(event, 220, 275, 126, 55)) {
							ClicService.clic();
							game.setScreen(new StoreScreen(game));
						}
						//NON
						else if (AndroidGraphics.inBounds(event, 480, 275, 126, 50)) {
							ClicService.clic();
							state=states.NORMAL;
						}
					}
					else if(state == states.ASKING_TO_UNLOCK) {
						//OUI
						if(AndroidGraphics.inBounds(event, 220, 275, 126, 55) && world_touched > 0) {
							//Unlocking the level touched
							GameManager.gm.getSave().savePrefs("world_"+world_touched);
							GameManager.gm.getSave().save("unlock_world_credit", GameManager.gm.getSave().retrieve("unlock_world_credit")-1);
							state=states.NORMAL;
							ClicService.clic();
						}
						//NON
						else if (AndroidGraphics.inBounds(event, 480, 275, 126, 50)) {
							state=states.NORMAL;
							ClicService.clic();
						}
					}
				}
			}
		}
		
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		
		//On récupère le nombre d'étoile gagné dans chaque monde
		g.drawImage(menu, 0, 0);
		
		g.drawString(GameManager.res.getString(R.string.world), 35, 45, paint, 50);
		
		int nbEtoilePerWorld=0;
		
		for(int monde=1;monde<=6;monde++){
			for(int i=1;i<=GameManager.NBLEVEL;i++)
				nbEtoilePerWorld+=GameManager.gm.getSave().retrieve(monde+"-"+i+"-etoile");
			
			
			if(monde == 1) {g.drawString(String.valueOf(monde), 185, 145, paint, 60);	g.drawString(Math.round(((double)nbEtoilePerWorld/36)*100)+"%", 178, 180, paint, 32);	}
			if(monde == 2) {g.drawString(String.valueOf(monde), 410, 145, paint, 60);	g.drawString(Math.round(((double)nbEtoilePerWorld/36)*100)+"%", 410, 180, paint, 32);	}
			if(monde == 3) {g.drawString(String.valueOf(monde), 635, 145, paint, 60);	g.drawString(Math.round(((double)nbEtoilePerWorld/36)*100)+"%", 635, 180, paint, 32);	}
			if(monde == 4) {g.drawString(String.valueOf(monde), 290, 260, paint, 60);	g.drawString(Math.round(((double)nbEtoilePerWorld/36)*100)+"%", 290, 295, paint, 32);	}
			if(monde == 5) {g.drawString(String.valueOf(monde), 525, 260, paint, 60);	g.drawString(Math.round(((double)nbEtoilePerWorld/36)*100)+"%", 525, 295, paint, 32);	}
			if(monde == 6) {g.drawString(String.valueOf(monde), 410, 375, paint, 60);	g.drawString(Math.round(((double)nbEtoilePerWorld/36)*100)+"%", 410, 415, paint, 32);	}
			nbEtoilePerWorld=0;
		} 
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.back), 675, 425, paint2, 38);
		else if(Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.back), 670, 425, paint2, 38);
		else
			g.drawString(GameManager.res.getString(R.string.back), 685, 425, paint2, 38);
		
		//Cadenas
		if(!Map.isCompleted(1, 12) && !GameManager.gm.getSave().retrievePrefs("world_2")) g.drawImage(locker, 450, 119);
		if(!Map.isCompleted(2, 12) && !GameManager.gm.getSave().retrievePrefs("world_3")) g.drawImage(locker, 670, 119);
		if(!Map.isCompleted(3, 12) && !GameManager.gm.getSave().retrievePrefs("world_4")) g.drawImage(locker, 330, 235);
		if(!Map.isCompleted(4, 12) && !GameManager.gm.getSave().retrievePrefs("world_5")) g.drawImage(locker, 558, 235);
		if(!Map.isCompleted(5, 12) && !GameManager.gm.getSave().retrievePrefs("world_6")) g.drawImage(locker, 450, 350);
		
		if(state == states.ASKING_TO_UNLOCK) {
			drawUnlockingUI();
		} else if(state == states.ASKING_TO_BUY) {
			drawBuyingUI();
		}
	}
	
	public void drawUnlockingUI() {
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0);
		
		g.drawImage(Assets.gameover, 200, 100);
		g.drawString(GameManager.res.getString(R.string.unlockthisworld), 260, 160, paint2, 40);
		
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.yes), 260, 305, paint2, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.yes), 260, 307, paint2, 34);
		else if(Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.yes), 250, 305, paint2, 34);
		else
			g.drawString(GameManager.res.getString(R.string.yes), 270, 306, paint2, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.no), 500, 305, paint2, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.no), 496, 307, paint2, 30);
		else
			g.drawString(GameManager.res.getString(R.string.no), 520, 305, paint2, 34);
	}
	
	public void drawBuyingUI() {
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0);
		
		g.drawImage(Assets.gameover, 200, 100);
		g.drawString(GameManager.res.getString(R.string.notenoughcredit), 270, 160, paint2, 40);
		g.drawString(GameManager.res.getString(R.string.buyingalevelcredit), 253, 210, paint2, 34);
		g.drawString(GameManager.res.getString(R.string.buyingaworldcredit2), 285, 260, paint2, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.yes), 260, 305, paint2, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.yes), 260, 307, paint2, 34);
		else if(Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.yes), 250, 305, paint2, 34);
		else
			g.drawString(GameManager.res.getString(R.string.yes), 270, 306, paint2, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.no), 515, 305, paint2, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.no), 511, 307, paint2, 30);
		else
			g.drawString(GameManager.res.getString(R.string.no), 535, 305, paint2, 34);
	}
	

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void backButton() {
		System.gc();
		ClicService.clic();
		game.setScreen(new MainMenuScreen(game));
		
	}

	
}

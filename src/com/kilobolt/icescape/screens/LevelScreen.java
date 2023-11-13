package com.kilobolt.icescape.screens;

import java.util.List;
import java.util.Locale;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
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


public class LevelScreen extends Screen {
	public enum states {NORMAL, ASKING_TO_UNLOCK, ASKING_TO_BUY, NOTIFICATION_REMOVAL_ADS, NOTIFICATION_UNLOCKING_LEVEL, NOTIFICATION_UNLOCKING_WORLD, RATE};

	private int world;
	private Image level_img, locker;
	private Paint paint;
	private int x = 50, x2=750;
	private states state = states.NORMAL;
	private int level_touched=0;
	private Image world1, world2, world3, world4, world5, world6, rate_menu;
	
	
	public LevelScreen(Game game, int world) {
		super(game);
		System.gc();

		Assets.stopMusic();
		game.getAdMobHandler().sendEmptyMessage(View.GONE);
		this.world=world;
		
		loadingImages();
		
        //Affichage de la pub interstitiale 
        if(GameManager.gm.getSave().isOkToShowInterstitial() && GameManager.isConnected() && !GameManager.gm.getSave().retrievePrefs("no_ads")) {
        	
    			GameManager.activity.runOnUiThread(new Runnable() {
    				  public void run() {
    					  GameManager.gm.getAdMobHandler().showInterstitial();
    				  }
    			});
        	
        	GameManager.gm.getSave().showingInterstitial();
        }
        GameManager.gm.getSave().showingLevels();
        
        
        //Gestion de la notation de l'application
        //Si on est connecté
        if(GameManager.isConnected()) {
        	//Si on a le droit de demander à l'utilisateur
        	if(GameManager.gm.getSave().askingRate()) {
        		
        		//Si l'utilisateur à déjà fini un monde
        		for(int i=1;i<6;i++) {
                	if(Map.isCompleted(i, 12)) {
                    	state=states.RATE;
                    	break;
                    }
                }
        	}
        }
        
	}
	
	
	public void loadingImages() {
		
		Graphics g = game.getGraphics();
		
		locker = g.newImage("sprites/locker.png", ImageFormat.RGB565);
		level_img = g.newImage("menus/level_img.png", ImageFormat.RGB565);
		paint = new Paint();
		paint.setColor(Color.WHITE); 
		paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        
        switch(world) {
	        case 1 : world1 = g.newImage("menus/world1.png", ImageFormat.RGB565); break;
	        case 2 : world2 = g.newImage("menus/world2.png", ImageFormat.RGB565); break;
	        case 3 : world3 = g.newImage("menus/world3.png", ImageFormat.RGB565); break;
	        case 4 : world4 = g.newImage("menus/world4.png", ImageFormat.RGB565); break;
	        case 5 : world5 = g.newImage("menus/world5.png", ImageFormat.RGB565); break;
	        case 6 : world6 = g.newImage("menus/world6.png", ImageFormat.RGB565); break;
        }

        rate_menu = g.newImage("menus/rate.png", ImageFormat.RGB565);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		if(len > 0) {
			for (int i = 0; i < touchEvents.size(); i++) {
				if(touchEvents.size() > 0) 
				{
					TouchEvent event = touchEvents.get(i);
				
					if (event.type == TouchEvent.TOUCH_UP) { 
						
//						Log.i("iceLog", String.valueOf(event.x)+"-"+String.valueOf(event.y));
						
						if(state == states.NORMAL) {
						
							int index=1, y=120, x=1;
							for(int j=1;j<=GameManager.NBLEVEL;j++)
							{
								if(index == 5) { y+=122;x=1;index=2; }
								else x=index++;
								 
								if (AndroidGraphics.inBounds(event, 145*x, y-30, 100, 100)) { 
			    
									if(j > 1) {
										
										ClicService.clic();
										
										if(Map.isCompleted(world, j-1) || GameManager.gm.getSave().retrievePrefs("level_"+world+"_"+j)) {
											game.setScreen(new GameScreen(game, world, j));
										} else {
											 
											Log.e("iceLog", GameManager.gm.getSave().retrieve("unlock_level_credit")+"");
											
											if(GameManager.gm.getSave().retrieve("unlock_level_credit") > 0) {
												//Interface de demande de débloquage du level 
												state=states.ASKING_TO_UNLOCK;
												level_touched=j;
											} else {
												//Interface de demande d'achat pour débloquer le level
												state=states.ASKING_TO_BUY;
											}
											
										}
										 
									}else if (j == 1) {
										game.setScreen(new GameScreen(game, world, j)); 
										ClicService.clic();
									}
								}
							} 
			 
							if (AndroidGraphics.inBounds(event, 635, 425, 165, 55)) {  //retour World Choice
								game.setScreen(new WorldScreen(game)); 
								ClicService.clic();
							}
						
						} else if(state == states.ASKING_TO_BUY) {
							
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
							
						} else if(state == states.ASKING_TO_UNLOCK) {
							
							//OUI
							if(AndroidGraphics.inBounds(event, 220, 275, 126, 55)) {
								//Unlocking the level touched
								GameManager.gm.getSave().savePrefs("level_"+world+"_"+level_touched);
								GameManager.gm.getSave().save("unlock_level_credit", GameManager.gm.getSave().retrieve("unlock_level_credit")-1);
								state=states.NORMAL;
								ClicService.clic();
							}
							//NON
							else if (AndroidGraphics.inBounds(event, 480, 275, 126, 50)) {
								state=states.NORMAL;
								ClicService.clic();
							}
							
						} else if(state == states.RATE) {
							
							//NON
							if(AndroidGraphics.inBounds(event, 220, 285, 126, 50)) {
								ClicService.clic();
								GameManager.gm.getSave().save("behaviour", 0);
								state=states.NORMAL;
							}
							//PLUS TARD
							else if(AndroidGraphics.inBounds(event, 360, 285, 126, 50)) {
								ClicService.clic();
								GameManager.gm.getSave().save("behaviour", 2);
								GameManager.gm.getSave().save("behaviourWait", 5);
								state=states.NORMAL;
							}
							//OUI
							else if(AndroidGraphics.inBounds(event, 490, 285, 126, 50)) {
								ClicService.clic();
								GameManager.gm.getSave().save("behaviour", 1);
								state=states.NORMAL;
								GameManager.gm.getSave().savePrefs("rate");
								GameManager.gm.startGooglePlayIntent();
							}
						}
		
					}
				}
			}
		}
		
	}

	@Override
	public void paint(float deltaTime) {
		//afficher Retour
		Graphics g = game.getGraphics();
		
		switch(world) {
			case 1: g.drawImage(world1, 0, 0);  break;
			case 2: g.drawImage(world2, 0, 0); break;
			case 3: g.drawImage(world3, 0, 0); break;
			case 4: g.drawImage(world4, 0, 0); break;
			case 5: g.drawImage(world5, 0, 0); break;
			case 6: g.drawImage(world6, 0, 0); break;
		}
		
		g.drawImage(Assets.nuage, x, 60);
		g.drawImage(Assets.nuage2, x2, 350);
		x+=Math.round(0.4*deltaTime);
		x2-=Math.round(0.5*deltaTime);
		if(x > 800) x=-200;
		if(x2 < -150) x2=800;
		
		Typeface tf = Typeface.createFromAsset(GameManager.context.getAssets(), "fonts/Harabara.ttf");
		paint.setTypeface(tf);
		
		
		int etoile, y=150, x=1, index=1;
		
		//on boucle sur les levels et on affiche les etoiles pour chaque levels
		for(int i=1;i<=GameManager.NBLEVEL;i++)
		{
			etoile = Map.getEtoiles(world, i);
			
			
			if(index == 5) { y+=100;x=1;index=2; }
			else x=index++;
			
			
			
			g.drawImage(level_img, 145*x, y-50);
			g.drawString(String.valueOf(i), 145*x+40, y, paint, 27);
			
			if(i > 1 && !Map.isCompleted(world, i) && !Map.isCompleted(world, i-1) && !GameManager.gm.getSave().retrievePrefs("level_"+world+"_"+i)) {
				g.drawImage(locker, 145*x+70, y-35);
			}
			
			if(etoile == 1)
				g.drawImage(Assets.etoile_small, 145*x+20, y+24);
			else if(etoile == 2){ 
				g.drawImage(Assets.etoile_small, 145*x+20, y+24);
				g.drawImage(Assets.etoile_small, 145*x+40, y+24);
			}else if(etoile == 3){
				g.drawImage(Assets.etoile_small, 145*x+20, y+24);
				g.drawImage(Assets.etoile_small, 145*x+40, y+24);
				g.drawImage(Assets.etoile_small, 145*x+60, y+24);
			}
			
		}
		
		paint.setFakeBoldText(false);
		paint.setTypeface(Assets.font);
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.back), 675, 458, paint, 38);
		else if(Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.back), 670, 458, paint, 38);
		else
			g.drawString(GameManager.res.getString(R.string.back), 685, 458, paint, 38);
		paint.setColor(Color.BLACK);
		g.drawString(GameManager.res.getString(R.string.levels), 35, 45, paint, 50);
		g.drawString("#"+String.valueOf(world), 700, 45, paint, 50);
		paint.setColor(Color.WHITE);
		
		
		if(state == states.ASKING_TO_UNLOCK) {
			drawUnlockingUI();
		} else if(state == states.ASKING_TO_BUY) {
			drawBuyingUI();
		} else if(state == states.RATE) {
			drawGameRateUI();
		}
	}
	
	public void drawUnlockingUI() {
		
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0);
		
		g.drawImage(Assets.gameover, 200, 100);
		g.drawString(GameManager.res.getString(R.string.unlockthislevel), 260, 160, paint, 40);
		
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.yes), 260, 305, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.yes), 260, 307, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.yes), 250, 305, paint, 34);
		else
			g.drawString(GameManager.res.getString(R.string.yes), 270, 306, paint, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.no), 500, 305, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.no), 496, 307, paint, 30);
		else
			g.drawString(GameManager.res.getString(R.string.no), 520, 305, paint, 34);
	}
	 
	public void drawBuyingUI() {
		
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0);
		
		g.drawImage(Assets.gameover, 200, 100);
		g.drawString(GameManager.res.getString(R.string.notenoughcredit), 260, 160, paint, 40);
		g.drawString(GameManager.res.getString(R.string.buyingalevelcredit), 250, 210, paint, 38);
		g.drawString(GameManager.res.getString(R.string.buyingalevelcredit2), 250, 260, paint, 38);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.yes), 260, 305, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.yes), 260, 307, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.yes), 250, 305, paint, 34);
		else
			g.drawString(GameManager.res.getString(R.string.yes), 270, 306, paint, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.no), 515, 305, paint, 34);
		else if(Locale.getDefault().getLanguage().equals("de"))
			g.drawString(GameManager.res.getString(R.string.no), 511, 307, paint, 30);
		else
			g.drawString(GameManager.res.getString(R.string.no), 535, 305, paint, 34);
	}
	
	private void drawGameRateUI()
	{
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0);
		 
		//On affiche l'écran de pause
		g.drawImage(rate_menu, 200, 100);
		
		g.drawString(GameManager.res.getString(R.string.ratetheapp), 270, 170, paint, 38);
		g.drawString(GameManager.res.getString(R.string.ratetheapp2), 300, 215, paint, 38);
		 
		
		g.drawString(GameManager.res.getString(R.string.no),257, 306, paint, 34);
		
		if(Locale.getDefault().getLanguage().equals("fr")) {
			g.drawString(GameManager.res.getString(R.string.later),360, 306, paint, 32);
		} else {
			g.drawString(GameManager.res.getString(R.string.later),360, 306, paint, 34);
		}
		g.drawString(GameManager.res.getString(R.string.yes),515, 306, paint, 34);
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
		game.setScreen(new WorldScreen(game));
	}

}

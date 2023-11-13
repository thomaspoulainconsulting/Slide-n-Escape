package com.kilobolt.icescape.screens;

import java.util.List;
import java.util.Locale;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;

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
import com.kilobolt.icescape.screens.LevelScreen.states;

public class StoreScreen extends Screen{
	
	private Paint paint2;
	private Image menu, congrats;
	public states state = states.NORMAL;
	private Image level_unlock, world_unlock, ads_unlock;

	public StoreScreen(Game game) {
		super(game);

		if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
			game.getAdMobHandler().sendEmptyMessage(View.VISIBLE);
		}
        
        loadingImages();
	}
	
	public void loadingImages() {
		
		paint2 = new Paint();
		paint2.setTextSize(30); 
		paint2.setColor(Color.BLACK);
		paint2.setTypeface(Assets.font); 
		paint2.setStyle(Paint.Style.FILL); 
        paint2.setAntiAlias(true);
        
		Graphics g = game.getGraphics();
        menu = g.newImage("menus/options.png", ImageFormat.RGB565);
        congrats = g.newImage("menus/congrats.png", ImageFormat.RGB565);
        level_unlock = g.newImage("menus/level_img_shop_final.png", ImageFormat.RGB565);
		world_unlock = g.newImage("menus/world_unlock_final.png", ImageFormat.RGB565);
		ads_unlock = g.newImage("menus/ads_unlock.png", ImageFormat.RGB565);
	}

	@Override	
	public void update(float deltaTime) {
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				
				
				if(state == states.NORMAL) {
					//Achat d'un "unlock level"
					if(AndroidGraphics.inBounds(event, 275, 100, 120, 120)) {
						GameManager.gm.getIAP().launchPurchase(Game.SKU_LEVEL_UNLOCK, this);
						ClicService.clic();
					}
					 
					//Achat d'un "unlock world"
					if(AndroidGraphics.inBounds(event, 275, 260, 120, 120)) {
						GameManager.gm.getIAP().launchPurchase(Game.SKU_WORLD_UNLOCK, this);
						ClicService.clic();
					}
					       
					//Achat de l'item "no_ads"
					if(AndroidGraphics.inBounds(event, 650, 100, 120, 120)) {
						ClicService.clic();
						
						//FIXME Test
						if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
							GameManager.gm.getIAP().launchPurchase(Game.SKU_NO_ADS, this);
						} else {
							
							GameManager.activity.runOnUiThread(new Runnable() {
							    public void run() {
							    	//This item has already been bought
									Toast.makeText(GameManager.context, GameManager.res.getString(R.string.youalreadyboughit), Toast.LENGTH_SHORT).show();
							    }
							});
							
						}
					}
					  
					if (AndroidGraphics.inBounds(event, 635, 387, 165, 60)) {  //retour MainMenu
						ClicService.clic();
						game.setScreen(new MainMenuScreen(game)); 
					}
				} else {
					
					//OK
					if(AndroidGraphics.inBounds(event, 350, 275, 126, 50)) {
						ClicService.clic();
						state=states.NORMAL;
					}
				}
				
				
			}
		}
	}   

	@Override
	public void paint(float deltaTime) {
		
		Graphics g = game.getGraphics();
		g.drawImage(menu, 0, 0);
		
		g.drawString(GameManager.res.getString(R.string.store), 35, 45, paint2, 50);
		
		//Bouton level_unlock
		g.drawImage(level_unlock, 250, 100);
		
		//Affichage du texte en fonction de la langue
		if(Locale.getDefault().getLanguage().equals("de")) {
			g.drawString(GameManager.res.getString(R.string.level_unlock), 25, 160, paint2);
		}else {
			g.drawString(GameManager.res.getString(R.string.level_unlock), 50, 160, paint2);
		}
		
		//bouton world_unlock
		g.drawImage(world_unlock, 250, 250);
		//Affichage du texte en fonction de la langue
		if(Locale.getDefault().getLanguage().equals("de")) {
			g.drawString(GameManager.res.getString(R.string.world_unlock), 25, 305, paint2);
		}else {
			g.drawString(GameManager.res.getString(R.string.world_unlock), 50, 305, paint2);
		}
		
		
		//bouton ads_unlock
		g.drawImage(ads_unlock, 650, 100);
		//Affichage du texte en fonction de la langue
		if(Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it")) {
			g.drawString(GameManager.res.getString(R.string.ads_unlock), 450, 160, paint2);
		}else {
			g.drawString(GameManager.res.getString(R.string.ads_unlock), 470, 160, paint2);
		}
		
		paint2.setColor(Color.WHITE);
		
		//Bouton retour
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es"))
			g.drawString(GameManager.res.getString(R.string.back), 675, 425, paint2, 38);
		else if(Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.back), 670, 425, paint2, 38);
		else
			g.drawString(GameManager.res.getString(R.string.back), 685, 425, paint2, 38);
		
		paint2.setColor(Color.BLACK);
		
		
		if(state != states.NORMAL) {
			drawNotification();
		}
	}
	
	
	private void drawNotification() {
		Graphics g = game.getGraphics();
		g.drawARGB(125, 0, 0, 0);
		
		paint2.setColor(Color.WHITE);
		
		g.drawImage(congrats, 200, 100);
		g.drawString(GameManager.res.getString(R.string.thanks), 360, 160, paint2, 40);
		
		if(state == states.NOTIFICATION_UNLOCKING_WORLD) {
			
			//Affichage du "You have unlock"
			if(Locale.getDefault().getLanguage().equals("fr")) {
				g.drawString(GameManager.res.getString(R.string.congratsunlocking), 220, 200, paint2, 40);
			} else if(Locale.getDefault().getLanguage().equals("es")) {
				g.drawString(GameManager.res.getString(R.string.congratsunlocking), 260, 200, paint2, 40);
			} else {
				g.drawString(GameManager.res.getString(R.string.congratsunlocking), 310, 200, paint2, 40);
			}
			
			int nbWorldCredit = GameManager.gm.getSave().retrieve("unlock_world_credit");
			
			g.drawString(""+nbWorldCredit+" ", 355, 245, paint2, 40);
			if(nbWorldCredit > 1) {
				g.drawString(GameManager.res.getString(R.string.world).toLowerCase(), 385, 245, paint2, 40);
			} else {
				g.drawString(GameManager.res.getString(R.string.world_singulier), 385, 245, paint2, 40);
			}
		} else if(state == states.NOTIFICATION_UNLOCKING_LEVEL) {
			
			if(Locale.getDefault().getLanguage().equals("fr")) {
				g.drawString(GameManager.res.getString(R.string.congratsunlocking), 220, 200, paint2, 40);
			} else if(Locale.getDefault().getLanguage().equals("es")) {
				g.drawString(GameManager.res.getString(R.string.congratsunlocking), 260, 200, paint2, 40);
			} else {
				g.drawString(GameManager.res.getString(R.string.congratsunlocking), 310, 200, paint2, 40);
			}
			
			int nbLevelCredit = GameManager.gm.getSave().retrieve("unlock_level_credit");
			
			g.drawString(""+nbLevelCredit+" ", 355, 245, paint2, 40);
			if(nbLevelCredit > 1) {
				g.drawString(GameManager.res.getString(R.string.levels).toLowerCase(), 385, 245, paint2, 40);
			} else {
				g.drawString(GameManager.res.getString(R.string.level), 385, 245, paint2, 40);
			}
			
		} else if(state == states.NOTIFICATION_REMOVAL_ADS) {
			if(Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it")) {
				g.drawString(GameManager.res.getString(R.string.congratsremovingads), 250, 200, paint2, 40);
			}else {
				g.drawString(GameManager.res.getString(R.string.congratsremovingads), 290, 200, paint2, 40); //There's no more ads in the game
			}
		}
		
		g.drawString(GameManager.res.getString(R.string.ok), 390, 310, paint2, 34);
		
		paint2.setColor(Color.BLACK);
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

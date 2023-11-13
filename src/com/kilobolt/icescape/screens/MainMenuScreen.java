package com.kilobolt.icescape.screens;

import java.util.List;
import java.util.Locale;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.google.android.gms.ads.AdListener;
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

public class MainMenuScreen extends Screen {
	  
	private Paint paint;
	private Image facebook, listAchievements, menu;
	
	public MainMenuScreen(Game game) {
		super(game);
		
		System.gc();
		
		game.getAdMobHandler().sendEmptyMessage(View.GONE);
		
		   
		paint = new Paint();
		paint.setColor(Color.argb(255, 231, 231, 226));
		paint.setTypeface(Assets.font);
		paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAntiAlias(true);
        
        Graphics g = game.getGraphics();
        facebook= g.newImage("sprites/fb.png", ImageFormat.RGB565);
        listAchievements = g.newImage("sprites/play-games.png", ImageFormat.RGB565);
        menu = g.newImage("menus/new_SnE_menu.png", ImageFormat.RGB565);
        
	} 

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		for (int i = 0; i < touchEvents.size(); i++) {
			if(touchEvents.size() > 0) {
				TouchEvent event = touchEvents.get(i);
				if (event.type == TouchEvent.TOUCH_UP) {
					
//					Log.i("iceLog", String.valueOf(event.x)+"-"+String.valueOf(event.y));
					 
					if (AndroidGraphics.inBounds(event, 0, 416, 200, 64))  // si on appuie sur la zone de "Jouer"
					{
						ClicService.clic();
						
						//Lancement du scénario ou de la sélection du monde
						if(GameManager.gm.getSave().retrievePrefs("AlreadyPlayed") == false){
							GameManager.gm.getSave().savePrefs("AlreadyPlayed");
							game.setScreen(new ScenarioScreen(game, true));
						}else {
							game.setScreen(new WorldScreen(game));
						}
					}
					else if(AndroidGraphics.inBounds(event, 200, 416, 200, 64)) {
						ClicService.clic();
						game.setScreen(new StoreScreen(game));
					}
					else if (AndroidGraphics.inBounds(event, 400, 416, 200,64)) {
						ClicService.clic(); 
						game.setScreen(new OptionsScreen(game));
					}
					else if(AndroidGraphics.inBounds(event, 600, 416, 200, 64)) {
						ClicService.clic();
						game.setScreen(new CreditScreen(game)); 
						
					} else if(AndroidGraphics.inBounds(event, 0, 0, 100, 60)) {
						ClicService.clic();
						GameManager.gm.showAchievements();
					} else if(AndroidGraphics.inBounds(event, 0, 60, 100, 100)) {
						ClicService.clic();
						game.startFacebookIntent();
					}
				}
			}
		}
	}


	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(menu, 0, 0);
		
		//Menu Jouer / Boutique / Options / Credit
		
		//Bouton jouer
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es")) g.drawString(GameManager.res.getString(R.string.play), 60, 465, paint, 50);
		else if(Locale.getDefault().getLanguage().equals("de")) g.drawString(GameManager.res.getString(R.string.play), 50, 465, paint, 50);
		else if(Locale.getDefault().getLanguage().equals("it")) g.drawString(GameManager.res.getString(R.string.play), 50, 465, paint, 50);
		else g.drawString(GameManager.res.getString(R.string.play), 70, 465, paint, 50);
		
		//Bouton boutique
		if(Locale.getDefault().getLanguage().equals("fr")) g.drawString(GameManager.res.getString(R.string.store), 245, 465, paint, 50);
		else g.drawString(GameManager.res.getString(R.string.store), 260, 465, paint, 50);
				
		//Boutique option
		if(Locale.getDefault().getLanguage().equals("de")) g.drawString(GameManager.res.getString(R.string.option), 440, 465, paint, 50);
		else if(Locale.getDefault().getLanguage().equals("es")) g.drawString(GameManager.res.getString(R.string.option), 445, 465, paint, 50);
		else g.drawString(GameManager.res.getString(R.string.option), 450, 465, paint, 50);
		
		//Bouton credit
		if(Locale.getDefault().getLanguage().equals("es")) g.drawString(GameManager.res.getString(R.string.credit2), 645, 465, paint, 50);
		else g.drawString(GameManager.res.getString(R.string.credit2), 650, 465, paint, 50);
		
		//Bouton achievements
		g.drawImage(listAchievements, 5, 5);
		
		//Bouton facebook
		g.drawImage(facebook, 5, 70);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		game.getAdMobHandler().sendEmptyMessage(View.GONE);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void backButton() {

		if(GameManager.isConnected()) {
			GameManager.activity.runOnUiThread(new Runnable() {
				  public void run() {
					  GameManager.gm.getAdMobHandler().showInterstitial();
					  GameManager.gm.getAdMobHandler().getInterstitial().setAdListener(new AdListener() {
						  @Override
						public void onAdClosed() {
							  GameManager.activity.finish();
							  super.onAdClosed();
						}
						  
						  @Override
						public void onAdFailedToLoad(int errorCode) {
							  GameManager.activity.finish();
							  super.onAdFailedToLoad(errorCode);
						}
						  
					});
				  }
			});
		} else {
			GameManager.activity.finish();
		}
	}
	
	
}

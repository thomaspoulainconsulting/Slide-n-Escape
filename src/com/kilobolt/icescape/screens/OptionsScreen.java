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

public class OptionsScreen extends Screen {
	
	private Paint paint;
	private boolean dialogBoxOpen=false;
	private Image menu;

	public OptionsScreen(Game game) {
		super(game);

		if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
			game.getAdMobHandler().sendEmptyMessage(View.VISIBLE);
		}
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Assets.font);
		paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        
        Graphics g = game.getGraphics();
        menu = g.newImage("menus/options.png", ImageFormat.RGB565);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				
//				Log.i("iceLog", String.valueOf(event.x)+"-"+String.valueOf(event.y));
				
				if(dialogBoxOpen)
				{	
					if(AndroidGraphics.inBounds(event, 220, 275, 126, 55)) {
						dialogBoxOpen=false;
						ClicService.clic();
					}else if (AndroidGraphics.inBounds(event, 480, 275, 126, 50)) {
						GameManager.gm.getSave().suppressAll();
						GameManager.gm.getSave().savePrefs("Son");
						GameManager.gm.getSave().savePrefs("Vibration");
						dialogBoxOpen=false;
						ClicService.clic();
					}
						
				} else {
										
					if (AndroidGraphics.inBounds(event, 550, 90, 50, 50))  //On change l'état de la checkbox
					{
						ClicService.clic();
						GameManager.gm.getSave().savePrefs("Son");
					}
					else if (AndroidGraphics.inBounds(event, 550, 150, 60, 60)) {
						GameManager.gm.getSave().savePrefs("Vibration");
						ClicService.clic();
					}else if (AndroidGraphics.inBounds(event, 550, 240, 60, 60)) {
						GameManager.gm.getSave().savePrefs("Grid");
						ClicService.clic();
					} else if(AndroidGraphics.inBounds(event, 175, 370, 427, 110)) {
						dialogBoxOpen=true; //on ouvre la boite de dialogue
						ClicService.clic();
					}
						
					//Retour menu principal
					if (AndroidGraphics.inBounds(event, 635, 387, 165, 60)) { 
						game.setScreen(new MainMenuScreen(game)); 
						ClicService.clic();
					}
				}

			}
		}
		
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(menu, 0, 0);
		
		//On affiche les checkbox avec les états
		boolean checkSon = GameManager.gm.getSave().retrievePrefs("Son");
		boolean checkVibration = GameManager.gm.getSave().retrievePrefs("Vibration");
		boolean checkGrid = GameManager.gm.getSave().retrievePrefs("Grid");
		
		if(checkSon || Assets.MusIsPlaying())
			g.drawImage(Assets.checkbox, 550, 90, 0, 0, 42, 42);
		else
			g.drawImage(Assets.checkbox, 550, 90, 42, 0, 42, 42);
			
		if(checkVibration)
			g.drawImage(Assets.checkbox, 550, 160, 0, 0, 42, 42);
		else
			g.drawImage(Assets.checkbox, 550, 160, 42, 0, 42, 42);
		
		if(checkGrid)
			g.drawImage(Assets.checkbox, 550, 225, 0, 0, 42, 42);
		else
			g.drawImage(Assets.checkbox, 550, 225, 42, 0, 42, 42);
		
		
		g.drawString(GameManager.res.getString(R.string.option), 35, 45, paint, 50);
		g.drawString(GameManager.res.getString(R.string.sound), 175, 133, paint, 48);
		g.drawString(GameManager.res.getString(R.string.vibration), 175, 200, paint, 48);
		g.drawString(GameManager.res.getString(R.string.grid), 175, 267, paint, 48);
		g.drawString(GameManager.res.getString(R.string.suppressdata), 175, 400, paint, 48);
 
		paint.setColor(Color.WHITE);
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.back), 675, 425, paint, 38);
		else
			g.drawString(GameManager.res.getString(R.string.back), 685, 425, paint, 38);
		
		if(dialogBoxOpen) {
			g.drawARGB(125, 0, 0, 0);
			g.drawImage(Assets.gameover, 200, 100);
			
			
			if(Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it"))
				g.drawString(GameManager.res.getString(R.string.confirmsuppress), 225, 200, paint, 40);
			else
				g.drawString(GameManager.res.getString(R.string.confirmsuppress), 240, 200, paint, 40);
				
				
			if(Locale.getDefault().getLanguage().equals("fr"))
				g.drawString(GameManager.res.getString(R.string.no), 258, 307, paint, 34);
			else if(Locale.getDefault().getLanguage().equals("de"))
				g.drawString(GameManager.res.getString(R.string.no), 254, 307, paint, 34);
			else
				g.drawString(GameManager.res.getString(R.string.no), 260, 307, paint, 34);
			
			if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("it"))
				g.drawString(GameManager.res.getString(R.string.yes), 515, 306, paint, 34);
			else
				g.drawString(GameManager.res.getString(R.string.yes), 510, 306, paint, 34);
		}
		
		paint.setColor(Color.BLACK);
		
		
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

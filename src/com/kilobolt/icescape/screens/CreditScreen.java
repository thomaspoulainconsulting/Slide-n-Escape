package com.kilobolt.icescape.screens;

import java.util.List;
import java.util.Locale;
import java.util.Vector;

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

public class CreditScreen extends Screen {
	
	Image likeEN, likeFR;
	private Paint paint;
	int y;
	boolean operation;
	public Vector<CreditCharacter> tabCharacter = new Vector<CreditCharacter>();
	private Image menu;
	
	public CreditScreen(Game game) {
		super(game);

		if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
			game.getAdMobHandler().sendEmptyMessage(View.VISIBLE);
		}
		
		paint = new Paint();
		paint.setTypeface(Assets.font);
		paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
		
		tabCharacter.add(new CreditCharacter("T", -5));
		tabCharacter.add(new CreditCharacter("h", -3));
		tabCharacter.add(new CreditCharacter("o",  -1));
		tabCharacter.add(new CreditCharacter("m",  1));
		tabCharacter.add(new CreditCharacter("a",  3));
		tabCharacter.add(new CreditCharacter("s",  5));
		tabCharacter.add(new CreditCharacter(" ", -5));
		tabCharacter.add(new CreditCharacter("P",  3));
		tabCharacter.add(new CreditCharacter("o",  1));
		tabCharacter.add(new CreditCharacter("u",  -1));
		tabCharacter.add(new CreditCharacter("l",  -3));
		tabCharacter.add(new CreditCharacter("a",  -5));
		tabCharacter.add(new CreditCharacter("i",  -3));
		tabCharacter.add(new CreditCharacter("n",  -1));
		
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
				
				if (AndroidGraphics.inBounds(event, 635, 387, 165, 60)) {  //retour menu
					game.setScreen(new MainMenuScreen(game)); 
					ClicService.clic();
				}
			}
		}
		
	}
  
	@Override
	public void paint(float deltaTime) {
		
		Graphics g = game.getGraphics();
		g.drawImage(menu, 0, 0);
		
		paint.setColor(Color.WHITE);
		
		if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("es") || Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("it"))
			g.drawString(GameManager.res.getString(R.string.back), 675, 425, paint, 38);
		else
			g.drawString(GameManager.res.getString(R.string.back), 685, 425, paint, 38);
		
		paint.setColor(Color.BLACK);
		g.drawString(GameManager.res.getString(R.string.credit), 35, 45, paint, 50);
		
		g.drawString(GameManager.res.getString(R.string.creditdev), 75, 110, paint, 38);
		
		g.drawString(GameManager.res.getString(R.string.graphics), 75, 330, paint, 38);
		g.drawString("Plume2Gamer", 320, 330, paint, 50);
		
		g.drawString("Levels", 75, 390, paint, 38);
		g.drawString("Gauthier Beignie", 320, 390, paint, 50);
		
		for(int i=0;i<tabCharacter.size();i++)
		{
			y = tabCharacter.get(i).getY();
			operation = tabCharacter.get(i).getOperation();
			
			g.drawString(tabCharacter.get(i).getCharacter(), (int)(200+(i+1)*30), (int)(200+y), paint, 50);
			 
			if(operation) // +
				y+=2;
			else y-=2;
			
			if(y > 5 || y < -5) tabCharacter.get(i).setOperation(!operation);
			
			tabCharacter.get(i).setY(y);
		}
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
		game.setScreen(new MainMenuScreen(game));
	}
}

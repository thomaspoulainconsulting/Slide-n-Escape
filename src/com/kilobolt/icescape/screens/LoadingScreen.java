package com.kilobolt.icescape.screens;

import java.util.Locale;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Graphics.ImageFormat;
import com.kilobolt.framework.Screen;
import com.kilobolt.icescape.Assets;
import com.kilobolt.icescape.GameManager;
import com.kilobolt.icescape.R;

public class LoadingScreen extends Screen {
	
	private Paint paint;
	
	public LoadingScreen(Game game) {
		super(game);
		
		if(!GameManager.gm.getSave().retrievePrefs("no_ads")) {
			game.getAdMobHandler().sendEmptyMessage(View.VISIBLE);
		}
		
		paint = new Paint();
		paint.setColor(Color.argb(255, 231, 231, 226));
		paint.setTypeface(Typeface.createFromAsset(GameManager.context.getAssets(), "fonts/Gotham Nights.ttf"));
		paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAntiAlias(true);
		
		Graphics g = game.getGraphics();
		g.drawImage(g.newImage("menus/new_SnE_menu.png", ImageFormat.RGB565), 0, 0);
		
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
		g.drawImage(g.newImage("sprites/play-games.png", ImageFormat.RGB565), 5, 5);
		g.drawImage(g.newImage("sprites/fb.png", ImageFormat.RGB565), 5, 70);
		
		//Valeur par défaut de l'application
		if(GameManager.gm.getSave().retrievePrefs("AlreadyPlayed") == false){
			GameManager.gm.getSave().savePrefs("Son");
			GameManager.gm.getSave().savePrefs("Vibration");
		}
		
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		
		//RGB565 = sans transparence
		//ARGB4444 = avec transparence (prend plus de mémoire)
		
		/* SPRITES */
		Assets.etoile = g.newImage("sprites/etoile.png", ImageFormat.RGB565);
		Assets.etoilevide = g.newImage("sprites/etoilevide.png", ImageFormat.RGB565);
		Assets.etoile_small = g.newImage("sprites/etoile_small.png", ImageFormat.RGB565);
		Assets.trou = g.newImage("sprites/trou.png", ImageFormat.RGB565);
		Assets.bas = g.newImage("sprites/bas.png", ImageFormat.RGB565);
		Assets.gauche  = g.newImage("sprites/gauche.png", ImageFormat.RGB565);
		Assets.haut  = g.newImage("sprites/haut.png", ImageFormat.RGB565);
		Assets.droite  = g.newImage("sprites/droite.png", ImageFormat.RGB565);
		Assets.ice = g.newImage("sprites/ice.png", ImageFormat.RGB565);
		Assets.bloc = g.newImage("sprites/bloc2.png", ImageFormat.RGB565);
		Assets.bloc2 = g.newImage("sprites/bloc.png", ImageFormat.RGB565);
		Assets.end = g.newImage("sprites/end.png", ImageFormat.RGB565);
		Assets.corner_top_left = g.newImage("sprites/left_top.png", ImageFormat.RGB565);
		Assets.corner_top_right = g.newImage("sprites/top_right.png", ImageFormat.RGB565);
		Assets.corner_bottom_left = g.newImage("sprites/bottom_left.png", ImageFormat.RGB565);
		Assets.corner_bottom_right = g.newImage("sprites/bottom_right.png", ImageFormat.RGB565);
		Assets.stage_left = g.newImage("sprites/stage_left.png", ImageFormat.RGB565);
		Assets.stage_right = g.newImage("sprites/stage_right.png", ImageFormat.RGB565);
		Assets.stage_top = g.newImage("sprites/stage_top.png", ImageFormat.RGB565);
		Assets.stage_bottom = g.newImage("sprites/stage_bottom.png", ImageFormat.RGB565);
		Assets.empty = g.newImage("sprites/empty.png", ImageFormat.RGB565);
		Assets.blocsnow = g.newImage("sprites/bloc_snow.png", ImageFormat.RGB565);
		Assets.tree_b = g.newImage("sprites/bottom_tree.png", ImageFormat.RGB565);
		Assets.tree_m = g.newImage("sprites/middle_tree.png", ImageFormat.RGB565);
		Assets.tree_t = g.newImage("sprites/top_tree.png", ImageFormat.RGB565);
		Assets.arbuste1 = g.newImage("sprites/arbuste1.png", ImageFormat.RGB565);
		Assets.arbuste2 = g.newImage("sprites/arbuste2.png", ImageFormat.RGB565);
		Assets.arbuste3 = g.newImage("sprites/arbuste3.png", ImageFormat.RGB565);
		Assets.switch_off = g.newImage("sprites/switch_off.png", ImageFormat.RGB565);
		Assets.switch_on = g.newImage("sprites/switch_on.png", ImageFormat.RGB565);
		Assets.caisse = g.newImage("sprites/caisse.png", ImageFormat.RGB565);
		Assets.button_pause = g.newImage("sprites/pause.png", ImageFormat.RGB565);
		Assets.portal1 = g.newImage("sprites/portal1.png", ImageFormat.RGB565);
		Assets.portal2 = g.newImage("sprites/portal2.png", ImageFormat.RGB565);
		Assets.portal3 = g.newImage("sprites/portal3.png", ImageFormat.RGB565);
		Assets.portal4 = g.newImage("sprites/portal4.png", ImageFormat.RGB565);
		Assets.bas_reflet = g.newImage("sprites/bas_reflet.png", ImageFormat.RGB565);
		Assets.haut_reflet = g.newImage("sprites/haut_reflet.png", ImageFormat.RGB565);
		Assets.gauche_reflet = g.newImage("sprites/gauche_reflet.png", ImageFormat.RGB565);
		Assets.droite_reflet = g.newImage("sprites/droite_reflet.png", ImageFormat.RGB565);
		Assets.bloc_reflet = g.newImage("sprites/bloc_reflet.png", ImageFormat.RGB565);
		Assets.bloc2_reflet = g.newImage("sprites/bloc2_reflet.png", ImageFormat.RGB565);
		Assets.eclair = g.newImage("sprites/eclair.png", ImageFormat.RGB565);
		Assets.eclair2 = g.newImage("sprites/eclair2.png", ImageFormat.RGB565);
		Assets.ok_menucarte = g.newImage("sprites/ok_menucarte.png", ImageFormat.RGB565);
		     
		/* MENUS */
		Assets.pause = g.newImage("menus/pause.png", ImageFormat.RGB565);
		Assets.finish = g.newImage("menus/finish.png", ImageFormat.RGB565);
		Assets.gameover = g.newImage("menus/gameover.png", ImageFormat.RGB565);
		Assets.checkbox = g.newImage("menus/checkbox.png", ImageFormat.RGB565);
		Assets.nuage = g.newImage("menus/nuage.png", ImageFormat.RGB565);
		Assets.nuage2 = g.newImage("menus/nuage2.png", ImageFormat.RGB565);
		
		
		/* FONT */
		Assets.font = Typeface.createFromAsset(GameManager.context.getAssets(), "fonts/Gotham Nights.ttf");
		
		/* SOUND */
		Assets.button_click = game.getAudio().createSound("musics/button_click.ogg");
		
		game.setScreen(new MainMenuScreen(game));
	}

	public void paint(float deltaTime) {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
	}

	public void backButton() {
	}
}
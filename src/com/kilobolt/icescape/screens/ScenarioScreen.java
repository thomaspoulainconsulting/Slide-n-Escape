package com.kilobolt.icescape.screens;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.view.View;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Graphics.ImageFormat;
import com.kilobolt.framework.Input.TouchEvent;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.implementation.AndroidGraphics;
import com.kilobolt.icescape.Assets;
import com.kilobolt.icescape.ClicService;
import com.kilobolt.icescape.GameManager;
import com.kilobolt.icescape.R;

public class ScenarioScreen extends Screen{
	
	boolean finished=false;
	boolean preOrpost;
	Paint paint;
	float until;
	int i=0, iprime;
	int y = 130, x=0;
	Vibrator vibrator = (Vibrator) GameManager.context.getSystemService(Context.VIBRATOR_SERVICE);
	String textPreFR="Ethan s'est égaré dans une grotte"+'\n'+"de glace lors d'une expédition. "+'\n'+'\n'+"Il ne retrouve pas son chemin."+'\n'+"Aide-le à trouver la sortie !";
	String textPostFR="Bravo !"+'\n'+'\n'+"Grâce à toi Ethan à retrouvé"+'\n'+"son chemin."+'\n'+"Merci d'avoir joué !";
	String textPreEN="Ethan got lost in a ice cave"+'\n'+"during a expedition."+'\n'+'\n'+"He can't find his way out."+'\n'+"Help him finding the exit!";
	String textPostEN="Well done !"+'\n'+'\n'+"Thanks to you, Ethan has found"+'\n'+"the exit of the ice cave."+'\n'+"Thanks for playing !";
	String textPreGER="Ethan hat sich in einer Eisgrotte"+'\n'+"verirrte während einen Ausflug."+'\n'+'\n'+"Er kann seinen Weg nicht finden."+'\n'+"Hilf ihn den Ausgang zu finden.";
	String textPostGER="Well done!"+'\n'+'\n'+"Vielen Dank an Sie, hat Ethan"+'\n'+"den Ausgang der Eishöhle gefunden."+'\n'+"Danke fürs Mitspielen!";
	String textPreES ="Eco se ha perdido en una cueva"+'\n'+"helada durante una expedición."+'\n'+'\n'+"No puede encontrar la salida."+'\n'+"¡Ayúdale tú a encontrarla!";
	String textPostES="¡Bien hecho!"+'\n'+'\n'+"Gracias a ti, Eco ha encontrado"+'\n'+"la salida de la cueva helada."+'\n'+"¡Gracias por jugar!";
	String textPreIT = "Ethan si è perso in una caverna"+'\n'+"di ghiaccio durante una spedizione."+'\n'+'\n'+"Non riesce a trovare la sua via"+'\n'+"d'uscita. Aiutalo a trovare l'uscita!";
	String textPostIT= "Ben fatto!"+'\n'+'\n'+"Grazie a te, Ethan ha trovato l'uscita"+'\n'+"della grotta di ghiaccio."+'\n'+"Grazie per aver giocato!";
	
	
	String text="";
	 
	public ScenarioScreen(Game game, boolean preOrpost) { 
		super(game);
		Graphics g = game.getGraphics();
		g.drawImage(g.newImage("menus/scenario_back.png", ImageFormat.RGB565), 0, 0);
		
		Typeface font = Typeface.createFromAsset(GameManager.context.getAssets(), "fonts/Targa MS.ttf");
		game.getAdMobHandler().sendEmptyMessage(View.GONE);
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(font);
		paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
		
		this.preOrpost=preOrpost;
		if(preOrpost) {
			if(Locale.getDefault().getLanguage().equals("fr"))	text=textPreFR;
			else if(Locale.getDefault().getLanguage().equals("de"))	text=textPreGER;
			else if(Locale.getDefault().getLanguage().equals("es"))	text=textPreES;
			else if(Locale.getDefault().getLanguage().equals("it"))	text=textPreIT;
			else text=textPreEN;
		}
		else {
			if(Locale.getDefault().getLanguage().equals("fr"))	text=textPostFR;
			else if(Locale.getDefault().getLanguage().equals("de"))	text=textPostGER;
			else if(Locale.getDefault().getLanguage().equals("es"))	text=textPostES;
			else if(Locale.getDefault().getLanguage().equals("it"))	text=textPostIT;
			else text=textPostEN;
		}
	}

	@Override
	public void update(float deltaTime) {
		
		//Si clic sur suivant, on affiche le choix des mondes
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		int len = touchEvents.size(); 
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				
				this.iprime = this.i;
				this.i = text.length();
				
				if (AndroidGraphics.inBounds(event, 289, 375, 280, 105) && finished) {
					ClicService.clic();
					if(preOrpost)	game.setScreen(new WorldScreen(game));
					else game.setScreen(new MainMenuScreen(game));
				} 
			} 
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		
		if(i < text.length()) {
			if(until > 4)
			{
				if(text.charAt(i) == '\n') { x=0; y+=50; i++;}
				else if(i < text.length()){
					g.drawString(String.valueOf(text.charAt(i)),90+(19*x), y, paint, 40);
					
					until=0;
					i++;
					x++;
				}
			
			}else {
				until += deltaTime;	
			}
		} else if(i == text.length()) {
			
			for(int t=iprime;t<text.length();t++) {
				
				if(text.charAt(t) == '\n') { x=0; y+=50;}
				else{
					g.drawString(String.valueOf(text.charAt(t)),90+(19*x), y, paint, 40);
					x++;
				}
			}
			i++;
		} else {
			g.drawImage(Assets.ok_menucarte, 289, 350);
			paint.setColor(Color.WHITE);
			
			if(preOrpost) {
				if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("de")) g.drawString(GameManager.res.getString(R.string.letsgo), 299, 395, paint, 34);
				else if(Locale.getDefault().getLanguage().equals("es")) g.drawString(GameManager.res.getString(R.string.letsgo), 350, 395, paint, 34);
				else g.drawString(GameManager.res.getString(R.string.letsgo), 330, 395, paint, 34);
			}
			else {
				g.drawString("Okay", 370, 395, paint, 34);
			}
			finished=true;
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
	}
	
	

}

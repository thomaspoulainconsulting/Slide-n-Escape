package com.kilobolt.icescape;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.kilobolt.framework.Image;
import com.kilobolt.framework.Music;
import com.kilobolt.framework.Sound;

public class Assets {
	
	public static Image etoile, pause, finish, etoilevide, etoile_small, trou, gameover, checkbox;
	public static Image ice, bloc, bloc2, end;
	public static Music musloop;
	public static Sound button_click;
	public static Image caisse, nuage, nuage2;
	public static Typeface font;
	
	public static Image corner_top_left, stage_top, stage_left, stage_bottom, stage_right, empty, corner_top_right, corner_bottom_left, corner_bottom_right, blocsnow;
	public static Image portal1, portal2, portal3, portal4;
	
	public static Image bas, gauche, droite, haut;
	public static Image tree_b, tree_m, tree_t, arbuste1, arbuste2, arbuste3;
	public static Image switch_off, switch_on;
	public static Image button_pause;
	public static Image bas_reflet, haut_reflet, gauche_reflet, droite_reflet;
	public static Image bloc2_reflet, bloc_reflet;
	public static Image eclair, eclair2;
	public static Image ok_menucarte;
	
	public static void loadMusic(GameManager sampleGame) {
		
		musloop = sampleGame.getAudio().createMusic("musics/musloop.ogg");
		musloop.setLooping(true);
	}

	
	public static void playMusic()
	{
		musloop.play();
	}
	
	public static boolean MusIsPlaying()
	{
		return musloop.isPlaying();
	}
	
	public static void playMusic(GameManager sampleGame)
	{
		SharedPreferences prefs = sampleGame.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		if(prefs.getBoolean("Son", false)) musloop.play();
	}
	
	public static void stopMusic()
	{
		musloop.stop();
	}
	
}

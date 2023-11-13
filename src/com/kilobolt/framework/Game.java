package com.kilobolt.framework;

import android.app.Activity;

import com.kilobolt.framework.implementation.AdMobHandler;
import com.kilobolt.framework.implementation.AndroidIAP;

public interface Game {
	
    public static String SKU_LEVEL_UNLOCK = "level_unlock";
	public static String SKU_WORLD_UNLOCK = "world_unlock";
	public static String SKU_NO_ADS = "no_ads";

    public Audio getAudio();

    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getInitScreen(Activity ac);
    
    public Save getSave();

	public Activity getActivity();

	public AdMobHandler getAdMobHandler();
	
	public AndroidIAP getIAP();
	
	/* Concerne les achievements */
	public void showAchievements();
	
	public void signIn();
	
	public void unlock(int achievement);

	public void increment(int achievementFinishAllWorlds);
	
	/* Concerne les redirection vers d'autres pages web */
	
	public void startFacebookIntent();
	
	public void startGooglePlayIntent();
	
	
}

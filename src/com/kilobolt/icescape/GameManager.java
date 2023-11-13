package com.kilobolt.icescape;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Screen;
import com.kilobolt.framework.implementation.AndroidGame;
import com.kilobolt.icescape.screens.SplashLoadingScreen;

public class GameManager extends AndroidGame {
 
	private boolean firstTimeCreate = true;
	public static Context context;
	public static Activity activity;
	public static Game gm;
	public static Resources res;
	public static int NBLEVEL=12;

	@SuppressWarnings("static-access")
	public Screen getInitScreen(Activity activity) {

		if (firstTimeCreate) { 
			Assets.loadMusic(this);
			this.firstTimeCreate = false;
		}
		
		this.context = getApplicationContext();
		this.gm=this;
		this.res=getResources();
		this.activity=activity;
		return new SplashLoadingScreen(this);
	}

	public void onBackPressed() {
		getCurrentScreen().backButton();
	}
	

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	public static boolean isConnected(){
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}

}
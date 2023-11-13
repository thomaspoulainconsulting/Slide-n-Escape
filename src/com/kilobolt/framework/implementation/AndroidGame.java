package com.kilobolt.framework.implementation;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.kilobolt.framework.Audio;
import com.kilobolt.framework.FileIO;
import com.kilobolt.framework.Game;
import com.kilobolt.framework.Graphics;
import com.kilobolt.framework.Input;
import com.kilobolt.framework.Save;
import com.kilobolt.framework.Screen;
import com.kilobolt.icescape.GameManager;
import com.kilobolt.icescape.R;
import com.kilobolt.icescape.map.Map;

public abstract class AndroidGame extends BaseGameActivity implements Game, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
   
	private AndroidFastRenderView renderView;
	private Graphics graphics;
	private Audio audio;
	private Input input;
	private FileIO fileIO;
	private Screen screen;
	private Save save;
	private AdView adView;
	private AdMobHandler admobHandler;
	private AndroidIAP androidIAP;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //Restaurer le contexte dans le cas où on quitte la l'appli et qu'on l'a reprend
        if(savedInstanceState != null) {
        	onRestoreInstanceState(savedInstanceState); 
        }
        

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int frameBufferWidth = isPortrait ? 480: 800;
        int frameBufferHeight = isPortrait ? 800: 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
        
        // getWidth & getHeight sont deprecated mais fonctionne sur toutes les API
        float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen(this);
        save = new AndroidSave(this);
        androidIAP = new AndroidIAP(this);
        
        //Gestion de la publicité
        RelativeLayout layout = new RelativeLayout(this); 
        layout.addView(renderView); 
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-4710721091947185/6343404755");
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( 
        RelativeLayout.LayoutParams.MATCH_PARENT, 
        RelativeLayout.LayoutParams.WRAP_CONTENT); 
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); 
        params.addRule(RelativeLayout.CENTER_IN_PARENT); 
        layout.addView(adView, params); 	
        setContentView(layout); 
 
        this.admobHandler = new AdMobHandler(adView, getActivity()); 
        this.admobHandler.sendEmptyMessage(View.VISIBLE);
        
        signIn();
        loadAchievementUnlocked();
    }
    
    public void signIn() {
    	beginUserInitiatedSignIn();
        getApiClient().connect();
    }
    
    /**
     * Chargement des achievements pour les personnes ayant déjà finit les niveaux
     */
    private void loadAchievementUnlocked() {
    	if(getApiClient().isConnected()) {
    		
			if(Map.isCompleted(1, 12) ){
				GameManager.gm.unlock(R.string.achievement_world_1_finished);
			} else if(Map.isCompleted(2, 12)) {
				GameManager.gm.unlock(R.string.achievement_world_2_finished);
			} else if(Map.isCompleted(3, 12)) {
				GameManager.gm.unlock(R.string.achievement_world_3_finished);
			} else if(Map.isCompleted(4, 12)) {
				GameManager.gm.unlock(R.string.achievement_world_4_finished);
			} else if(Map.isCompleted(5, 12)) {
				GameManager.gm.unlock(R.string.achievement_world_5_finished);
			} else if(Map.isCompleted(6, 12)) {
				GameManager.gm.unlock(R.string.achievement_world_6_finished);
			}				
			
    	}
    }
    
    public void showAchievements() {
    	
    	if(getApiClient().isConnected()) {
    		startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 1001);
    	} else {
    		signIn();
    	}
    }
    
    
    public void unlock(int achievement) {
    	if(getApiClient().isConnected()) {
    		Games.Achievements.unlock(getApiClient(), getResources().getString(achievement));
    	} 
    }
    
    public void increment(int achievement) {
    	if(getApiClient().isConnected()) {
    		Games.Achievements.increment(getApiClient(), getResources().getString(achievement), 1);
    	} 
    }
    
    
    public void onStart() {
    	super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        admobHandler.getView().resume();
        screen.resume();
        renderView.resume();
        
		if(!admobHandler.getLoaded()) {
			AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice("627C845E37C0BC72E2472B7662BC6ABB")
				.build();
			admobHandler.getView().loadAd(adRequest); 
		}
    }

    @Override
    public void onPause() {
        super.onPause();
        admobHandler.getView().pause();
        renderView.pause();
        screen.pause();

        if (isFinishing()) {
            screen.dispose();
        }
    }
    
    @Override
    public void onDestroy() {
      admobHandler.getView().destroy();
      super.onDestroy();
    }
    
    public AdMobHandler getAdMobHandler() {
    	return admobHandler;
    }
    
    public Activity getActivity() {
    	return this;
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }
    
    @Override
    public AndroidIAP getIAP() {
    	return androidIAP;
    }
    
    @Override
    public Audio getAudio() {
        return audio;
    }
    
    public Save getSave() {
    	return save;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen() {
    	return screen;
    }
    
    
    @Override
	public void onSignInFailed() {
		Log.e("iceLog", "SignIn Failed");
	}


	@Override
	public void onSignInSucceeded() {
		Log.e("iceLog", "SignIn succed");
	}


	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.e("iceLog", "onConnectionFailed"+arg0.getErrorCode());
	}


	@Override
	public void onConnected(Bundle arg0) {
	}
  

	@Override
	public void onDisconnected() {
	}
  

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if (!this.androidIAP.getmHelper().handleActivityResult(requestCode, resultCode, data)) {
    		super.onActivityResult(requestCode, resultCode, data);
    	}
    }
	
	@Override
	public void startFacebookIntent() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/SlidenEscape"));
		startActivity(browserIntent);
	}
	
	@Override
	public void startGooglePlayIntent() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.kilobolt.icescape"));
		startActivity(browserIntent);
	}
	
}
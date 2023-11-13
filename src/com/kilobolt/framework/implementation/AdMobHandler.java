package com.kilobolt.framework.implementation;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

public class AdMobHandler extends Handler { 
	private AdView adView=null; 
	private boolean loaded=false;
	private PublisherInterstitialAd interstitial;
	
	public AdMobHandler(AdView adView, Activity context) { 
		super(); 
		this.adView = adView; 
		
		this.adView.setAdListener(new AdListener() {
			
			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				loaded=true;
			}
		}); 
		
		interstitial = new PublisherInterstitialAd(context);
		interstitial.setAdUnitId("ca-app-pub-4710721091947185/7570486354");

	    // Begin loading your interstitial
		PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
			.addTestDevice("627C845E37C0BC72E2472B7662BC6ABB")
			.build();
	    interstitial.loadAd(adRequest);
	    interstitial.setAdListener(new AdListener() {
	    	@Override
	    	public void onAdClosed() {
	    		loadNextInterstitial();
	    		super.onAdClosed();
	    	}
		});
	} 
	
	@Override 
	public void handleMessage(Message msg) { 
		if ( adView.getVisibility() != msg.what ) { 
			adView.setVisibility(msg.what); 
		} 
	} 

	public boolean getLoaded() {
		return loaded;
	}
	
	public AdView getView() {
		return adView;
	}

	public void showInterstitial() {
		interstitial.show();
	}
	
	public void loadNextInterstitial() {
		PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
		.addTestDevice("627C845E37C0BC72E2472B7662BC6ABB")
		.build();
		interstitial.loadAd(adRequest);
	}
	
	public PublisherInterstitialAd getInterstitial() {
		return interstitial;
	}
} 
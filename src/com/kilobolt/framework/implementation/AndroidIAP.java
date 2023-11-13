package com.kilobolt.framework.implementation;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.utils.IabHelper;
import com.android.utils.IabResult;
import com.android.utils.Inventory;
import com.android.utils.Purchase;
import com.kilobolt.framework.Game;
import com.kilobolt.icescape.GameManager;
import com.kilobolt.icescape.screens.LevelScreen.states;
import com.kilobolt.icescape.screens.StoreScreen;

public class AndroidIAP {
	
	private IabHelper mHelper;
    private String PAYLOAD = "";
    private Activity activity;
    private StoreScreen storeScreen;
    
    public AndroidIAP(Activity activity) {

    	this.activity=activity;
    	
    	this.mHelper = new IabHelper(GameManager.context, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApm0nzwyeFJdDBokdObqa3b7NlZOuwDkCjMhcPI0pYhTP0+HV97ZTLkWuge2iIn5B6TPdMUKs/LyHVwtITRwwzHlJI6RwB6/nCMXX46lrDw5rvrRkAXNE+zCWJ71ODJhk66cTSK4Ox5IMFvybqhub0GB/Dn2K+J6fBqgfnA3o1KZ4kbT7oucXeM6pCuXOcwGIPLhHeOddke4PwLRRxtLJU4roDoOV9WujR/R44XaZqT6ZcJab5YCLwWLifdL5FPTFimWPMJ1d0Rsm2KcyBPrrHLRTgTjHqS0kPI6AevOc5Re89CGWnaSrFCOp6/NQoxkhc8R/G6VBiXO8WJtRzMMdAwIDAQAB");
        this.mHelper.enableDebugLogging(true);
        
        AccountManager accountManager = AccountManager.get(GameManager.context);
		Account[] accounts = accountManager.getAccountsByType("com.google");
		if(accounts.length > 0) PAYLOAD = accounts[0].name;
  
		//Vérification si un achat avait été fait, on le consomme
		this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					Log.d("test", "Problem setting up In-app Billing: " + result);
				}            
   
				Log.d("test", "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});  
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {

		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d("test", "Purchase finished: " + result + ", purchase: " + purchase);
			if (result.isFailure()) {
				Log.d("test","Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				Log.d("test","Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d("test", "Purchase successful.");

			if (purchase.getSku().equals(Game.SKU_LEVEL_UNLOCK)) {
				// bought  indices/unlocked levels. So consume it.
				Log.d("test", "Purchase is level unlock. Starting indice consumption.");
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}
			
			if (purchase.getSku().equals(Game.SKU_WORLD_UNLOCK)) {
				// bought  indices/unlocked levels. So consume it.
				Log.d("test", "Purchase is world unlock. Starting indice consumption.");
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}
			
			if (purchase.getSku().equals(Game.SKU_NO_ADS)) {
				// bought  indices/unlocked levels. So consume it.
				Log.d("test", "Purchase is no ads. Starting indice consumption.");
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}
			
		}
	};
	
	
	
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d("test", "Query inventory finished.");
			if (result.isFailure()) {
				Log.d("test", "Failed to query inventory: " + result);
				return;
			}

			Log.d("test", "Query inventory was successful.");

			Purchase purchase = inventory.getPurchase(Game.SKU_LEVEL_UNLOCK);
			if (purchase != null && verifyDeveloperPayload(purchase)) {
				Log.d("test", "We have unlock a level. Consuming it.");
				mHelper.consumeAsync(inventory.getPurchase(Game.SKU_LEVEL_UNLOCK), mConsumeFinishedListener);
				return;
			}
			
			purchase = inventory.getPurchase(Game.SKU_NO_ADS);
			if (purchase != null && verifyDeveloperPayload(purchase)) {
				Log.d("test", "We have no ads. Consuming it.");
				mHelper.consumeAsync(inventory.getPurchase(Game.SKU_NO_ADS), mConsumeFinishedListener);
				return;
			}
			
			purchase = inventory.getPurchase(Game.SKU_WORLD_UNLOCK);
			if (purchase != null && verifyDeveloperPayload(purchase)) {
				Log.d("test", "We have unlock a world. Consuming it.");
				mHelper.consumeAsync(inventory.getPurchase(Game.SKU_NO_ADS), mConsumeFinishedListener);
				return;
			}
			
		}
	};
	
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d("test", "Consumption finished. Purchase: " + purchase + ", result: " + result);
			
//			Log.e("test", "result:"+result.isSuccess()+" purchause:"+purchase.getSku()+"-"+SKU_LEVEL_UNLOCK);

			if (result.isSuccess() && purchase.getSku().equals(Game.SKU_LEVEL_UNLOCK)) {
				Log.e("iceLog", "success! purchase SKU_LEVEL_UNLOCK");
				GameManager.gm.getSave().save("unlock_level_credit", GameManager.gm.getSave().retrieve("unlock_level_credit")+1);
				
				//Affiche une fenetre de confirmation d'achat
				storeScreen.state=states.NOTIFICATION_UNLOCKING_LEVEL;
				
			} else if(result.isSuccess() && purchase.getSku().equals(Game.SKU_NO_ADS)) {
				Log.e("iceLog", "success! purchase SKU_NO_ADS");
				GameManager.gm.getSave().savePrefs("no_ads");
				
				//Retire la pub sur la page actuelle
				((Game)activity).getAdMobHandler().sendEmptyMessage(View.GONE);
				
				//Affiche une fenetre de confirmation d'achat
				storeScreen.state=states.NOTIFICATION_REMOVAL_ADS;
				
			} else if(result.isSuccess() && purchase.getSku().equals(Game.SKU_WORLD_UNLOCK)) {
				Log.e("iceLog", "success! purchase SKU_WORLD_UNLOCK");
				GameManager.gm.getSave().save("unlock_world_credit", GameManager.gm.getSave().retrieve("unlock_world_credit")+1);
				
				//Affiche une fenetre de confirmation d'achat
				storeScreen.state=states.NOTIFICATION_UNLOCKING_WORLD;
				
			}
		}
	};
	
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		  
		boolean equals=false;
		if(payload.equals(PAYLOAD)) {
			equals=true;
		}
		
		return equals;
	} 

	public IabHelper getmHelper() {
		return mHelper;
	}
	
	public void launchPurchase(String SKU, StoreScreen storeScreen) {
		
		this.storeScreen = storeScreen;
		
		try {
			mHelper.launchPurchaseFlow(this.activity, SKU, 1001, mPurchaseFinishedListener, PAYLOAD);
		}catch(Exception e) { 
			Toast.makeText(this.activity.getApplicationContext(), "try again in a few seconds", Toast.LENGTH_SHORT).show();
		}
	}

}

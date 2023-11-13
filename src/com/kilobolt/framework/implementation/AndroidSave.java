package com.kilobolt.framework.implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.kilobolt.framework.Save;

public class AndroidSave implements Save{
	Context context;
	
	
	AndroidSave(Context context){
		this.context = context;
	}
	
	public void save(String key, int score)
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(key, score);
		editor.commit();
	}
	
	public void save(String key, float temps)
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putFloat(key, temps);
		editor.commit();
	}
	
	public void savePrefs(String key)
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		if(prefs.getBoolean(key, false))
			editor.putBoolean(key, false);
		else
			editor.putBoolean(key, true);
		
		editor.commit();
	}
	
	public boolean retrievePrefs(String key)
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		return prefs.getBoolean(key, false); //false is the default value
	}
	
	public int retrieve(String key)
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		return prefs.getInt(key, 0); //0 is the default value
	}
	
	public double retrieveTime(String key)
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		return prefs.getFloat(key, 0); //0 is the default value
	}
	
	public void suppressAll()
	{
		//SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		
		/*for(int i=1;i<=GameManager.NBLEVEL;i++)
		{
			prefs.edit().remove(1+"-"+i+"-score");
			prefs.edit().commit();
			prefs.edit().remove(1+"-"+i+"-etoile");
			prefs.edit().commit();
			prefs.edit().remove(1+"-"+i+"-coups");
			prefs.edit().commit();
			prefs.edit().remove(1+"-"+i+"-temps");
			prefs.edit().commit();
		}*/
		
		
		//Log.i("iceLog", "Suppresion des sauvegardes");
		try{
		Editor editor = 
				context	.getSharedPreferences("IceScape", Context.MODE_PRIVATE).edit();
				editor.clear();
				editor.commit();
		}catch(Exception e){ /*Log.i("iceLog", e.getMessage());*/ }
		
		
	}
	
	public void showingInterstitial()
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();  
		editor.putInt("nbshowinglevel",0);
		editor.commit();
	}
	
	public boolean isOkToShowInterstitial()
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		int nb =  prefs.getInt("nbshowinglevel", 0); //0 is the default value
		
		if(nb >= 11) return true;
		else return false;
	}
	
	public void showingLevels()
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();  
		editor.putInt("nbshowinglevel",getNbShowingLevelGen()+1);
		editor.commit();
	}
	
	public int getNbShowingLevelGen()
	{
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		return prefs.getInt("nbshowinglevel", 0); //0 is the default value
	}
	
	
	public boolean askingRate() {
		
		SharedPreferences prefs = context.getSharedPreferences("IceScape", Context.MODE_PRIVATE);
		int behaviour = prefs.getInt("behaviour", -1); 
		int behaviourWait = prefs.getInt("behaviourWait", -1); 
		
		Log.e("iceLog", "behaviour "+behaviour);
		 
		//Si on a jamais affiché la demande
		if(behaviour == -1) {
			Log.e("iceLog", "asking 1ere fois");
			return true;
		} 
		//Si la demande a été accepté ou refusé, on ne redemande plus
		else if(behaviour == 1 || behaviour == 0) {
			Log.e("iceLog", "déjà accepte ou refusé");
			return false;
		} 
		//Si l'utilisateur à dis "plus tard" et que c'est le moment de demander
		else if(behaviour == 2 && behaviourWait == 0) {
			Log.e("iceLog", "plus tard a été coché & c'est le temps de demander");
			return true;
		} 
		//Diminition du behaviourWait
		else {
			
			Editor editor = prefs.edit();  
			editor.putInt("behaviourWait",prefs.getInt("behaviourWait", -1)-1);
			editor.commit();
			Log.e("iceLog", "behaviourWait--"+behaviourWait);
			return false;
		}
	}
}

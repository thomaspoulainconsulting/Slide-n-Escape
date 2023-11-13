package com.kilobolt.framework;

public interface Save {

	public void save(String key, int score);
	public void save(String key, float temps);
	public void savePrefs(String key);
	public int retrieve(String key);
	public double retrieveTime(String key);
	public boolean retrievePrefs(String key);
	public void suppressAll();
	public int getNbShowingLevelGen();
	public void showingLevels();
	public boolean isOkToShowInterstitial();
	public void showingInterstitial();
	public boolean askingRate();
}

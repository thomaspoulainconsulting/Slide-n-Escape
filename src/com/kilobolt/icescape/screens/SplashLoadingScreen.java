package com.kilobolt.icescape.screens;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Screen;

public class SplashLoadingScreen extends Screen {
	public SplashLoadingScreen(Game game) {
		super(game);
	}

	public void update(float deltaTime) {
		game.setScreen(new LoadingScreen(game));
	}

	@Override
	public void paint(float deltaTime) {

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
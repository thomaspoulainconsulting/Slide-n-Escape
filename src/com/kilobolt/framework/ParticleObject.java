package com.kilobolt.framework;

import android.graphics.Paint;

public class ParticleObject {
	
	private int x, y, radius;
	
	public ParticleObject(int x, int y, int radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void show(Game game, Paint paint)
	{
		Graphics g = game.getGraphics();
		g.drawCircle(x, y, radius, paint);
	}

}

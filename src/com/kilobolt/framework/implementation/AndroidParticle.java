package com.kilobolt.framework.implementation;

import android.graphics.Paint;

import com.kilobolt.framework.Game;
import com.kilobolt.framework.Particle;
import com.kilobolt.framework.ParticleObject;

public class AndroidParticle implements Particle{
	
	
	public void activateSnow(Game game, Paint paint)
	{
		
		
		//for(int x=1;x<17;x++)
		//{
			for(int y=1;y<25;y++)
			{
				ParticleObject object = new ParticleObject(10, y*20, 5);
				object.show(game, paint);
			}
			
			
		//}
		
		
	}

}

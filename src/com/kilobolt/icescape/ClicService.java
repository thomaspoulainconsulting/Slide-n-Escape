package com.kilobolt.icescape;

import android.content.Context;
import android.os.Vibrator;

public class ClicService {
	
	private static Vibrator vibrator = (Vibrator) GameManager.context.getSystemService(Context.VIBRATOR_SERVICE);
	
	public static void clic() {
		if(GameManager.gm.getSave().retrievePrefs("Vibration")) vibrator.vibrate(50);
		Assets.button_click.play(100);
	}

}

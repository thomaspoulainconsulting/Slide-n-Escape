package com.kilobolt.icescape.map;

import com.kilobolt.framework.Image;
import com.kilobolt.icescape.Assets;

public class Tile {

	private int tileX, tileY;
	public char type;
	public Image tileImage;

	public Tile(int x, int y, char typeInt) {
		tileX = x * 40;
		tileY = y * 40;

		type = typeInt;

		if (type == '5') 
			tileImage = Assets.bloc2;
		 else if (type == '4') 
			tileImage = Assets.stage_right;
		 else if (type == '8') 
			tileImage = Assets.stage_bottom;
		else if (type == '6') 
			tileImage = Assets.stage_left;
		 else if (type == '2') 
			tileImage = Assets.stage_top;
		 else if(type == '1') 
		    tileImage = Assets.bloc;
		 else if(type == '3')
			 tileImage = Assets.end;
		 else if(type == '7')
			 tileImage = Assets.trou;
		 else if(type == 'o')
			 tileImage = Assets.ice;
		 else if(type == '0')
			 tileImage = Assets.empty;
		 else if(type == 'a')
			 tileImage = Assets.corner_top_left;
		 else if(type == 'b')
			 tileImage = Assets.corner_top_right;
		 else if(type == 'c')
			 tileImage = Assets.corner_bottom_left;
		 else if(type == 'd')
			 tileImage = Assets.corner_bottom_right;
		 else if(type == 'e')
			 tileImage = Assets.blocsnow;
		 else if(type == 'f')
			 tileImage = Assets.tree_b;
		 else if(type == 'g')
			 tileImage = Assets.tree_m;
		 else if(type == 'h')
			 tileImage = Assets.tree_t;
		 else if(type == 'i')
			 tileImage = Assets.arbuste1;
		 else if(type == 'j')
			 tileImage = Assets.arbuste2;
		 else if(type == 'k')
			 tileImage = Assets.arbuste3;
		 else if(type == 'l')
			 tileImage = Assets.switch_off;
		 else if(type == 'm')
			 tileImage = Assets.caisse;
		 else if(type == 'p'){
			 tileImage = Assets.portal1;
			 
			 
			 
		 }else
		 {
			 type = 'o';
			 tileImage = Assets.ice;  
		 }
		

	}


	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public Image getTileImage() {
		return tileImage;
	}
	
	public char getType() { return type; }

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}


}
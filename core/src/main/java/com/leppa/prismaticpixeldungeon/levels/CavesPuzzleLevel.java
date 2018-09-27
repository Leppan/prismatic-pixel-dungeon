package com.leppa.prismaticpixeldungeon.levels;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.levels.PuzzleLevel;

public class CavesPuzzleLevel extends PuzzleLevel{
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		
		viewDistance = Math.min(6, viewDistance);
	}
	
	@Override
	public String tilesTex(){
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex(){
		return Assets.WATER_CAVES;
	}
}

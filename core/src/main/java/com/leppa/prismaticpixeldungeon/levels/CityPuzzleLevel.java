package com.leppa.prismaticpixeldungeon.levels;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.levels.PuzzleLevel;

public class CityPuzzleLevel extends PuzzleLevel{
	
	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}
	
	@Override
	public String tilesTex(){
		return Assets.TILES_CITY;
	}
	
	@Override
	public String waterTex(){
		return Assets.WATER_CITY;
	}
}

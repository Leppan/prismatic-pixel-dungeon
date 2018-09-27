package com.leppa.prismaticpixeldungeon.levels;

import com.leppa.prismaticpixeldungeon.Assets;

public class PrisonPuzzleLevel extends PuzzleLevel{
	
	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}
	
	@Override
	public String tilesTex(){
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex(){
		return Assets.WATER_PRISON;
	}
}

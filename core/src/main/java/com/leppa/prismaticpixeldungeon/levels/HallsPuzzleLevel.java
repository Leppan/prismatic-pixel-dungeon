package com.leppa.prismaticpixeldungeon.levels;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.Dungeon;

public class HallsPuzzleLevel extends PuzzleLevel{
	
	{
		viewDistance = Math.min( 26 - Dungeon.depth, viewDistance );
		
		color1 = 0x801500;
		color2 = 0xa68521;
	}
	
	@Override
	public String tilesTex(){
		return Assets.TILES_HALLS;
	}
	
	@Override
	public String waterTex(){
		return Assets.WATER_HALLS;
	}
}

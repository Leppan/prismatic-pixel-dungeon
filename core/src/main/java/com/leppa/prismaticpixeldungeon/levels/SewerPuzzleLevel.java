package com.leppa.prismaticpixeldungeon.levels;


import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.Bones;
import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.mobs.Mob;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.AnyPushablePuzzleSheep;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.HorizontalPuzzleSheep;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.Sheep;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.VerticalPuzzleSheep;
import com.leppa.prismaticpixeldungeon.items.Heap;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.levels.puzzle.PressurePad;
import com.leppa.prismaticpixeldungeon.levels.traps.DistortionTrap;
import com.leppa.prismaticpixeldungeon.levels.traps.FleecingTrap;
import com.leppa.prismaticpixeldungeon.levels.traps.Trap;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

public class SewerPuzzleLevel extends PuzzleLevel{
	
	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}
	
	@Override
	public String tilesTex(){
		return Assets.TILES_SEWERS;
	}
	
	@Override
	public String waterTex(){
		return Assets.WATER_SEWERS;
	}
	
	protected void setup(){
		int[] pressurePadStages = {0, 1, 1, 2, 2, 2, 2, 3, 3, 4, 4};
		int[] pressurePadColours = {0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2};
		int[] stageDoors = {0, 1, 3, 4, 2};
		int[] padsPerStage = {1, 2, 4, 2, 2};
		this.pressurePadStages = pressurePadStages;
		this.pressurePadColours = pressurePadColours;
		this.stageDoors = stageDoors;
		this.padsPerStage = padsPerStage;
	}
}
package com.leppa.prismaticpixeldungeon.levels;

import android.util.Log;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.actors.mobs.Mob;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.AnyPushablePuzzleSheep;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.HorizontalPuzzleSheep;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.Sheep;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.VerticalPuzzleSheep;
import com.leppa.prismaticpixeldungeon.levels.traps.DistortionTrap;
import com.leppa.prismaticpixeldungeon.levels.traps.FleecingTrap;
import com.leppa.prismaticpixeldungeon.levels.traps.Trap;

public class PrisonPuzzleLevel extends PuzzleLevel{
	
	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}
	
	public PrisonPuzzleLevel(){
		int[] MAP_START =
				{W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, S, e, S, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, e, E, e, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, M, W, W, D, W, M, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, e, e, e, e, e, e, T, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, e, e, e, e, e, P, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, P, e, e, e, e, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, P, e, e, e, e, e, P, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, M, L, M, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, e, e, e, e, e, P, T, W, W, W, W, W, W,
						W, W, W, W, W, W, P, e, e, e, e, W, W, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, e, e, e, W, W, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, P, e, e, P, e, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, W, W, e, e, e, e, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, W, W, e, e, e, e, P, e, W, W, W, W, W, W,
						W, W, W, W, W, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, W, T, W, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, S, e, e, e, S, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, e, e, X, e, e, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, S, e, e, e, S, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
						W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};
		
		this.MAP_START = MAP_START;
		
		int[] pressurePadStages = {0, 0, 0, 0, 1, 1, 1, 1, 1};
		this.pressurePadStages = pressurePadStages;
		int[] pressurePadColours = {0, 0, 0, 0, 0, 0, 0, 0, 0};
		this.pressurePadColours = pressurePadColours;
		int[] stageDoors = {0, 1};
		this.stageDoors = stageDoors;
		int[] padsPerStage = {4, 5};
		this.padsPerStage = padsPerStage;
		Trap[] traps = {new DistortionTrap().reveal(), new DistortionTrap().reveal(), new FleecingTrap().reveal()};
		this.traps = traps;
	}
	
	@Override
	public String tilesTex(){
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex(){
		return Assets.WATER_PRISON;
	}
	
	protected void createMobs(){
		int[] sheepPositions   = {7 + 7 * width               , 8 + 7 * width              , 13 + 7 * width           , 7 + 8 * width              , 7 + 17 * width             , 12 + 17 * width            , 13 + 15 * width          , 12 + 18 * width             , 10 + 17 * width             };
		Sheep[] puzzleSheep    = {new AnyPushablePuzzleSheep(), new HorizontalPuzzleSheep(), new VerticalPuzzleSheep(), new HorizontalPuzzleSheep(), new HorizontalPuzzleSheep(), new HorizontalPuzzleSheep(), new VerticalPuzzleSheep(), new AnyPushablePuzzleSheep(), new AnyPushablePuzzleSheep()};
		
		for(int i = 0; i < puzzleSheep.length; i++){
			Sheep sh = puzzleSheep[i];
			sh.pos = sheepPositions[i];
			mobs.add(sh);
		}
		
		for(Mob m : mobs){
			if(map[m.pos] == Terrain.HIGH_GRASS){
				map[m.pos] = Terrain.GRASS;
				losBlocking[m.pos] = false;
			}
		}
	}
}

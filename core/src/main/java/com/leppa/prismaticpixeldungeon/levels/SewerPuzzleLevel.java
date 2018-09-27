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
	
	@Override
	public Mob createMob(){
		return null;
	}
	
	private static final int W = Terrain.WALL;
	private static final int D = Terrain.DOOR;
	private static final int L = Terrain.LOCKED_DOOR;
	private static final int e = Terrain.EMPTY;
	
	private static final int P = Terrain.PRESSUREPAD;
	
	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;
	
	private static final int M = Terrain.WALL_DECO;
	private static final int S = Terrain.STATUE;
	private static final int T = Terrain.TRAP;
	
	//TODO if I ever need to store more static maps I should externalize them instead of hard-coding
	//Especially as I means I won't be limited to legal identifiers
	private static final int[] MAP_START =
			{W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, S, e, S, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, E, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, M, D, M, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, P, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, M, L, M, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, e, e, e, e, e, e, e, e, T, W, W, W, W, W, W,
					W, W, W, W, W, W, e, e, P, e, e, e, P, e, e, W, W, W, W, W, W,
					W, W, W, W, W, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
					W, W, W, M, W, W, W, W, W, M, L, M, W, W, W, W, W, M, W, W, W,
					W, W, e, e, e, e, W, e, e, e, e, e, e, T, W, e, e, e, e, W, W,
					W, W, e, P, e, e, L, e, e, P, e, P, e, e, L, e, e, P, e, W, W,
					W, W, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, W, W,
					W, W, W, W, W, W, W, W, D, W, e, W, D, W, W, W, W, W, W, W, W,
					W, W, W, W, W, e, e, e, e, W, e, W, e, e, e, e, W, W, W, W, W,
					W, W, W, W, W, e, P, e, e, W, e, W, e, e, P, e, W, W, W, W, W,
					W, W, W, W, W, e, e, P, e, W, T, W, e, P, e, e, W, W, W, W, W,
					W, W, W, W, W, e, e, e, e, W, e, W, e, e, e, e, W, W, W, W, W,
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
}
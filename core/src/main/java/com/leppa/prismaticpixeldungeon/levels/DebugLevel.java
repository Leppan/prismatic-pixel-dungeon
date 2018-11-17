package com.leppa.prismaticpixeldungeon.levels;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.blobs.WaterOfHealth;
import com.leppa.prismaticpixeldungeon.actors.blobs.WellWater;
import com.leppa.prismaticpixeldungeon.actors.mobs.Jellyfish;
import com.leppa.prismaticpixeldungeon.actors.mobs.Mob;

public class DebugLevel extends Level{
	
	int[] MAP;
	String stringMap = 	"WWWWWWWWWWWWWWWWW" +
						"W               W" +
						"W               W" +
						"W               W" +
						"W               W" +
						"W               W" +
						"Wwwww           W" +
						"Wwwww           W" +
						"Wwwww   p       W" +
						"Wwwww           W" +
						"Wwwww           W" +
						"W               W" +
						"W               W" +
						"W               W" +
						"W               W" +
						"WWWWWWWWWWWWWWWWW";

	{
		viewDistance = 12;
	}

	protected boolean build(){
		setSize(17, 16);
		
		map = MAP.clone();
		entrance = 9+16;
		
		buildFlagMaps();
		cleanWalls();
		
		set(15 + 7 * width, Terrain.WELL, this);
		WellWater.seed(15 + 7 * width, 1, WaterOfHealth.class, this);
		return true;
	}
	
	public DebugLevel(){
		MAP = HardcodedMapHelper.mapStringToMapIntArray(stringMap);
	}
	
	public String tilesTex(){
		return Assets.TILES_CHAOS;
	}
	
	public String waterTex(){
		return Assets.WATER_CHAOS;
	}
	
	public Mob createMob(){
		return null;
	}
	
	protected void createMobs(){
		Mob mob = new Jellyfish();
		mob.pos = 1 + 7*width;
		mobs.add(mob);
		
		mob = new Jellyfish();
		mob.pos = 2 + 7*width;
		mobs.add(mob);
		
		mob = new Jellyfish();
		mob.pos = 3 + 7*width;
		mobs.add(mob);
		
		mob = new Jellyfish();
		mob.pos = 3 + 8*width;
		mobs.add(mob);
	}
	
	protected void createItems(){}
	
	public Actor respawner(){
		return null;
	}
}
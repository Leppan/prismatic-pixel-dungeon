package com.leppa.prismaticpixeldungeon.levels;

import android.util.Log;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.actors.mobs.Mob;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.Sheep;
import com.leppa.prismaticpixeldungeon.items.Generator;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfExperience;
import com.leppa.prismaticpixeldungeon.levels.puzzle.PressurePad;
import com.leppa.prismaticpixeldungeon.levels.puzzle.PuzzleGenerator;
import com.leppa.prismaticpixeldungeon.levels.puzzle.PuzzleRoom;
import com.leppa.prismaticpixeldungeon.levels.traps.DistortionTrap;
import com.leppa.prismaticpixeldungeon.levels.traps.FleecingTrap;
import com.leppa.prismaticpixeldungeon.levels.traps.Trap;
import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

public class PrisonPuzzleLevel extends PuzzleLevel{
	
	static Item reward = Generator.random(Generator.Category.SCROLL);
	PuzzleRoom room;
	PuzzleRoom room2;
	
	String stringMap = ""+
			"WWWWWWWWWWMWWWWWWWWWW" +
			"WWWWWWWWWSTSWWWWWWWWW" +
			"WWWWWWWWW E WWWWWWWWW" +
			"WWWWWWWWW   WWWWWWWWW" +
			"WWWWWWWMW   WMWWWWWWW" +
			"WWWWWW  WWDWW  WWWWWW" +
			"WWWWW           WWWWW" +
			"WWWWW           WWWWW" +
			"WWWWWW         WWWWWW" +
			"WWWWWW         WWWWWW" +
			"WWWWWW         WWWWWW" +
			"WWWWW           WWWWW" +
			"WWWWW           WWWWW" +
			"WWWWWW  WM MW  WWWWWW" +
			"WWWWWWWWWMLMWWWWWWWWW" +
			"WWWWWW  WM MW  WWWWWW" +
			"WWWWW           WWWWW" +
			"WWWWW      WW   WWWWW" +
			"WWWWWW     WW  WWWWWW" +
			"WWWWWW         WWWWWW" +
			"WWWWWW WW      WWWWWW" +
			"WWWWW  WW       WWWWW" +
			"WWWWW           WWWWW" +
			"WWWWWW  WWTWW  WWWWWW" +
			"WWWWWWWWMWLWMWWWWWWWW" +
			"WWWWWWWWS   SWWWWWWWW" +
			"WWWWWWWW  p  WWWWWWWW" +
			"WWWWWWWW     WWWWWWWW" +
			"WWWWWWWW     WWWWWWWW" +
			"WWWWWWWW  X  WWWWWWWW" +
			"WWWWWWWWS   SWWWWWWWW" +
			"WWWWWWWWWWWWWWWWWWWWW";
	
	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
		
		usesHardCodedPressurePads = false;
		viewDistance = 12;
	}
	
	public PrisonPuzzleLevel(){
		pressurePads = new SparseArray<>();
		MAP_START = HardcodedMapHelper.mapStringToMapIntArray(stringMap);
	}
	
	@Override
	protected void setup(){
		super.setup();
		
		room = PuzzleGenerator.generatePuzzleRoom(9, 7, 10, 21, 6, 6, 0, new int[0], this);
		room2 = PuzzleGenerator.generatePuzzleRoom(9, 7, 6, 21, 6, 16, 1, new int[0], this);
		int[] stageDoors = {0, 1};
		int[] padsPerStage = {room.getPressurePads().size(), room2.getPressurePads().size()};
		Trap[] traps = {new DistortionTrap().reveal(), new FleecingTrap().reveal()};
		this.stageDoors = stageDoors;
		this.padsPerStage = padsPerStage;
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
		for(Sheep sheep : room.getSheep()){
			mobs.add(sheep);
		}
		
		for(Sheep sheep : room2.getSheep()){
			mobs.add(sheep);
		}
		
		for(Mob m : mobs){
			if(map[m.pos] == Terrain.HIGH_GRASS){
				map[m.pos] = Terrain.GRASS;
				losBlocking[m.pos] = false;
			}
		}
	}
	
	public void create(){
		super.create();
		
		for(PressurePad p : room.getPressurePads()){
			MAP_START[p.pos] = P;
			setPressurePad(p, p.pos);
		}
		
		for(PressurePad p : room2.getPressurePads()){
			MAP_START[p.pos] = P;
			setPressurePad(p, p.pos);
		}
	}
	
	protected void createItems(){
		super.createItems();
		
		if(interResetData.get(0) == 0){
			drop(new PotionOfExperience(), 10 + 26 * width());
			drop(reward, 10 + 26 * width());
		}
	}
	
	public void collectItem(Item item){
		if(item == reward){
			interResetData.set(0, 1);
		}
	}
	
	public void setupInterResetData(){
		interResetData.add(0);
	}
}

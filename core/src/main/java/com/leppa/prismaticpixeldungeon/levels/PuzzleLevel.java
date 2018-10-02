package com.leppa.prismaticpixeldungeon.levels;

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
import com.leppa.prismaticpixeldungeon.levels.Level;
import com.leppa.prismaticpixeldungeon.levels.Terrain;
import com.leppa.prismaticpixeldungeon.levels.puzzle.PressurePad;
import com.leppa.prismaticpixeldungeon.levels.traps.DistortionTrap;
import com.leppa.prismaticpixeldungeon.levels.traps.FleecingTrap;
import com.leppa.prismaticpixeldungeon.levels.traps.Trap;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

public class PuzzleLevel extends Level{
	
	int[] pressurePadStages = {0, 1, 1, 2, 2, 2, 2, 3, 3, 4, 4};
	int[] pressurePadColours = {0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2};
	int[] stageDoors = {0, 1, 3, 4, 2};
	int[] padsPerStage = {1, 2, 4, 2, 2};
	Trap[] traps = {new DistortionTrap().reveal(), new DistortionTrap().reveal(), new DistortionTrap().reveal(), new FleecingTrap().reveal()};
	int trapCounter = 0;
	ArrayList<Integer> stageDoorsPos = new ArrayList<>();
	
	@Override
	protected boolean build(){
		setSize(21, 32);
		
		map = MAP_START.clone();
		
		pressurePads = new SparseArray<>();
		
		for(int f = 0; f < MAP_START.length; f++){
			if(MAP_START[f] == X) exit = f;
			if(MAP_START[f] == E) entrance = f;
			if(MAP_START[f] == P) setPressurePad(new PressurePad().setColour(pressurePadColours[pressurePads.size()]).setStage(pressurePadStages[pressurePads.size()]), f);
			if(MAP_START[f] == L) stageDoorsPos.add(f);
			if(MAP_START[f] == T){
				setTrap(traps[trapCounter], f);
				trapCounter++;
			}
		}
		
		return true;
	}
	
	public PressurePad setPressurePad(PressurePad pad, int pos){
		PressurePad existingPad = pressurePads.get(pos);
		if(existingPad != null){
			pressurePads.remove(pos);
		}
		pad.set(pos);
		pressurePads.put(pos, pad);
		GameScene.updateMap(pos);
		return pad;
	}
	
	public void press(int cell, Char ch, boolean hard){
		super.press(cell, ch, hard);
		boolean pressurePadsUpdated = false;
		for(int i = 0; i < pressurePads.size(); i++){
			PressurePad p = pressurePads.valueAt(i);
			boolean checked = false;
			for(Object a : mobs.toArray()){
				Mob mob = (Mob)a;
				if(mob.pos == p.pos && mob instanceof Sheep){
					if(!p.pressed && !p.finished){
						p.pressed = true;
						pressurePadsUpdated = true;
					}
					GameScene.updateMap(p.pos);
					checked = true;
				}
			}
			if(Dungeon.hero.pos == p.pos){
				if(!p.pressed && !p.finished){
					p.pressed = true;
					pressurePadsUpdated = true;
				}
				GameScene.updateMap(p.pos);
				checked = true;
			}
			if(!checked){
				if(p.pressed && !p.finished){
					p.pressed = false;
					pressurePadsUpdated = true;
				}
				GameScene.updateMap(p.pos);
			}
		}
		
		if(pressurePadsUpdated){
			SparseArray<Integer> pressesPerStage = new SparseArray<>();
			//Check how many pads are pressed per stage
			for(int i = 0; i < pressurePads.size(); i++){
				PressurePad p = pressurePads.valueAt(i);
				if(p.pressed){
					pressesPerStage.put(p.stage, pressesPerStage.get(p.stage) != null ? pressesPerStage.get(p.stage) + 1 : 1);
				}
			}
			for(int x = 0; x < padsPerStage.length; x++){
				if((pressesPerStage.get(x) != null ? pressesPerStage.get(x) : 0) >= padsPerStage[x]){
					//Change pressure pads to green
					for(int i = 0; i < pressurePads.size(); i++){
						PressurePad p = pressurePads.valueAt(i);
						if(p.stage == x){
							p.setColour(PressurePad.COMPLETE);
							p.finished = true;
							p.pressed = true;
							GameScene.updateMap(p.pos);
						}
					}
					//Unlock doors
					for(int i = 0; i < stageDoorsPos.size(); i++){
						if(stageDoors[i] == x){
							Level.set(stageDoorsPos.get(i), Terrain.DOOR);
							GameScene.updateMap(stageDoorsPos.get(i));
						}
					}
				}
			}
		}
	}
	
	@Override
	public Mob createMob(){
		return null;
	}
	
	@Override
	protected void createMobs(){
		int[] sheepPositions = {10 + 11 * width, 4 + 15 * width, 10 + 15 * width, 16 + 15 * width, 7 + 19 * width, 13 + 19 * width, 10 + 19 * width};
		Sheep[] puzzleSheep = {new HorizontalPuzzleSheep(), new HorizontalPuzzleSheep(), new HorizontalPuzzleSheep(), new HorizontalPuzzleSheep(), new AnyPushablePuzzleSheep(), new AnyPushablePuzzleSheep(), new VerticalPuzzleSheep()};
		
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
	
	@Override
	protected void createItems(){
		Item item = Bones.get();
		if(item != null){
			drop(item, randomRespawnCell()).type = Heap.Type.REMAINS;
		}
	}
	
	protected static final int W = Terrain.WALL;
	protected static final int D = Terrain.DOOR;
	protected static final int L = Terrain.LOCKED_DOOR;
	protected static final int e = Terrain.EMPTY;
	
	protected static final int P = Terrain.PRESSUREPAD;
	
	protected static final int E = Terrain.ENTRANCE;
	protected static final int X = Terrain.EXIT;
	
	protected static final int M = Terrain.WALL_DECO;
	protected static final int S = Terrain.STATUE;
	protected static final int T = Terrain.TRAP;
	
	//TODO if I ever need to store more static maps I should externalize them instead of hard-coding
	//Especially as I means I won't be limited to legal identifiers
	protected int[] MAP_START =
			{W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, S, e, S, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, E, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, M, D, M, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, e, e, T, W, W, W, W, W, W, W, W, W,
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

package com.leppa.prismaticpixeldungeon.levels.puzzle;

import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.Sheep;
import com.leppa.prismaticpixeldungeon.levels.traps.Trap;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

public class PuzzleRoom{
	
	ArrayList<Sheep> sheep = new ArrayList<>();
	ArrayList<PressurePad> pressurePads = new ArrayList<>();
	
	public void setSheep(ArrayList<Sheep> sheep){
		this.sheep = sheep;
	}
	
	public void setPressurePads(ArrayList<PressurePad> pressurePads){
		this.pressurePads = pressurePads;
	}
	
	public ArrayList<Sheep> getSheep(){
		return sheep;
	}
	
	public ArrayList<PressurePad> getPressurePads(){
		return pressurePads;
	}
	
	public SparseArray<PressurePad> getPressurePadsAsSparseArray(){
		SparseArray<PressurePad> p = new SparseArray<>();
		for(PressurePad pad : pressurePads){
			p.put(pad.pos, pad);
		}
		return p;
	}
}
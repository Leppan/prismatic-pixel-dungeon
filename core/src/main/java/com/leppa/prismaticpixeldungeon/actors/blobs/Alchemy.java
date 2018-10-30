/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.leppa.prismaticpixeldungeon.actors.blobs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.effects.BlobEmitter;
import com.leppa.prismaticpixeldungeon.effects.Speck;
import com.leppa.prismaticpixeldungeon.journal.Notes;
import com.leppa.prismaticpixeldungeon.scenes.AlchemyScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Alchemy extends Blob implements AlchemyScene.AlchemyProvider {

	protected int pos;
	
	@Override
	protected void evolve() {
		int cell;
		for (int i=area.top-1; i <= area.bottom; i++) {
			for (int j = area.left-1; j <= area.right; j++) {
				cell = j + i* Dungeon.level.width();
				if (Dungeon.level.insideMap(cell)) {
					off[cell] = cur[cell];
					volume += off[cell];
					if (off[cell] > 0 && Dungeon.level.heroFOV[cell]){
						Notes.add( Notes.Landmark.ALCHEMY );
					}
					
					//for pre-0.6.2 saves
					while (off[cell] > 0 && Dungeon.level.heaps.get(cell) != null){
						
						int n;
						do {
							n = cell + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
						} while (!Dungeon.level.passable[n]);
						Dungeon.level.drop( Dungeon.level.heaps.get(cell).pickUp(), n ).sprite.drop( pos );
					}
				}
			}
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.BUBBLE ), 0.33f, 0 );
	}
	
	public static int alchPos;
	
	//1 volume is kept in reserve
	
	@Override
	public int getEnergy() {
		return Math.max(0, cur[alchPos] - 1);
	}
	
	@Override
	public void spendEnergy(int reduction) {
		cur[alchPos] = Math.max(1, cur[alchPos] - reduction);
	}
}
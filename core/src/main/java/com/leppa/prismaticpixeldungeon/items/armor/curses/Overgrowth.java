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

package com.leppa.prismaticpixeldungeon.items.armor.curses;

import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.effects.CellEmitter;
import com.leppa.prismaticpixeldungeon.effects.particles.LeafParticle;
import com.leppa.prismaticpixeldungeon.items.Generator;
import com.leppa.prismaticpixeldungeon.items.armor.Armor;
import com.leppa.prismaticpixeldungeon.plants.BlandfruitBush;
import com.leppa.prismaticpixeldungeon.plants.Plant;
import com.leppa.prismaticpixeldungeon.plants.Starflower;
import com.leppa.prismaticpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Overgrowth extends Armor.Glyph {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		
		if ( Random.Int( 20 ) == 0) {
			
			Plant.Seed s;
			do{
				s = (Plant.Seed) Generator.random(Generator.Category.SEED);
			} while (s instanceof BlandfruitBush.Seed || s instanceof Starflower.Seed);
			
			Plant p = s.couch(defender.pos, null);
			
			p.activate();
			CellEmitter.get( defender.pos ).burst( LeafParticle.LEVEL_SPECIFIC, 10 );
			
		}
		
		return damage;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}
	
	@Override
	public boolean curse() {
		return true;
	}
}

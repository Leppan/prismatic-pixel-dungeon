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

package com.leppa.prismaticpixeldungeon.items.armor.glyphs;

import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Charm;
import com.leppa.prismaticpixeldungeon.effects.Speck;
import com.leppa.prismaticpixeldungeon.items.armor.Armor;
import com.leppa.prismaticpixeldungeon.items.armor.Armor.Glyph;
import com.leppa.prismaticpixeldungeon.sprites.ItemSprite;
import com.leppa.prismaticpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Affection extends Glyph {
	
	private static ItemSprite.Glowing PINK = new ItemSprite.Glowing( 0xFF4488 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level());
		
		if (Random.Int( level / 2 + 10 ) >= 9) {
			
			int duration = Random.IntRange( 4, 12 );

			Buff.affect( attacker, Charm.class, duration ).object = defender.id();
			attacker.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );

		}
		
		return damage;
	}

	@Override
	public Glowing glowing() {
		return PINK;
	}
}

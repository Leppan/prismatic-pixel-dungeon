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

package com.leppa.prismaticpixeldungeon.items.weapon.missiles.darts;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Chill;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;

public class ChillingDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.CHILLING_DART;
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		
		if (Dungeon.level.water[defender.pos]){
			Buff.prolong(defender, Chill.class, 10f);
		} else {
			Buff.prolong(defender, Chill.class, 6f);
		}
		
		return super.proc(attacker, defender, damage);
	}
}

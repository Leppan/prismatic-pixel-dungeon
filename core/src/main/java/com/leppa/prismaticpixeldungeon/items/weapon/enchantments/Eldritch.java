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

package com.leppa.prismaticpixeldungeon.items.weapon.enchantments;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Terror;
import com.leppa.prismaticpixeldungeon.actors.buffs.Vertigo;
import com.leppa.prismaticpixeldungeon.items.weapon.Weapon;
import com.leppa.prismaticpixeldungeon.sprites.ItemSprite;
import com.leppa.prismaticpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Eldritch extends Weapon.Enchantment{
	
	private static ItemSprite.Glowing GREY = new ItemSprite.Glowing(0x222222);
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage){
		// lvl 0 - 20%
		// lvl 1 - 33%
		// lvl 2 - 43%
		int level = Math.max(0, weapon.level());
		
		if (Random.Int( level + 5 ) >= 4) {

			if (defender == Dungeon.hero) {
				Buff.affect( defender, Vertigo.class, Vertigo.DURATION );
			} else {
				//damage will reduce by 5 turns, so effectively 10 turns of terror
				Buff.affect( defender, Terror.class, 10f + 5f ).object = attacker.id();
			}
			
		}
		
		return damage;
	}
	
	public int procGuaranteed(Weapon weapon, Char attacker, Char defender, int damage){
		int level = Math.max(0, weapon.level());
		
		if(defender == Dungeon.hero){
			Buff.affect(defender, Vertigo.class, Vertigo.DURATION);
		}else{
			Buff.affect(defender, Terror.class, Terror.DURATION).object = attacker.id();
		}
		
		return damage;
	}
	
	@Override
	public Glowing glowing(){
		return GREY;
	}
}

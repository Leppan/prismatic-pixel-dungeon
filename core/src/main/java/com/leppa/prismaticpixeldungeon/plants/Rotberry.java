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

package com.leppa.prismaticpixeldungeon.plants;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.effects.CellEmitter;
import com.leppa.prismaticpixeldungeon.effects.particles.LeafParticle;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;

public class Rotberry extends Plant {

	{
		image = 0;
	}

	@Override
	public void activate() {
		Dungeon.level.drop( new Seed(), pos ).sprite.drop();
	}
	
	@Override
	public void wither() {
		Dungeon.level.uproot( pos );
		
		if (Dungeon.level.heroFOV[pos]) {
			CellEmitter.get( pos ).burst( LeafParticle.GENERAL, 6 );
		}
		
		//no warden benefit
	}

	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_ROTBERRY;

			plantClass = Rotberry.class;
		}
		
		@Override
		public int price() {
			return 30 * quantity;
		}
	}
}

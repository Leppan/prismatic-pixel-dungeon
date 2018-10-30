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

package com.leppa.prismaticpixeldungeon.items.stones;

import com.leppa.prismaticpixeldungeon.items.EquipableItem;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.items.wands.Wand;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.leppa.prismaticpixeldungeon.windows.WndBag;

public class StoneOfDetectCurse extends InventoryStone {
	
	{
		mode = WndBag.Mode.CURSE_DETECTABLE;
		image = ItemSpriteSheet.STONE_CURSE;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		item.cursedKnown = true;
		useAnimation();
		
		if (item.cursed){
			GLog.w( Messages.get(this, "cursed") );
		} else {
			GLog.w( Messages.get(this, "not_cursed") );
		}
	}
	
	public static boolean canDetectCurse(Item item){
		return !item.isIdentified()
				&& !item.cursedKnown
				&& (item instanceof EquipableItem || item instanceof Wand);
	}
}

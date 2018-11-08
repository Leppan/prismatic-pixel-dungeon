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

package com.leppa.prismaticpixeldungeon.items.spells;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.items.MerchantsBeacon;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.leppa.prismaticpixeldungeon.windows.WndBag;

import java.util.ArrayList;

public class MagicalPorter extends InventorySpell {
	
	{
		image = ItemSpriteSheet.MAGIC_PORTER;
		mode = WndBag.Mode.NOT_EQUIPPED;
	}
	
	@Override
	protected void onCast(Hero hero) {
		if (Dungeon.depth >= 25){
			GLog.w(Messages.get(this, "nowhere"));
		} else {
			super.onCast(hero);
		}
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		Item result = item.detachAll(curUser.belongings.backpack);
		int portDepth = 5 * (1 + Dungeon.depth/6);
		ArrayList<Item> ported = Dungeon.portedItems.get(portDepth);
		if (ported == null) {
			Dungeon.portedItems.put(portDepth, ported = new ArrayList<>());
		}
		ported.add(result);
		
	}
	
	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((30 + 5) / 8f));
	}
	
	public static class Recipe extends com.leppa.prismaticpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfIdentify.class, MerchantsBeacon.class};
			inQuantity = new int[]{1, 1};
			
			cost = 4;
			
			output = MagicalPorter.class;
			outQuantity = 8;
		}
		
	}
}

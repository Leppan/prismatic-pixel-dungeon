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

import com.leppa.prismaticpixeldungeon.ShatteredPixelDungeon;
import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.scenes.AlchemyScene;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.utils.GLog;

public class Alchemize extends Spell implements AlchemyScene.AlchemyProvider {
	
	{
		image = ItemSpriteSheet.ALCHEMIZE;
	}
	
	@Override
	protected void onCast(Hero hero) {
		if (hero.visibleEnemies() > hero.mindVisionEnemies.size()) {
			GLog.i( Messages.get(this, "enemy_near") );
			return;
		}
		detach( curUser.belongings.backpack );
		updateQuickslot();
		AlchemyScene.setProvider(this);
		ShatteredPixelDungeon.switchScene(AlchemyScene.class);
	}
	
	@Override
	public int getEnergy() {
		return 0;
	}
	
	@Override
	public void spendEnergy(int reduction) {
		//do nothing
	}
	
	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((30 + 30) / 4f));
	}
	
	public static class Recipe extends com.leppa.prismaticpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfRecharging.class, PotionOfLiquidFlame.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = Alchemize.class;
			outQuantity = 4;
		}
		
	}
}
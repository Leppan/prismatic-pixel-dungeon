package com.leppa.prismaticpixeldungeon.items.food;

import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.items.Recipe;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfParalyticGas;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfToxicGas;
import com.leppa.prismaticpixeldungeon.items.potions.exotic.PotionOfShroudingFog;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;

public class ColourGel extends Food{
	
	{
		image = ItemSpriteSheet.GEL_BLUE;
		energy = 200;
	}
	
	protected void satisfy(Hero hero) {
		super.satisfy(hero);
		effect(hero);
	}
	
	public int price() {
		return 20 * quantity;
	}
	
	public void effect(Hero hero){}
	
	public static class BlueGelRecipe extends Recipe.SimpleRecipe{
		{
			inputs =  new Class[]{BlueGel.class};
			inQuantity = new int[]{1};
			
			cost = 1;
			
			output = PotionOfParalyticGas.class;
			outQuantity = 1;
		}
	}
	
	public static class GreenGelRecipe extends Recipe.SimpleRecipe{
		{
			inputs =  new Class[]{GreenGel.class};
			inQuantity = new int[]{1};
			
			cost = 1;
			
			output = PotionOfShroudingFog.class;
			outQuantity = 1;
		}
	}
	
	public static class PurpleGelRecipe extends Recipe.SimpleRecipe{
		{
			inputs =  new Class[]{PurpleGel.class};
			inQuantity = new int[]{1};
			
			cost = 1;
			
			output = PotionOfToxicGas.class;
			outQuantity = 1;
		}
	}
}

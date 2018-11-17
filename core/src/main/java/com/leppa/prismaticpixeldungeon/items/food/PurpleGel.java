package com.leppa.prismaticpixeldungeon.items.food;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.buffs.Blindness;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Poison;
import com.leppa.prismaticpixeldungeon.actors.buffs.Recharging;
import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class PurpleGel extends ColourGel{
	
	{
		image = ItemSpriteSheet.GEL_PURPLE;
	}
	
	public void effect(Hero hero){
		Buff.affect(hero, Recharging.class, 16f);
		ScrollOfRecharging.charge(hero);
		
		Buff.affect(hero, Poison.class).set((int)(Dungeon.depth*0.5));
	}
}

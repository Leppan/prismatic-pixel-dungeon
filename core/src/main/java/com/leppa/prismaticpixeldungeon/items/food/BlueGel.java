package com.leppa.prismaticpixeldungeon.items.food;

import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Paralysis;
import com.leppa.prismaticpixeldungeon.actors.buffs.Recharging;
import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class BlueGel extends ColourGel{
	
	{
		image = ItemSpriteSheet.GEL_BLUE;
	}
	
	public void effect(Hero hero){
		Buff.affect(hero, Recharging.class, 16f);
		ScrollOfRecharging.charge(hero);
		
		Buff.prolong(hero, Paralysis.class, Random.Int(5, 10));
	}
}

package com.leppa.prismaticpixeldungeon.sprites;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.mobs.ElementalImage;

public class ElementalImageSprite extends StatueSprite{
	
	public ElementalImageSprite(){
		super();
		if(ElementalImage.instances.get(Dungeon.depth) != null) tint(ElementalImage.instances.get(Dungeon.depth).enchantment.glowing().color, 0.2f);
	}
	
	@Override
	public void resetColor(){
		super.resetColor();
		if(ElementalImage.instances.get(Dungeon.depth) != null) tint(ElementalImage.instances.get(Dungeon.depth).enchantment.glowing().color, 0.2f);
	}
}
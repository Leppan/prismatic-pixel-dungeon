package com.leppa.prismaticpixeldungeon.items.scrolls;

import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.windows.WndOptions;
import com.leppa.prismaticpixeldungeon.windows.WndSendDepth;

import java.util.ArrayList;

public class ScrollOfDebug extends Item{
	
	{
		image = ItemSpriteSheet.SCROLL_DEBUG;
		stackable = true;
		defaultAction = AC_READ;
	}
	
	public static final String AC_READ = "READ";
	
	public ArrayList<String> actions(Hero hero){
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_READ);
		return actions;
	}
	
	public void execute(Hero hero, String action){
		super.execute(hero, action);
		if(action.equals(AC_READ)){
			GameScene.show(
					new WndOptions(Messages.get(this, "name"),
							Messages.get(this, "use_prompt"),
							Messages.get(this, "get_item"),
							Messages.get(this, "send_to_level")){
						protected void onSelect(int index){
							if(index == 1) GameScene.show(new WndSendDepth());
						}
					}
			);
		}
	}
	
	public boolean isUpgradable(){
		return false;
	}
}
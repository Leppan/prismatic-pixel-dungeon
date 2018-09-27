package com.leppa.prismaticpixeldungeon.levels.traps;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.Sheep;
import com.leppa.prismaticpixeldungeon.items.EquipableItem;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.levels.Level;
import com.leppa.prismaticpixeldungeon.levels.Terrain;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

import static com.leppa.prismaticpixeldungeon.Dungeon.hero;

public class FleecingTrap extends Trap{
	
	public static boolean removeArmour = true;
	
	{
		color = VIOLET;
		shape = WAVES;
	}
	
	@Override
	public void trigger() {
		if (Dungeon.level.heroFOV[pos]){
			Sample.INSTANCE.play(Assets.SND_TRAP);
		}
		reveal();
		if(!(Actor.findChar(pos) instanceof Hero) && !(Actor.findChar(pos) instanceof Sheep)) {
			Level.set(pos, Terrain.TRAP);
		}else{
			disarm();
			activate();
		}
		GameScene.updateMap(pos);
	}
	
	@Override
	public void activate(){
		Char target = Actor.findChar(pos);
		
		if(target instanceof Sheep){
			//Kill the (poor) sheep
			target.damage(target.HT, this);
			Level.set(pos, Terrain.EMBERS);
			GameScene.updateMap(pos);
			return;
		}else if(target instanceof Hero){
			//Move their armour, or destroy it if it's cursed
			if(removeArmour && hero.belongings.armor != null){
				removeArmour = false;
				if(!hero.belongings.armor.cursed){
					EquipableItem i = hero.belongings.armor;
					i.doUnequip(hero, false, true);
					Dungeon.level.drop(i.detachAll(hero.belongings.backpack), Dungeon.level.exit).sprite.drop(Dungeon.level.exit);
					GLog.w( Messages.get(this, "movearmour"));
				}else{
					hero.belongings.armor.detachAll(hero.belongings.backpack);
					GLog.w( Messages.get(this, "destroyarmour"));
				}
			}
			
		}
	}
}
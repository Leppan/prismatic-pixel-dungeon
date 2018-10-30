package com.leppa.prismaticpixeldungeon.items.stones;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Blindness;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Charm;
import com.leppa.prismaticpixeldungeon.actors.buffs.Chill;
import com.leppa.prismaticpixeldungeon.actors.buffs.Cripple;
import com.leppa.prismaticpixeldungeon.actors.buffs.FlavourBuff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Roots;
import com.leppa.prismaticpixeldungeon.actors.buffs.Slow;
import com.leppa.prismaticpixeldungeon.actors.buffs.Terror;
import com.leppa.prismaticpixeldungeon.actors.buffs.Weakness;
import com.leppa.prismaticpixeldungeon.effects.CellEmitter;
import com.leppa.prismaticpixeldungeon.effects.Speck;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StoneOfDespair extends Runestone{
	
	{
		image = ItemSpriteSheet.STONE_DISPAIR;
	}
	
	public static ArrayList<Class<? extends FlavourBuff>> debuffs = new ArrayList<>();
	
	static{
		debuffs.add(Charm.class);
		debuffs.add(Weakness.class);
		debuffs.add(Cripple.class);
		debuffs.add(Blindness.class);
		debuffs.add(Terror.class);
		debuffs.add(Roots.class);
		debuffs.add(Slow.class);
		debuffs.add(Chill.class);
	}
	
	@Override
	protected void activate(int cell){
		for (int i : PathFinder.NEIGHBOURS9){
			Char ch = Actor.findChar( cell + i );
			
			CellEmitter.center(cell + i).start(Speck.factory(Speck.STEAM), 0.2f, 5);
			
			if (ch != null && ch.alignment == Char.Alignment.ENEMY){
				Class<? extends FlavourBuff> debuff = debuffs.get(Random.Int(debuffs.size()));
				if(debuff == Charm.class) Buff.prolong(ch, Charm.class, 10f).object = curUser.id();
				else Buff.prolong(ch, debuff, 10f);
			}
		}
		
		Sample.INSTANCE.play(Assets.SND_CURSED);
	}
}

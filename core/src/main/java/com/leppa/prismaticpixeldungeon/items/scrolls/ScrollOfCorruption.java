package com.leppa.prismaticpixeldungeon.items.scrolls;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.Badges;
import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.Statistics;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Corruption;
import com.leppa.prismaticpixeldungeon.actors.buffs.Doom;
import com.leppa.prismaticpixeldungeon.actors.buffs.Drowsy;
import com.leppa.prismaticpixeldungeon.actors.buffs.FlavourBuff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Invisibility;
import com.leppa.prismaticpixeldungeon.actors.buffs.PinCushion;
import com.leppa.prismaticpixeldungeon.actors.buffs.SoulMark;
import com.leppa.prismaticpixeldungeon.actors.mobs.Mob;
import com.leppa.prismaticpixeldungeon.effects.MagicMissile;
import com.leppa.prismaticpixeldungeon.effects.Speck;
import com.leppa.prismaticpixeldungeon.items.armor.glyphs.Stone;
import com.leppa.prismaticpixeldungeon.items.artifacts.LloydsBeacon;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfDespair;
import com.leppa.prismaticpixeldungeon.items.wands.WandOfCorruption;
import com.leppa.prismaticpixeldungeon.mechanics.Ballistica;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.scenes.CellSelector;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.leppa.prismaticpixeldungeon.sprites.CharSprite;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ScrollOfCorruption extends Scroll{
	
	{
		initials = 12;
		bones = true;
		usesTargeting = true;
	}
	
	@Override
	public void doRead(){
		setKnown();
		
		Invisibility.dispel();
		
		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if(Dungeon.level.heroFOV[mob.pos]){
				Buff.affect(mob, StoneOfDespair.debuffs.get(Random.Int(StoneOfDespair.debuffs.size())));
				mob.sprite.centerEmitter().start(Speck.factory(Speck.STEAM), 0.1f, 6);
			}
		}
		
		GameScene.selectCell(zapper);
	}
	
	@Override
	public void empoweredRead(){
		doRead();
	}
	
	protected CellSelector.Listener zapper = new CellSelector.Listener(){
		
		@Override
		public void onSelect(Integer cell){
			Invisibility.dispel();
			if(cell != null && Actor.findChar(cell) != curUser){
				final Ballistica bolt = new Ballistica(curUser.pos, cell, Ballistica.MAGIC_BOLT);
				final Char ch = Actor.findChar(bolt.collisionPos);
				
				MagicMissile.boltFromChar(curUser.sprite.parent,
						MagicMissile.SHADOW,
						curUser.sprite,
						bolt.collisionPos,
						new Callback(){
							public void call(){
								if(ch != null && ch != curUser){
									Mob enemy = (Mob)ch;
									if(!enemy.isImmune(Corruption.class) && ch.buff(Corruption.class) == null){
										enemy.HP = enemy.HT;
										for(Buff buff : enemy.buffs()){
											if(buff.type == Buff.buffType.NEGATIVE
													&& !(buff instanceof SoulMark)){
												buff.detach();
											}else if(buff instanceof PinCushion){
												buff.detach();
											}
										}
										Buff.affect(enemy, Corruption.class);
										
										Statistics.enemiesSlain++;
										Badges.validateMonstersSlain();
										Statistics.qualifiedForNoKilling = false;
										if(enemy.EXP > 0 && curUser.lvl <= enemy.maxLvl){
											curUser.sprite.showStatus(CharSprite.POSITIVE, Messages.get(enemy, "exp", enemy.EXP));
											curUser.earnExp(enemy.EXP);
										}
										enemy.rollToDropLoot();
										
										readAnimation();
									}else if(ch.buff(Doom.class) == null && ch.buff(Corruption.class) == null){
										Buff.affect(enemy, Doom.class);
										readAnimation();
									}else{
										GLog.w(Messages.get(ScrollOfCorruption.class, "already_corrupted"));
										readAnimation();
										new ScrollOfCorruption().collect();
										return;
									}
								}else if(ch == null){
									readAnimation();
									new ScrollOfCorruption().collect();
									return;
								}
							}
						});
				Sample.INSTANCE.play(Assets.SND_ZAP);
			}else if(cell == null){
				readAnimation();
				new ScrollOfCorruption().collect();
				return;
			}else if(Actor.findChar(cell) == curUser){
				GLog.w(Messages.get(ScrollOfCorruption.class, "self_target"));
				readAnimation();
				new ScrollOfCorruption().collect();
				return;
			}
		}
		
		@Override
		public String prompt(){
			return Messages.get(ScrollOfCorruption.class, "prompt");
		}
	};
	
	@Override
	public int price(){
		return isKnown() ? 55 * quantity : super.price();
	}
}

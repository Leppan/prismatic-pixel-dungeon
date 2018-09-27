package com.leppa.prismaticpixeldungeon.items.artifacts;

import com.leppa.prismaticpixeldungeon.Badges;
import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.LockedFloor;
import com.leppa.prismaticpixeldungeon.actors.buffs.PinCushion;
import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.effects.CellEmitter;
import com.leppa.prismaticpixeldungeon.effects.particles.BlastParticle;
import com.leppa.prismaticpixeldungeon.effects.particles.RainbowParticle;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.items.weapon.Weapon;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Blazing;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Chilling;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Dazzling;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Eldritch;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Grim;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Lucky;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Shocking;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Stunning;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Unstable;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Vampiric;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Venomous;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Vorpal;
import com.leppa.prismaticpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.sprites.ItemSprite;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

import static com.leppa.prismaticpixeldungeon.Dungeon.hero;

//13 effective enchantments, starts with three
public class DeckOfElements extends Artifact{
	
	{
		image = ItemSpriteSheet.ARTIFACT_DECK;
		
		levelCap = 10;
		partialCharge = 0;
		charge = 100;
		chargeCap = 100;
		
		defaultAction = AC_USE;
		usesTargeting = true;
	}
	
	private static final String CURRENT        = "current";
	private static final String USABLEENCHANTS = "usableEnchants";
	
	public static final String AC_USE = "USE";
	public static ArrayList<Class> usableEnchants = new ArrayList<>();
	public Weapon.Enchantment current;
	public static int curMinCardDamage = 0;
	public static int curMaxCardDamage = 0;
	
	public DeckOfElements(){
		if(usableEnchants.size() == 0){
			for(int i = 0; i < 3; i++){
				Weapon.Enchantment e = Weapon.Enchantment.random();
				while(usableEnchants.contains(e.getClass())){
					e = Weapon.Enchantment.random();
				}
				usableEnchants.add(e.getClass());
			}
		}
		try{
			current = (Weapon.Enchantment)Random.element(usableEnchants).newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put(CURRENT, current);
		bundle.put(USABLEENCHANTS, usableEnchants.toArray(new Class[usableEnchants.size()]));
	}
	
	public void restoreFromBundle(Bundle bundle){
		super.restoreFromBundle(bundle);
		current = (Weapon.Enchantment)bundle.get(CURRENT);
		usableEnchants.clear();
		Collections.addAll(usableEnchants, bundle.getClassArray(USABLEENCHANTS));
	}
	
	@Override
	public ArrayList<String> actions(Hero hero){
		ArrayList<String> actions = super.actions(hero);
		if(isEquipped(hero) && charge > 0 && !cursed) actions.add(AC_USE);
		return actions;
	}
	
	public void execute(Hero hero, String action){
		super.execute(hero, action);
		if(action.equals(AC_USE)){
			if(!isEquipped(hero)) GLog.i(Messages.get(Artifact.class, "need_to_equip"));
			else if(cursed) GLog.i(Messages.get(this, "cursed"));
			else{
				EnchantedCard card = new EnchantedCard();
				card.enchant(current);
				curMinCardDamage = hero.belongings.weapon.min() / 2;
				curMaxCardDamage = hero.belongings.weapon.max() / 2;
				if(Random.Int(100) > charge){
					hero.shoot(hero, card);
					hero.spendAndNext(card.castDelay(hero, 0));
				}else{
					charge -= 10;
					card.execute(curUser);
				}
				if(charge < 10) charge = 10;
				try{
					current = (Weapon.Enchantment)Random.element(usableEnchants).newInstance();
				}catch(Exception e){
					e.printStackTrace();
				}
				updateQuickslot();
			}
		}
	}
	
	public ItemSprite.Glowing glowing(){
		return current.glowing();
	}
	
	public Item upgrade(){
		while(usableEnchants.size() < level() + 4 && level() < 10){
			Weapon.Enchantment e = Weapon.Enchantment.random();
			while(usableEnchants.contains(e.getClass())){
				e = Weapon.Enchantment.random();
			}
			usableEnchants.add(e.getClass());
		}
		return super.upgrade();
	}
	
	protected ArtifactBuff passiveBuff(){
		return new DeckRecharge();
	}
	
	public String desc(){
		String desc = super.desc();
		desc += " " + Messages.get(this, "desc_damage", (int)hero.belongings.weapon.min() / 2, (int)hero.belongings.weapon.max() / 2);
		desc += "\n\n" + Messages.get(this, "desc_current", current.name(Messages.get(this, "card")));
		if(this.level() < levelCap) desc += "\n\n" + Messages.get(this, "desc_missing", 13 - usableEnchants.size());
		return desc;
	}
	
	public class DeckRecharge extends ArtifactBuff{
		@Override
		public boolean act(){
			spend(TICK);
			LockedFloor lock = target.buff(LockedFloor.class);
			if(charge < chargeCap && !cursed && (lock == null || lock.regenOn())){
				partialCharge += 0.5;
				if(partialCharge > 1 && charge < chargeCap){
					partialCharge--;
					charge++;
					updateQuickslot();
				}
				if(charge >= chargeCap){
					partialCharge = 0;
				}
			}
			return true;
		}
	}
	
	public class EnchantedCard extends MissileWeapon{
		
		public EnchantedCard(){
			super();
		}
		
		{
			image = ItemSpriteSheet.DECK_CARD;
		}
		
		@Override
		public int STRReq(int lvl){
			return 9;
		}
		
		@Override
		public int min(int lvl){
			return DeckOfElements.curMinCardDamage;
		}
		
		@Override
		public int max(int lvl){
			return DeckOfElements.curMaxCardDamage;
		}
		
		protected void rangedMiss(int cell){
			parent = null;
		}
		
		public int proc(Char attacker, Char defender, int damage){
			enchantment.procGuaranteed(this, attacker, defender, damage);
			
			//Run part of super.proc, since we don't want twice-activating enchantments
			if(!levelKnown){
				if(--hitsToKnow <= 0){
					identify();
					GLog.i(Messages.get(Weapon.class, "identify"));
					Badges.validateItemLevelAquired(this);
				}
			}
			
			return damage;
		}
		
		protected void rangedHit(Char enemy, int cell){
			//Nothing needs to happen here, cards are unbreakable and don't drop
		}
		
		//Don't drop
		protected void onThrow(int cell){
			Char enemy = Actor.findChar(cell);
			if(enemy == null || enemy == curUser){
				parent = null;
				CellEmitter.center(cell).burst(RainbowParticle.BURST, 30);
				Dungeon.level.press(cell, null, true);
			}else{
				if(!curUser.shoot(enemy, this)){
					rangedMiss(cell);
				}else{
					rangedHit(enemy, cell);
				}
			}
		}
	}
}
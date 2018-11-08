package com.leppa.prismaticpixeldungeon.actors.mobs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.ShatteredPixelDungeon;
import com.leppa.prismaticpixeldungeon.items.Generator;
import com.leppa.prismaticpixeldungeon.items.rings.Ring;
import com.leppa.prismaticpixeldungeon.items.rings.RingOfAccuracy;
import com.leppa.prismaticpixeldungeon.items.rings.RingOfEvasion;
import com.leppa.prismaticpixeldungeon.items.rings.RingOfFuror;
import com.leppa.prismaticpixeldungeon.items.rings.RingOfHaste;
import com.leppa.prismaticpixeldungeon.items.rings.RingOfTenacity;
import com.leppa.prismaticpixeldungeon.items.weapon.Weapon;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Grim;
import com.leppa.prismaticpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class PuzzleStatue extends WeaponMob{
	
	private static final String RING = "ring";
	
	Ring ring;
	public Class<? extends Ring>[] rings = new Class[]{
			RingOfHaste.class,
			RingOfTenacity.class,
			RingOfFuror.class,
			RingOfEvasion.class,
			RingOfAccuracy.class
	};
	
	{
		spriteClass = StatueSprite.class;
		
		EXP = 12;
		maxLvl = 16;
		state = PASSIVE;
		
		properties.add(Property.INORGANIC);
		
		resistances.add(Grim.class);
	}
	
	public PuzzleStatue(){
		super();
		
		HP = HT = 130;
		defenseSkill = 30;
		
		try{
			ring = Random.element(rings).newInstance();
			ring.level(2);
			ring.identify();
		}catch(Exception e){
			ShatteredPixelDungeon.reportException(e);
		}
		
		do{
			weapon = (MeleeWeapon)Generator.random(Generator.Category.WEAPON);
		}while(weapon.cursed);
		weapon.enchant(Weapon.Enchantment.random());
		
		ring.activate(this);
	}
	
	@Override
	public int selfAttackSkill(){
		return 11 + Dungeon.depth;
	}
	
	@Override
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put(RING, ring);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle){
		super.restoreFromBundle(bundle);
		ring = (Ring)bundle.get(RING);
	}
	
	public void die(Object cause){
		ring.identify();
		ring.degrade();
		Dungeon.level.drop(ring, pos).sprite.drop();
		super.die(cause);
	}
	
	@Override
	public void damage(int dmg, Object src){
		
		if(state == PASSIVE){
			state = HUNTING;
		}
		
		super.damage(dmg, src);
	}
	
	@Override
	public void beckon(int cell){
		// Do nothing
	}
	
	public String description(){
		return Messages.get(this, "desc", weapon.name(), ring.name());
	}
}
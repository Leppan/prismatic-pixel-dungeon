package com.leppa.prismaticpixeldungeon.actors.mobs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.items.weapon.Weapon;
import com.leppa.prismaticpixeldungeon.items.weapon.melee.WornShortsword;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class WeaponMob extends Mob{
	
	protected Weapon weapon = new WornShortsword();
	private static final String WEAPON = "weapon";
	
	public abstract int selfAttackSkill();
	
	@Override
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put(WEAPON, weapon);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle){
		super.restoreFromBundle(bundle);
		weapon = (Weapon)bundle.get(WEAPON);
	}
	
	@Override
	public int damageRoll(){
		return weapon.damageRoll(this);
	}
	
	@Override
	public int attackSkill(Char target){
		return (int)((/*9 + Dungeon.depth*/ selfAttackSkill()) * weapon.accuracyFactor(this));
	}
	
	@Override
	protected float attackDelay(){
		return super.attackDelay() * weapon.speedFactor(this);
	}
	
	@Override
	protected boolean canAttack(Char enemy){
		return Dungeon.level.distance(pos, enemy.pos) <= weapon.reachFactor(this);
	}
	
	@Override
	public int drRoll(){
		return Random.NormalIntRange(0, Dungeon.depth + weapon.defenseFactor(this));
	}
	
	@Override
	public int attackProc(Char enemy, int damage){
		damage = super.attackProc(enemy, damage);
		return weapon.proc(this, enemy, damage);
	}
}
package com.leppa.prismaticpixeldungeon.actors.mobs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Blindness;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Burning;
import com.leppa.prismaticpixeldungeon.actors.buffs.Paralysis;
import com.leppa.prismaticpixeldungeon.actors.buffs.Poison;
import com.leppa.prismaticpixeldungeon.actors.buffs.Vertigo;
import com.leppa.prismaticpixeldungeon.effects.particles.SparkParticle;
import com.leppa.prismaticpixeldungeon.items.food.BlueGel;
import com.leppa.prismaticpixeldungeon.items.food.GreenGel;
import com.leppa.prismaticpixeldungeon.items.food.MysteryMeat;
import com.leppa.prismaticpixeldungeon.items.food.PurpleGel;
import com.leppa.prismaticpixeldungeon.levels.RegularLevel;
import com.leppa.prismaticpixeldungeon.levels.rooms.Room;
import com.leppa.prismaticpixeldungeon.levels.rooms.special.PoolRoom;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.sprites.CharSprite;
import com.leppa.prismaticpixeldungeon.sprites.BlueJellyfishSprite;
import com.leppa.prismaticpixeldungeon.sprites.GreenJellyfishSprite;
import com.leppa.prismaticpixeldungeon.sprites.PurpleJellyfishSprite;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Jellyfish extends Mob implements Callback{
	
	private static final float TIME_TO_ZAP = 1f;
	private static final String PRIVATE_DEPTH = "privateDepth";
	private static final String COLOUR = "colour";
	
	int privateDepth;
	int colour;
	
	public static final int BLUE = 0;
	public static final int GREEN = 1;
	public static final int PURPLE = 2;
	
	{
		spriteClass = BlueJellyfishSprite.class;
		
		EXP = 0;
		
		HUNTING = new Hunting();
		
		loot = BlueGel.class;
		lootChance = 1f;
		
		immunities.add(Burning.class);
		immunities.add(Vertigo.class);
		properties.add(Property.BLOB_IMMUNE);
		properties.add(Property.ELECTRIC);
	}
	
	public Jellyfish(){
		super();
		
		colour = Random.Int(3);
		if(colour == BLUE){
			spriteClass = BlueJellyfishSprite.class;
			loot = BlueGel.class;
		}
		if(colour == GREEN){
			spriteClass = GreenJellyfishSprite.class;
			loot = GreenGel.class;
		}
		if(colour == PURPLE){
			spriteClass = PurpleJellyfishSprite.class;
			loot = PurpleGel.class;
		}
		
		privateDepth = Dungeon.depth;
		HP = HT = 8 + privateDepth * 4;
		defenseSkill = 12 + privateDepth * 2;
	}
	
	public Jellyfish(int colour, int depth){
		super();
		
		this.colour = colour;
		if(colour == BLUE){
			spriteClass = BlueJellyfishSprite.class;
			loot = BlueGel.class;
		}
		if(colour == GREEN){
			spriteClass = GreenJellyfishSprite.class;
			loot = GreenGel.class;
		}
		if(colour == PURPLE){
			spriteClass = PurpleJellyfishSprite.class;
			loot = PurpleGel.class;
		}
		
		privateDepth = depth;
		HP = HT = 8 + privateDepth * 4;
		defenseSkill = 5 + privateDepth * 4;
	}
	
	protected boolean act(){
		if(!Dungeon.level.water[pos]){
			die(null);
			sprite.killAndErase();
			return true;
		}else{
			return super.act();
		}
	}
	
	public int damageRoll(){
		return Random.NormalIntRange(3 + privateDepth, 3 + privateDepth * 3);
	}
	
	public int attackSkill(Char target){
		return 25 + privateDepth * 2;
	}
	
	public int drRoll(){
		return Random.NormalIntRange(0, (int)(privateDepth * 0.5));
	}
	
	public boolean reset(){
		return true;
	}
	
	protected boolean getCloser(int target){
		if(rooted){
			return false;
		}
		int step = Dungeon.findStep(this, pos, target,
				Dungeon.level.water,
				fieldOfView);
		if(step != -1){
			move(step);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean attack(Char enemy){
		return false;
	}
	
	protected boolean getFurther(int target){
		int step = Dungeon.flee(this, pos, target,
				Dungeon.level.water,
				fieldOfView);
		if(step != -1){
			move(step);
			return true;
		}else{
			return false;
		}
	}
	
	protected boolean doAttack(Char enemy){
		boolean mobInWater = Dungeon.level.water[enemy.pos];
		
		boolean attackable = mobInWater ? Dungeon.level.distance(pos, enemy.pos) <= 2 : Dungeon.level.distance(pos, enemy.pos) <= 1;
		if(attackable){
			sprite.attack(enemy.pos);
		}
		
		spend(TIME_TO_ZAP);
		if(hit(this, enemy, true)){
			int dmg = damageRoll();
			if(Dungeon.level.water[enemy.pos] && !enemy.flying){
				dmg *= 1.5f;
			}
			enemy.damage(dmg, this);
			
			if(colour == BLUE && Random.Int(5) == 0) Buff.prolong(enemy, Paralysis.class, Random.Int(3, 7));
			if(colour == GREEN && Random.Int(3) == 0) Buff.prolong(enemy, Blindness.class, Random.Int(5, 15));
			if(colour == PURPLE && Random.Int(3) == 0) Buff.affect(enemy, Poison.class).set((int)(damageRoll()*0.25));
			
			enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 12);
			enemy.sprite.flash();
			
			if(enemy == Dungeon.hero){
				Camera.main.shake(3, 0.3f);
				
				if(!enemy.isAlive()){
					Dungeon.fail(getClass());
					GLog.n(Messages.get(this, "zap_kill"));
				}
			}
		}else{
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}
		
		return !attackable;
	}
	
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put(PRIVATE_DEPTH, privateDepth);
		bundle.put(COLOUR, colour);
	}
	
	public void restoreFromBundle(Bundle bundle){
		super.restoreFromBundle(bundle);
		privateDepth = bundle.getInt(PRIVATE_DEPTH);
		colour = bundle.getInt(COLOUR);
	}
	
	public void call(){
		next();
	}
	
	private class Hunting extends Mob.Hunting{
		
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted){
			boolean result = super.act(enemyInFOV, justAlerted);
			//this causes piranha to move away when a door is closed on them in a pool room.
			if(state == WANDERING && Dungeon.level instanceof RegularLevel){
				Room curRoom = ((RegularLevel)Dungeon.level).room(pos);
				if(curRoom instanceof PoolRoom){
					target = Dungeon.level.pointToCell(curRoom.random(1));
				}
			}
			return result;
		}
	}
	
	protected boolean canAttack(Char enemy){
		return Dungeon.level.distance(pos, enemy.pos) <= (Dungeon.level.water[enemy.pos] ? 2 : 1);
	}
}
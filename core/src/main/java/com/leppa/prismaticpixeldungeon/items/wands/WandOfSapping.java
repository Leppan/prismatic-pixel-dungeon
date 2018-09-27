package com.leppa.prismaticpixeldungeon.items.wands;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Burning;
import com.leppa.prismaticpixeldungeon.actors.buffs.Charm;
import com.leppa.prismaticpixeldungeon.actors.buffs.Healing;
import com.leppa.prismaticpixeldungeon.actors.buffs.Ooze;
import com.leppa.prismaticpixeldungeon.actors.buffs.Poison;
import com.leppa.prismaticpixeldungeon.actors.buffs.Weakness;
import com.leppa.prismaticpixeldungeon.effects.MagicMissile;
import com.leppa.prismaticpixeldungeon.items.weapon.enchantments.Vampiric;
import com.leppa.prismaticpixeldungeon.items.weapon.melee.MagesStaff;
import com.leppa.prismaticpixeldungeon.mechanics.Ballistica;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfSapping extends DamageWand{
	
	{
		image = ItemSpriteSheet.WAND_SAPPING;
	}
	
	@Override
	public int min(int lvl){
		return 2 + 2 * lvl;
	}
	
	@Override
	public int max(int lvl){
		return 3 + 4 * lvl;
	}
	
	@Override
	protected void onZap(Ballistica attack){
		int cell = attack.collisionPos;
		
		Char ch = Actor.findChar(cell);
		if(ch != null){
			processSoulMark(ch, chargesPerCast());
			int damageAmount = damageRoll();
			ch.damage(damageAmount, this);
			Buff.affect(curUser, Healing.class).setHeal((int)(0.5 * damageAmount), 0.333f, 0);
			
			ch.sprite.burst(0xFF9818FF, level() / 2 + 2);
			
			if(ch.properties().contains(Char.Property.UNDEAD)){
				Buff.affect(curUser, Weakness.class, Math.max(3, 8 - level()));
			}else if(ch.properties().contains(Char.Property.ACIDIC)){
				Buff.affect(curUser, Ooze.class);
			}else if(ch.properties().contains(Char.Property.FIERY)){
				Buff.affect(curUser, Burning.class);
			}else{
				Buff.affect(curUser, Charm.class, Math.max(3, 8 - level())).object = ch.id();
			}
		}else{
			Dungeon.level.press(attack.collisionPos, null, true);
		}
	}
	
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage){
		//Acts as Vampiric, could add direct sapping but eh
		new Vampiric().proc(staff, attacker, defender, damage);
	}
	
	public void staffFx(MagesStaff.StaffParticle particle){
		particle.color(0x9818FF);
		particle.am = 0.6f;
		particle.setLifespan(3f);
		float amt = Random.Float(PointF.PI2);
		particle.speed.polar(amt, 0.3f);
		particle.setSize(1f, 2f);
		particle.radiateXY(5f);
		
		particle.speed.polar(amt + PointF.PI, 2.5f);
	}
	
	protected void fx(Ballistica bolt, Callback callback){
		MagicMissile missile = ((MagicMissile)curUser.sprite.parent.recycle(MagicMissile.class));
		missile.reset(MagicMissile.MAGIC_MISSILE, DungeonTilemap.raisedTileCenterToWorld(bolt.collisionPos), curUser.sprite.center(), callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}
}
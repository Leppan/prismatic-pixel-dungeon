/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

<<<<<<< HEAD:core/src/main/java/com/leppa/prismaticpixeldungeon/items/wands/WandOfTransfusion.java
package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
=======
package com.leppa.prismaticpixeldungeon.items.wands;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Charm;
import com.leppa.prismaticpixeldungeon.actors.mobs.Mob;
import com.leppa.prismaticpixeldungeon.effects.Beam;
import com.leppa.prismaticpixeldungeon.effects.CellEmitter;
import com.leppa.prismaticpixeldungeon.effects.Speck;
import com.leppa.prismaticpixeldungeon.effects.particles.BloodParticle;
import com.leppa.prismaticpixeldungeon.effects.particles.LeafParticle;
import com.leppa.prismaticpixeldungeon.effects.particles.ShadowParticle;
import com.leppa.prismaticpixeldungeon.items.Generator;
import com.leppa.prismaticpixeldungeon.items.Heap;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.items.rings.Ring;
import com.leppa.prismaticpixeldungeon.items.weapon.melee.MagesStaff;
import com.leppa.prismaticpixeldungeon.levels.Terrain;
import com.leppa.prismaticpixeldungeon.mechanics.Ballistica;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.plants.Plant;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.leppa.prismaticpixeldungeon.sprites.CharSprite;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.tiles.DungeonTilemap;
import com.leppa.prismaticpixeldungeon.utils.GLog;
>>>>>>> 57789e0c... Added::core/src/main/java/com/leppa/prismaticpixeldungeon/items/wands/WandOfTransfusion.java
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfTransfusion extends Wand {

	{
		image = ItemSpriteSheet.WAND_TRANSFUSION;

		collisionProperties = Ballistica.PROJECTILE;
	}

	private boolean freeCharge = false;

	@Override
	protected void onZap(Ballistica beam) {

		for (int c : beam.subPath(0, beam.dist))
			CellEmitter.center(c).burst( BloodParticle.BURST, 1 );

		int cell = beam.collisionPos;

		Char ch = Actor.findChar(cell);

		if (ch != null && ch instanceof Mob){
			
			// 10% of max hp
			int selfDmg = (int)Math.ceil(curUser.HT*0.10f);

			processSoulMark(ch, chargesPerCast());
			
			//this wand does different things depending on the target.
			
			//heals/shields an ally, or a charmed enemy
			if (ch.alignment == Char.Alignment.ALLY || ch.buff(Charm.class) != null){
				
				int healing = selfDmg + 3*level();
				int shielding = (ch.HP + healing) - ch.HT;
				if (shielding > 0){
					healing -= shielding;
					Buff.affect(ch, Barrier.class).setShield(shielding);
				} else {
					shielding = 0;
				}
				
				ch.HP += healing;
				
				ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 2 + level() / 2);
				ch.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healing + shielding);

			//harms the undead
			} else if (ch.properties().contains(Char.Property.UNDEAD)){
				
				int damage = selfDmg + 3*level();
				ch.damage(damage, this);
				ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + level());
				Sample.INSTANCE.play(Assets.SND_BURNING);

			//charms an enemy
			} else {
				
				Buff.affect(ch , Charm.class, 4 + level() ).object = curUser.id();

				ch.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );

			}
			
			if (!freeCharge) {
				damageHero(selfDmg);
			} else {
				freeCharge = false;
			}
			
		}
		
	}

	//this wand costs health too
	private void damageHero(int damage){
		
		curUser.damage(damage, this);

		if (!curUser.isAlive()){
			Dungeon.fail( getClass() );
			GLog.n( Messages.get(this, "ondeath") );
		}
	}

	@Override
	protected int initialCharges() {
		return 1;
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		// lvl 0 - 10%
		// lvl 1 - 18%
		// lvl 2 - 25%
		if (Random.Int( level() + 10 ) >= 9){
			//grants a free use of the staff
			freeCharge = true;
			GLog.p( Messages.get(this, "charged") );
			attacker.sprite.emitter().burst(BloodParticle.BURST, 20);
		}
	}

	@Override
	protected void fx(Ballistica beam, Callback callback) {
		curUser.sprite.parent.add(
				new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0xCC0000 );
		particle.am = 0.6f;
		particle.setLifespan(1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2f);
		particle.radiateXY(0.5f);
	}

	private static final String FREECHARGE = "freecharge";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		freeCharge = bundle.getBoolean( FREECHARGE );
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FREECHARGE, freeCharge );
	}

}

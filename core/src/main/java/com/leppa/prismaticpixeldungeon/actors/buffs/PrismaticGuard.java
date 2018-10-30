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

package com.leppa.prismaticpixeldungeon.actors.buffs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.actors.mobs.Mob;
import com.leppa.prismaticpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.leppa.prismaticpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class PrismaticGuard extends Buff {
	
	{
		type = buffType.POSITIVE;
	}
	
	private float HP;
	
	@Override
	public boolean act() {
		
		Hero hero = (Hero)target;
		
		Mob closest = null;
		int v = hero.visibleEnemies();
		for (int i=0; i < v; i++) {
			Mob mob = hero.visibleEnemy( i );
			if ( mob.isAlive() && mob.state != mob.PASSIVE && !hero.mindVisionEnemies.contains(mob)
					&& (closest == null || Dungeon.level.distance(hero.pos, mob.pos) < Dungeon.level.distance(hero.pos, closest.pos))) {
				closest = mob;
			}
		}
		
		if (closest != null && Dungeon.level.distance(hero.pos, closest.pos) < 5){
			//spawn guardian
			int bestPos = -1;
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = hero.pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
					if (bestPos == -1 || Dungeon.level.trueDistance(p, closest.pos) < Dungeon.level.trueDistance(bestPos, closest.pos)){
						bestPos = p;
					}
				}
			}
			if (bestPos != -1) {
				PrismaticImage pris = new PrismaticImage();
				pris.duplicate(hero, (int)Math.floor(HP) );
				pris.state = pris.HUNTING;
				GameScene.add(pris, 1);
				ScrollOfTeleportation.appear(pris, bestPos);
				
				detach();
			} else {
				spend( TICK );
			}
			
			
		} else {
			spend(TICK);
		}
		
		LockedFloor lock = target.buff(LockedFloor.class);
		if (HP < maxHP() && (lock == null || lock.regenOn())){
			HP += 0.1f;
		}
		
		return true;
	}
	
	public void set( int HP ){
		this.HP = HP;
	}
	
	public int maxHP(){
		return maxHP((Hero)target);
	}
	
	public static int maxHP( Hero hero ){
		return 8 + (int)Math.floor(hero.lvl * 2.5f);
	}
	
	@Override
	public int icon() {
		return BuffIndicator.ARMOR;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.tint(0.5f, 0.5f, 1, 0.5f);
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", (int)HP, maxHP());
	}
	
	private static final String HEALTH = "hp";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(HEALTH, HP);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		HP = bundle.getFloat(HEALTH);
	}
}

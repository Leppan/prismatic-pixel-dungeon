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

package com.leppa.prismaticpixeldungeon.items.scrolls;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.ShatteredPixelDungeon;
import com.leppa.prismaticpixeldungeon.actors.buffs.Blindness;
import com.leppa.prismaticpixeldungeon.actors.buffs.MagicImmune;
import com.leppa.prismaticpixeldungeon.actors.hero.Hero;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.items.ItemStatusHandler;
import com.leppa.prismaticpixeldungeon.items.Recipe;
import com.leppa.prismaticpixeldungeon.items.artifacts.UnstableSpellbook;
import com.leppa.prismaticpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.leppa.prismaticpixeldungeon.items.stones.Runestone;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfAffection;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfAggression;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfAugmentation;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfBlast;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfBlink;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfClairvoyance;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfDeepenedSleep;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfDespair;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfDetectCurse;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfEnchantment;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfFlock;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfIntuition;
import com.leppa.prismaticpixeldungeon.items.stones.StoneOfShock;
import com.leppa.prismaticpixeldungeon.journal.Catalog;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.sprites.HeroSprite;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Scroll extends Item {
	
	public static final String AC_READ	= "READ";
	
	protected static final float TIME_TO_READ	= 1f;

	protected Integer initials;

	private static final Class<?>[] scrolls = {
		ScrollOfIdentify.class,
		ScrollOfMagicMapping.class,
		ScrollOfRecharging.class,
		ScrollOfRemoveCurse.class,
		ScrollOfTeleportation.class,
		ScrollOfUpgrade.class,
		ScrollOfRage.class,
		ScrollOfTerror.class,
		ScrollOfLullaby.class,
		ScrollOfTransmutation.class,
		ScrollOfRetribution.class,
		ScrollOfMirrorImage.class,
		ScrollOfCorruption.class
	};

	private static final HashMap<String, Integer> runes = new HashMap<String, Integer>() {
		{
			put("KAUNAN",ItemSpriteSheet.SCROLL_KAUNAN);
			put("SOWILO",ItemSpriteSheet.SCROLL_SOWILO);
			put("LAGUZ",ItemSpriteSheet.SCROLL_LAGUZ);
			put("YNGVI",ItemSpriteSheet.SCROLL_YNGVI);
			put("GYFU",ItemSpriteSheet.SCROLL_GYFU);
			put("RAIDO",ItemSpriteSheet.SCROLL_RAIDO);
			put("ISAZ",ItemSpriteSheet.SCROLL_ISAZ);
			put("MANNAZ",ItemSpriteSheet.SCROLL_MANNAZ);
			put("NAUDIZ",ItemSpriteSheet.SCROLL_NAUDIZ);
			put("BERKANAN",ItemSpriteSheet.SCROLL_BERKANAN);
			put("ODAL",ItemSpriteSheet.SCROLL_ODAL);
			put("TIWAZ",ItemSpriteSheet.SCROLL_TIWAZ);
			put("FERN",ItemSpriteSheet.SCROLL_FERN);
			put("WYNN",ItemSpriteSheet.SCROLL_WYNN);
		}
	};
	
	protected static ItemStatusHandler<Scroll> handler;
	
	protected String rune;
	
	{
		stackable = true;
		defaultAction = AC_READ;
	}
	
	@SuppressWarnings("unchecked")
	public static void initLabels() {
		handler = new ItemStatusHandler<>( (Class<? extends Scroll>[])scrolls, runes );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
		handler.saveSelectively( bundle, items );
	}

	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<>( (Class<? extends Scroll>[])scrolls, runes, bundle );
	}
	
	public Scroll() {
		super();
		reset();
	}
	
	//anonymous scrolls are always IDed, do not affect ID status,
	//and their sprite is replaced by a placeholder if they are not known,
	//useful for items that appear in UIs, or which are only spawned for their effects
	protected boolean anonymous = false;
	public void anonymize(){
		if (!isKnown()) image = ItemSpriteSheet.SCROLL_HOLDER;
		anonymous = true;
	}
	
	
	@Override
	public void reset(){
		super.reset();
		if (handler != null && handler.contains(this)) {
			image = handler.image(this);
			rune = handler.label(this);
		}
	};
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_READ )) {
			
			if (hero.buff(MagicImmune.class) != null){
				GLog.w( Messages.get(this, "no_magic") );
			} else if (hero.buff( Blindness.class ) != null) {
				GLog.w( Messages.get(this, "blinded") );
			} else if (hero.buff(UnstableSpellbook.bookRecharge.class) != null
					&& hero.buff(UnstableSpellbook.bookRecharge.class).isCursed()
					&& !(this instanceof ScrollOfRemoveCurse || this instanceof ScrollOfAntiMagic)){
				GLog.n( Messages.get(this, "cursed") );
			} else {
				curUser = hero;
				curItem = detach( hero.belongings.backpack );
				doRead();
			}
			
		}
	}
	
	public abstract void doRead();
	
	//currently only used in scrolls owned by the unstable spellbook
	public abstract void empoweredRead();

	protected void readAnimation() {
		curUser.spend( TIME_TO_READ );
		curUser.busy();
		((HeroSprite)curUser.sprite).read();
	}
	
	public boolean isKnown() {
		return anonymous || (handler != null && handler.isKnown( this ));
	}
	
	public void setKnown() {
		if (!anonymous) {
			if (!isKnown()) {
				handler.know(this);
				updateQuickslot();
			}
			
			if (Dungeon.hero.isAlive()) {
				Catalog.setSeen(getClass());
			}
		}
	}
	
	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}
	
	@Override
	public String name() {
		return isKnown() ? name : Messages.get(this, rune);
	}
	
	@Override
	public String info() {
		return isKnown() ?
			desc() :
			Messages.get(this, "unknown_desc");
	}

	public Integer initials(){
		return isKnown() ? initials : null;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return isKnown();
	}
	
	public static HashSet<Class<? extends Scroll>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Scroll>> getUnknown() {
		return handler.unknown();
	}
	
	public static boolean allKnown() {
		return handler.known().size() == scrolls.length;
	}
	
	@Override
	public int price() {
		return 40 * quantity;
	}
	
	public static class ScrollToStone extends Recipe {
		
		private static HashMap<Class<?extends Scroll>, Class<?extends Runestone>> stones = new HashMap<>();
		private static HashMap<Class<?extends Scroll>, Integer> amnts = new HashMap<>();
		static {
			stones.put(ScrollOfIdentify.class,      StoneOfIntuition.class);
			amnts.put(ScrollOfIdentify.class,       3);
			
			stones.put(ScrollOfLullaby.class,       StoneOfDeepenedSleep.class);
			amnts.put(ScrollOfLullaby.class,        3);
			
			stones.put(ScrollOfMagicMapping.class,  StoneOfClairvoyance.class);
			amnts.put(ScrollOfMagicMapping.class,   3);
			
			stones.put(ScrollOfMirrorImage.class,   StoneOfFlock.class);
			amnts.put(ScrollOfMirrorImage.class,    3);
			
			stones.put(ScrollOfRetribution.class,   StoneOfBlast.class);
			amnts.put(ScrollOfRetribution.class,    2);
			
			stones.put(ScrollOfRage.class,          StoneOfAggression.class);
			amnts.put(ScrollOfRage.class,           3);
			
			stones.put(ScrollOfRecharging.class,    StoneOfShock.class);
			amnts.put(ScrollOfRecharging.class,     2);
			
			stones.put(ScrollOfRemoveCurse.class,   StoneOfDetectCurse.class);
			amnts.put(ScrollOfRemoveCurse.class,    2);
			
			stones.put(ScrollOfTeleportation.class, StoneOfBlink.class);
			amnts.put(ScrollOfTeleportation.class,  2);
			
			stones.put(ScrollOfTerror.class,        StoneOfAffection.class);
			amnts.put(ScrollOfTerror.class,         3);
			
			stones.put(ScrollOfTransmutation.class, StoneOfAugmentation.class);
			amnts.put(ScrollOfTransmutation.class,  2);
			
			stones.put(ScrollOfUpgrade.class,       StoneOfEnchantment.class);
			amnts.put(ScrollOfUpgrade.class,        2);
			
			stones.put(ScrollOfCorruption.class,    StoneOfDespair.class);
			amnts.put(ScrollOfCorruption.class,     3);
		}
		
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 1
					|| !(ingredients.get(0) instanceof Scroll)
					|| !stones.containsKey(ingredients.get(0).getClass())){
				return false;
			}
			
			return true;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 0;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			Scroll s = (Scroll) ingredients.get(0);
			
			s.quantity(s.quantity() - 1);
			
			try{
				return stones.get(s.getClass()).newInstance().quantity(amnts.get(s.getClass()));
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
				return null;
			}
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			try{
				Scroll s = (Scroll) ingredients.get(0);
				return stones.get(s.getClass()).newInstance().quantity(amnts.get(s.getClass()));
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
				return null;
			}
		}
	}
}
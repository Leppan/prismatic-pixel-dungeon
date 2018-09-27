package com.leppa.prismaticpixeldungeon.actors.mobs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.items.artifacts.DeckOfElements;
import com.leppa.prismaticpixeldungeon.items.weapon.Weapon;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.sprites.ElementalImageSprite;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Collection;

public class ElementalImage extends Statue{
	
	private static final String INSTANCES = "instances";
	private static final String ENCHANTMENT = "enchantment";
	
	{
		spriteClass = ElementalImageSprite.class;
		
		EXP = 0;
		state = WANDERING;
		
		properties.add(Property.MINIBOSS);
	}
	
	public Weapon.Enchantment enchantment;
	public static SparseArray<ElementalImage> instances = new SparseArray<>();
	
	public ElementalImage(){
		super();
		enchantment = Weapon.Enchantment.random();
		while(DeckOfElements.usableEnchants.size() < 13 && DeckOfElements.usableEnchants.contains(enchantment.getClass())){
			enchantment = Weapon.Enchantment.random();
		}
		weapon.enchant(enchantment);
		weapon.upgrade();
		resistances.add(enchantment.getClass());
		name = enchantment.name(Messages.get(this, "name"));
		instances.put(Dungeon.depth, this);
	}
	
	public void beckon(int cell){
		notice();
		
		if(state != HUNTING){
			state = WANDERING;
		}
		target = cell;
	}
	
	protected boolean act(){
		return super.act();
	}
	
	public static void storeAllInBundle(Bundle bundle){
		bundle.put(INSTANCES, instances.values());
	}
	
	public static void restoreAllFromBundle(Bundle bundle){
		Collection<Bundlable> collection = bundle.getCollection(INSTANCES);
		for(Bundlable p : collection){
			ElementalImage image = (ElementalImage)p;
			instances.put(image.pos, image);
		}
	}
	
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put(ENCHANTMENT, enchantment);
	}
	
	public void restoreFromBundle(Bundle bundle){
		super.restoreFromBundle(bundle);
		enchantment = (Weapon.Enchantment)bundle.get(ENCHANTMENT);
	}
	
	public void die(Object cause){
		weapon.degrade();
		super.die(cause);
		
		DeckOfElements deck = Dungeon.hero.belongings.getItem(DeckOfElements.class);
		if(deck != null){
			deck.upgrade();
			GLog.p(Messages.get(DeckOfElements.class, "upgrade"));
		}
	}
	
	public String description() {
		return Messages.get(this, "desc", weapon.name());
	}
}
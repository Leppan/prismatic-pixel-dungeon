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

package com.leppa.prismaticpixeldungeon.ui;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.ShatteredPixelDungeon;
import com.leppa.prismaticpixeldungeon.items.Generator;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.items.Recipe;
import com.leppa.prismaticpixeldungeon.items.bombs.Bomb;
import com.leppa.prismaticpixeldungeon.items.food.Blandfruit;
import com.leppa.prismaticpixeldungeon.items.food.ColourGel;
import com.leppa.prismaticpixeldungeon.items.food.Food;
import com.leppa.prismaticpixeldungeon.items.food.MeatPie;
import com.leppa.prismaticpixeldungeon.items.food.MysteryMeat;
import com.leppa.prismaticpixeldungeon.items.food.Pasty;
import com.leppa.prismaticpixeldungeon.items.food.StewedMeat;
import com.leppa.prismaticpixeldungeon.items.potions.Potion;
import com.leppa.prismaticpixeldungeon.items.potions.brews.BlizzardBrew;
import com.leppa.prismaticpixeldungeon.items.potions.brews.CausticBrew;
import com.leppa.prismaticpixeldungeon.items.potions.brews.FrigidBrew;
import com.leppa.prismaticpixeldungeon.items.potions.brews.FrostfireBrew;
import com.leppa.prismaticpixeldungeon.items.potions.brews.InfernalBrew;
import com.leppa.prismaticpixeldungeon.items.potions.brews.ShockingBrew;
import com.leppa.prismaticpixeldungeon.items.potions.brews.WickedBrew;
import com.leppa.prismaticpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.leppa.prismaticpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.leppa.prismaticpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.leppa.prismaticpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.leppa.prismaticpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.leppa.prismaticpixeldungeon.items.potions.elixirs.ElixirOfRestoration;
import com.leppa.prismaticpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.leppa.prismaticpixeldungeon.items.potions.elixirs.ElixirOfVitality;
import com.leppa.prismaticpixeldungeon.items.potions.exotic.ExoticPotion;
import com.leppa.prismaticpixeldungeon.items.scrolls.Scroll;
import com.leppa.prismaticpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.leppa.prismaticpixeldungeon.items.spells.Alchemize;
import com.leppa.prismaticpixeldungeon.items.spells.AquaBlast;
import com.leppa.prismaticpixeldungeon.items.spells.BeaconOfReturning;
import com.leppa.prismaticpixeldungeon.items.spells.CurseInfusion;
import com.leppa.prismaticpixeldungeon.items.spells.FeatherFall;
import com.leppa.prismaticpixeldungeon.items.spells.MagicalInfusion;
import com.leppa.prismaticpixeldungeon.items.spells.MagicalPorter;
import com.leppa.prismaticpixeldungeon.items.spells.PhaseShift;
import com.leppa.prismaticpixeldungeon.items.spells.ReclaimTrap;
import com.leppa.prismaticpixeldungeon.items.spells.Recycle;
import com.leppa.prismaticpixeldungeon.items.stones.Runestone;
import com.leppa.prismaticpixeldungeon.items.weapon.missiles.darts.Dart;
import com.leppa.prismaticpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.plants.Plant;
import com.leppa.prismaticpixeldungeon.scenes.AlchemyScene;
import com.leppa.prismaticpixeldungeon.scenes.PixelScene;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.windows.WndBag;
import com.leppa.prismaticpixeldungeon.windows.WndInfoItem;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Arrays;

public class QuickRecipe extends Component {
	
	private ArrayList<Item> ingredients;
	
	private ArrayList<ItemSlot> inputs;
	private QuickRecipe.arrow arrow;
	private ItemSlot output;
	
	public QuickRecipe(Recipe.SimpleRecipe r){
		this(r, r.getIngredients(), r.sampleOutput(null));
	}
	
	public QuickRecipe(Recipe r, ArrayList<Item> inputs, final Item output) {
		
		ingredients = inputs;
		int cost = r.cost(inputs);
		boolean hasInputs = true;
		this.inputs = new ArrayList<>();
		for (final Item in : inputs) {
			anonymize(in);
			ItemSlot curr;
			curr = new ItemSlot(in) {
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(in));
				}
			};
			
			ArrayList<Item> similar = Dungeon.hero.belongings.getAllSimilar(in);
			int quantity = 0;
			for (Item sim : similar) {
				if (sim.isIdentified()) quantity += sim.quantity();
			}
			
			if (quantity < in.quantity()) {
				curr.icon.alpha(0.3f);
				hasInputs = false;
			}
			curr.showParams(true, false, true);
			add(curr);
			this.inputs.add(curr);
		}
		
		if (cost > 0) {
			arrow = new arrow(Icons.get(Icons.RESUME), cost);
			arrow.hardlightText(0x00CCFF);
		} else {
			arrow = new arrow(Icons.get(Icons.RESUME));
		}
		if (hasInputs) {
			arrow.icon.tint(1, 1, 0, 1);
			if (!(ShatteredPixelDungeon.scene() instanceof AlchemyScene)) {
				arrow.enable(false);
			}
		} else {
			arrow.icon.color(0, 0, 0);
			arrow.enable(false);
		}
		add(arrow);
		
		anonymize(output);
		this.output = new ItemSlot(output){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(output));
			}
		};
		if (!hasInputs){
			this.output.icon.alpha(0.3f);
		}
		this.output.showParams(true, false, true);
		add(this.output);
		
		layout();
	}
	
	@Override
	protected void layout() {
		
		height = 16;
		width = 0;
		
		for (ItemSlot item : inputs){
			item.setRect(x + width, y, 16, 16);
			width += 16;
		}
		
		arrow.setRect(x + width, y, 14, 16);
		width += 14;
		
		output.setRect(x + width, y, 16, 16);
		width += 16;
	}
	
	//used to ensure that un-IDed items are not spoiled
	private void anonymize(Item item){
		if (item instanceof Potion){
			((Potion) item).anonymize();
		} else if (item instanceof Scroll){
			((Scroll) item).anonymize();
		}
	}
	
	public class arrow extends IconButton {
		
		BitmapText text;
		
		public arrow(){
			super();
		}
		
		public arrow( Image icon ){
			super( icon );
		}
		
		public arrow( Image icon, int count ){
			super( icon );
			text = new BitmapText( Integer.toString(count), PixelScene.pixelFont);
			text.measure();
			add(text);
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (text != null){
				text.x = x;
				text.y = y;
				PixelScene.align(text);
			}
		}
		
		@Override
		protected void onClick() {
			super.onClick();
			
			//find the window this is inside of and close it
			Group parent = this.parent;
			while (parent != null){
				if (parent instanceof Window){
					((Window) parent).hide();
					break;
				} else {
					parent = parent.parent;
				}
			}
			
			((AlchemyScene)ShatteredPixelDungeon.scene()).populate(ingredients, Dungeon.hero.belongings);
		}
		
		public void hardlightText(int color ){
			if (text != null) text.hardlight(color);
		}
	}
	
	//gets recipes for a particular alchemy guide page
	//a null entry indicates a break in section
	public static ArrayList<QuickRecipe> getRecipes( int pageIdx ){
		ArrayList<QuickRecipe> result = new ArrayList<>();
		switch (pageIdx){
			case 0: default:
				result.add(new QuickRecipe( new Potion.SeedToPotion(), new ArrayList<>(Arrays.asList(new Plant.Seed.PlaceHolder().quantity(3))), new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
					{
						name = Messages.get(Potion.SeedToPotion.class, "name");
					}
					
					@Override
					public String info() {
						return "";
					}
				}));
				return result;
			case 1:
				Recipe r = new Scroll.ScrollToStone();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					try{
						Scroll scroll = (Scroll) cls.newInstance();
						ArrayList<Item> in = new ArrayList<Item>(Arrays.asList(scroll));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 2:
				r = new TippedDart.TipDart();
				for (Class<?> cls : Generator.Category.SEED.classes){
					try{
						Plant.Seed seed = (Plant.Seed) cls.newInstance();
						ArrayList<Item> in = new ArrayList<>(Arrays.asList(seed, new Dart()));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 3:
				r = new ExoticPotion.PotionToExotic();
				for (Class<?> cls : Generator.Category.POTION.classes){
					try{
						Potion pot = (Potion) cls.newInstance();
						ArrayList<Item> in = new ArrayList<>(Arrays.asList(pot, new Plant.Seed.PlaceHolder().quantity(2)));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 4:
				r = new ExoticScroll.ScrollToExotic();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					try{
						Scroll scroll = (Scroll) cls.newInstance();
						ArrayList<Item> in = new ArrayList<>(Arrays.asList(scroll, new Runestone.PlaceHolder().quantity(2)));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 5:
				result.add(new QuickRecipe( new StewedMeat.oneMeat() ));
				result.add(new QuickRecipe( new StewedMeat.twoMeat() ));
				result.add(new QuickRecipe( new StewedMeat.threeMeat() ));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new MeatPie.Recipe(),
						new ArrayList<Item>(Arrays.asList(new Pasty(), new Food(), new MysteryMeat.PlaceHolder())),
						new MeatPie()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new Blandfruit.CookFruit(),
						new ArrayList<>(Arrays.asList(new Blandfruit(), new Plant.Seed.PlaceHolder())),
						new Blandfruit(){
							{
								name = Messages.get(Blandfruit.class, "cooked");
							}
							
							@Override
							public String info() {
								return "";
							}
						}));
				return result;
			case 6:
				r = new Bomb.EnhanceBomb();
				int i = 0;
				for (Class<?> cls : Bomb.EnhanceBomb.validIngredients.keySet()){
					try{
						if (i == 2){
							result.add(null);
							i = 0;
						}
						Item item = (Item) cls.newInstance();
						ArrayList<Item> in = new ArrayList<Item>(Arrays.asList(new Bomb(), item));
						result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
						i++;
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
				return result;
			case 7:
				result.add(new QuickRecipe(new WickedBrew.Recipe()));
				result.add(new QuickRecipe(new FrigidBrew.Recipe()));
				result.add(new QuickRecipe(new FrostfireBrew.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new InfernalBrew.Recipe()));
				result.add(new QuickRecipe(new BlizzardBrew.Recipe()));
				result.add(new QuickRecipe(new ShockingBrew.Recipe()));
				result.add(new QuickRecipe(new CausticBrew.Recipe()));
				return result;
			case 8:
				result.add(new QuickRecipe(new ElixirOfRestoration.Recipe()));
				result.add(new QuickRecipe(new ElixirOfVitality.Recipe()));
				result.add(new QuickRecipe(new ElixirOfHoneyedHealing.Recipe()));
				result.add(new QuickRecipe(new ElixirOfAquaticRejuvenation.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ElixirOfDragonsBlood.Recipe()));
				result.add(new QuickRecipe(new ElixirOfIcyTouch.Recipe()));
				result.add(new QuickRecipe(new ElixirOfToxicEssence.Recipe()));
				result.add(new QuickRecipe(new ElixirOfMight.Recipe()));
				return result;
			case 9:
				result.add(new QuickRecipe(new MagicalPorter.Recipe()));
				result.add(new QuickRecipe(new PhaseShift.Recipe()));
				result.add(new QuickRecipe(new BeaconOfReturning.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new AquaBlast.Recipe()));
				result.add(new QuickRecipe(new FeatherFall.Recipe()));
				result.add(new QuickRecipe(new ReclaimTrap.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new MagicalInfusion.Recipe()));
				result.add(new QuickRecipe(new CurseInfusion.Recipe()));
				result.add(new QuickRecipe(new Alchemize.Recipe()));
				result.add(new QuickRecipe(new Recycle.Recipe()));
				return result;
			case 10:
				result.add(new QuickRecipe((new ColourGel.BlueGelRecipe())));
				result.add(new QuickRecipe((new ColourGel.GreenGelRecipe())));
				result.add(new QuickRecipe((new ColourGel.PurpleGelRecipe())));
				return result;
		}
	}
	
}

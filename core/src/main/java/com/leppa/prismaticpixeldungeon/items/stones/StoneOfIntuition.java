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

package com.leppa.prismaticpixeldungeon.items.stones;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.effects.Identification;
import com.leppa.prismaticpixeldungeon.items.Item;
import com.leppa.prismaticpixeldungeon.items.potions.Potion;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfExperience;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfFrost;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfHaste;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfHealing;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfInvisibility;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfLevitation;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfMindVision;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfParalyticGas;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfPurity;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfStrength;
import com.leppa.prismaticpixeldungeon.items.potions.PotionOfToxicGas;
import com.leppa.prismaticpixeldungeon.items.potions.exotic.ExoticPotion;
import com.leppa.prismaticpixeldungeon.items.scrolls.Scroll;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfRage;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfTerror;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.leppa.prismaticpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.leppa.prismaticpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.leppa.prismaticpixeldungeon.scenes.PixelScene;
import com.leppa.prismaticpixeldungeon.sprites.ItemSprite;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.leppa.prismaticpixeldungeon.ui.IconButton;
import com.leppa.prismaticpixeldungeon.ui.RedButton;
import com.leppa.prismaticpixeldungeon.ui.RenderedTextMultiline;
import com.leppa.prismaticpixeldungeon.ui.Window;
import com.leppa.prismaticpixeldungeon.utils.GLog;
import com.leppa.prismaticpixeldungeon.windows.IconTitle;
import com.leppa.prismaticpixeldungeon.windows.WndBag;
import com.watabou.noosa.Image;

import java.util.HashSet;

public class StoneOfIntuition extends InventoryStone {
	
	
	{
		mode = WndBag.Mode.UNIDED_POTION_OR_SCROLL;
		image = ItemSpriteSheet.STONE_INTUITION;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		GameScene.show( new WndGuess(item));
		
	}
	
	//in order of their consumable icon
	public static Class[] potions = new Class[]{
			PotionOfExperience.class,
			PotionOfFrost.class,
			PotionOfHaste.class,
			PotionOfHealing.class,
			PotionOfInvisibility.class,
			PotionOfLevitation.class,
			PotionOfLiquidFlame.class,
			PotionOfMindVision.class,
			PotionOfParalyticGas.class,
			PotionOfPurity.class,
			PotionOfStrength.class,
			PotionOfToxicGas.class
	};
	
	public static Class[] scrolls = new Class[]{
			ScrollOfIdentify.class,
			ScrollOfLullaby.class,
			ScrollOfMagicMapping.class,
			ScrollOfMirrorImage.class,
			ScrollOfRetribution.class,
			ScrollOfRage.class,
			ScrollOfRecharging.class,
			ScrollOfRemoveCurse.class,
			ScrollOfTeleportation.class,
			ScrollOfTerror.class,
			ScrollOfTransmutation.class,
			ScrollOfUpgrade.class
	};
	
	static Class curGuess = null;
	
	public class WndGuess extends Window {
		
		private static final int WIDTH = 120;
		private static final int BTN_SIZE = 20;
		
		public WndGuess(final Item item){
			
			IconTitle titlebar = new IconTitle();
			titlebar.icon( new ItemSprite(ItemSpriteSheet.STONE_INTUITION, null) );
			titlebar.label( Messages.get(StoneOfIntuition.class, "name") );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );
			
			RenderedTextMultiline text = PixelScene.renderMultiline(6);
			text.text( Messages.get(this, "text") );
			text.setPos(0, titlebar.bottom());
			text.maxWidth( WIDTH );
			add(text);
			
			final RedButton guess = new RedButton(""){
				@Override
				protected void onClick() {
					super.onClick();
					useAnimation();
					if (item.getClass() == curGuess){
						item.identify();
						GLog.p( Messages.get(WndGuess.class, "correct") );
						curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );
					} else {
						GLog.n( Messages.get(WndGuess.class, "incorrect") );
					}
					curGuess = null;
					hide();
				}
			};
			guess.visible = false;
			guess.icon( new ItemSprite(item) );
			guess.enable(false);
			guess.setRect(0, 80, WIDTH, 20);
			add(guess);
			
			float left;
			float top = text.bottom() + 5;
			int rows;
			int placed = 0;
			
			HashSet<Class<?extends Item>> unIDed = new HashSet<>();
			final Class[] all;
			
			final int row;
			if (item.isIdentified()){
				hide();
				return;
			} else if (item instanceof Potion){
				unIDed.addAll(Potion.getUnknown());
				all = potions.clone();
				if (item instanceof ExoticPotion){
					row = 8;
					for (int i = 0; i < all.length; i++){
						all[i] = ExoticPotion.regToExo.get(all[i]);
					}
					HashSet<Class<?extends Item>> exoUID = new HashSet<>();
					for (Class<?extends Item> i : unIDed){
						exoUID.add(ExoticPotion.regToExo.get(i));
					}
					unIDed = exoUID;
				} else {
					row = 0;
				}
			} else if (item instanceof Scroll){
				unIDed.addAll(Scroll.getUnknown());
				all = scrolls.clone();
				if (item instanceof ExoticScroll){
					row = 24;
					for (int i = 0; i < all.length; i++){
						all[i] = ExoticScroll.regToExo.get(all[i]);
					}
					HashSet<Class<?extends Item>> exoUID = new HashSet<>();
					for (Class<?extends Item> i : unIDed){
						exoUID.add(ExoticScroll.regToExo.get(i));
					}
					unIDed = exoUID;
				} else {
					row = 16;
				}
			} else {
				hide();
				return;
			}
			
			if (unIDed.size() < 6){
				rows = 1;
				top += BTN_SIZE/2f;
				left = (WIDTH - BTN_SIZE*unIDed.size())/2f;
			} else {
				rows = 2;
				left = (WIDTH - BTN_SIZE*((unIDed.size()+1)/2))/2f;
			}
			
			for (int i = 0; i < all.length; i++){
				if (!unIDed.contains(all[i])) {
					continue;
				}
				
				final int j = i;
				IconButton btn = new IconButton(){
					@Override
					protected void onClick() {
						curGuess = all[j];
						guess.visible = true;
						guess.text( Messages.get(curGuess, "name") );
						guess.enable(true);
						super.onClick();
					}
				};
				Image im = new Image(Assets.CONS_ICONS, 7*i, row, 7, 8);
				im.scale.set(2f);
				btn.icon(im);
				btn.setRect(left + placed*BTN_SIZE, top, BTN_SIZE, BTN_SIZE);
				add(btn);
				
				placed++;
				if (rows == 2 && placed == ((unIDed.size()+1)/2)){
					placed = 0;
					if (unIDed.size() % 2 == 1){
						left += BTN_SIZE/2f;
					}
					top += BTN_SIZE;
				}
			}
			
			resize(WIDTH, 100);
			
		}
		
		
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			new StoneOfIntuition().collect();
		}
	}
}

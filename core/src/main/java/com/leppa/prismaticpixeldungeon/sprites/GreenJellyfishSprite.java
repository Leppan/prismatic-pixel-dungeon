package com.leppa.prismaticpixeldungeon.sprites;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.mobs.Jellyfish;
import com.leppa.prismaticpixeldungeon.effects.Lightning;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.watabou.noosa.TextureFilm;

public class GreenJellyfishSprite extends MobSprite{
	
	public GreenJellyfishSprite(){
		renderShadow = false;
		perspectiveRaise = 0.2f;
		
		texture(Assets.JELLYFISH);
		
		TextureFilm frames = new TextureFilm(texture, 16, 16);
		
		idle = new Animation(1, true);
		idle.frames(frames, 32+0, 32+1);
		
		run = new Animation(10, true);
		run.frames(frames, 32+0, 32+1);
		
		attack = new Animation(14, false);
		attack.frames(frames, 32+3, 32+4, 32+5, 32+6, 32+7);
		
		zap = attack.clone();
		
		die = new Animation(4, false);
		die.frames(frames, 32+8, 32+9, 32+10, 32+11);
		
		play(idle);
	}
	
	@Override
	public void link(Char ch){
		super.link(ch);
		renderShadow = false;
	}
	
	@Override
	public void onComplete(Animation anim){
		super.onComplete(anim);
		
		if(anim == attack){
			GameScene.ripple(ch.pos);
		}
	}
	
	public void attack(int pos){
		parent.add(new Lightning(ch.pos, pos, (Jellyfish)ch));
		
		turnTo(ch.pos, pos);
		play(attack);
	}
}

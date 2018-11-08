package com.leppa.prismaticpixeldungeon.sprites;

import com.leppa.prismaticpixeldungeon.Assets;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.watabou.noosa.TextureFilm;

public class JellyfishSprite extends MobSprite{
	
	public JellyfishSprite(){
		renderShadow = false;
		perspectiveRaise = 0.2f;
		
		texture(Assets.JELLYFISH);
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 2, true );
		idle.frames( frames, 0, 1);

		run = new Animation( 10, true );
		run.frames( frames, 0, 1);

		attack = new Animation( 10, false );
		attack.frames( frames, 3, 4, 5, 6, 7);

		die = new Animation( 4, false );
		die.frames( frames, 8, 9, 10, 11);

		play( idle );
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
}

package com.leppa.prismaticpixeldungeon.windows;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.scenes.InterlevelScene;
import com.leppa.prismaticpixeldungeon.ui.OptionSlider;
import com.leppa.prismaticpixeldungeon.ui.RedButton;
import com.leppa.prismaticpixeldungeon.ui.Window;
import com.watabou.noosa.Game;

public class WndSendDepth extends Window{
	
	static int depthToSend = 1;
	
	private static final int SLIDER_HEIGHT = 24;
	private static final int BUTTON_HEIGHT = 20;
	private static final int WIDTH = 140;
	
	public WndSendDepth(){
		final RedButton sendToDepth = new RedButton(Messages.get(this, "send", depthToSend)){
			protected void onClick(){
				Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
				if(buff != null) buff.detach();
				
				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnDepth = depthToSend;
				InterlevelScene.returnPos = -1;
				Game.switchScene(InterlevelScene.class);
			}
		};
		
		OptionSlider depthScale = new OptionSlider(Messages.get(this, "depth"), "Floor 1", "Floor 31", 1, 31){
			protected void onChange(){
				depthToSend = getSelectedValue();
				sendToDepth.text(Messages.get(WndSendDepth.class, "send", depthToSend));
			}
		};
		depthScale.setSelectedValue(1);
		depthScale.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
		add(depthScale);
		
		sendToDepth.setRect(0, SLIDER_HEIGHT + 10, WIDTH, BUTTON_HEIGHT);
		add(sendToDepth);
		resize(WIDTH, SLIDER_HEIGHT + 10 + BUTTON_HEIGHT);
	}
}
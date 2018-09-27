package com.leppa.prismaticpixeldungeon.actors.mobs.npcs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.sprites.AnyPushableSheepSprite;
import com.leppa.prismaticpixeldungeon.sprites.HorizontalSheepSprite;
import com.watabou.utils.PathFinder;

public class AnyPushablePuzzleSheep extends Sheep{

    {
        spriteClass = AnyPushableSheepSprite.class;
    }
    
    protected boolean act() {
        spend(1);
        return true;
    }
    
    @Override
    public boolean interact(){
        for(int x : PathFinder.NEIGHBOURS4){
            if(Dungeon.hero.pos == pos + x){
                sprite.move(pos, pos - x);
                move(pos - x);
                Dungeon.hero.sprite.move(Dungeon.hero.pos, Dungeon.hero.pos - x);
                Dungeon.hero.move(Dungeon.hero.pos - x);
                Dungeon.hero.spend(1 / Dungeon.hero.speed());
                Dungeon.hero.busy();
                return true;
            }
        }
        super.interact();
        return false;
    }
}
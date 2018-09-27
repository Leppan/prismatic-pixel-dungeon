package com.leppa.prismaticpixeldungeon.actors.mobs.npcs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.sprites.HorizontalSheepSprite;
import com.leppa.prismaticpixeldungeon.sprites.SheepSprite;

public class HorizontalPuzzleSheep extends Sheep{

    {
        spriteClass = HorizontalSheepSprite.class;
    }
    
    protected boolean act() {
        spend(1);
        return true;
    }
    
    @Override
    public boolean interact(){
        if(Dungeon.hero.pos == pos + 1 && Dungeon.level.passable[pos-1]){
            sprite.move(pos, pos - 1);
            move(pos - 1);
            Dungeon.hero.sprite.move(Dungeon.hero.pos, Dungeon.hero.pos - 1);
            Dungeon.hero.move(Dungeon.hero.pos - 1);
            Dungeon.hero.spend(1 / Dungeon.hero.speed());
            Dungeon.hero.busy();
            return true;
        }else if(Dungeon.hero.pos == pos - 1 && Dungeon.level.passable[pos+1]){
            sprite.move(pos, pos + 1);
            move(pos + 1);
            Dungeon.hero.sprite.move(Dungeon.hero.pos, Dungeon.hero.pos + 1);
            Dungeon.hero.move(Dungeon.hero.pos + 1);
            Dungeon.hero.spend(1 / Dungeon.hero.speed());
            Dungeon.hero.busy();
            return true;
        }else{
            super.interact();
        }
        return false;
    }
}
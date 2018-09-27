package com.leppa.prismaticpixeldungeon.actors.mobs.npcs;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.sprites.HorizontalSheepSprite;
import com.leppa.prismaticpixeldungeon.sprites.VerticalSheepSprite;

public class VerticalPuzzleSheep extends Sheep{

    {
        spriteClass = VerticalSheepSprite.class;
    }
    
    protected boolean act() {
        spend(1);
        return true;
    }
    
    @Override
    public boolean interact(){
        if(Dungeon.hero.pos == pos + Dungeon.level.width()){
            sprite.move(pos, pos - Dungeon.level.width());
            move(pos - Dungeon.level.width());
            Dungeon.hero.sprite.move(Dungeon.hero.pos, Dungeon.hero.pos - Dungeon.level.width());
            Dungeon.hero.move(Dungeon.hero.pos - Dungeon.level.width());
            Dungeon.hero.spend(1 / Dungeon.hero.speed());
            Dungeon.hero.busy();
            return true;
        }else if(Dungeon.hero.pos == pos - Dungeon.level.width()){
            sprite.move(pos, pos + Dungeon.level.width());
            move(pos + Dungeon.level.width());
            Dungeon.hero.sprite.move(Dungeon.hero.pos, Dungeon.hero.pos + Dungeon.level.width());
            Dungeon.hero.move(Dungeon.hero.pos + Dungeon.level.width());
            Dungeon.hero.spend(1 / Dungeon.hero.speed());
            Dungeon.hero.busy();
            return true;
        }else{
            super.interact();
        }
        return false;
    }
}
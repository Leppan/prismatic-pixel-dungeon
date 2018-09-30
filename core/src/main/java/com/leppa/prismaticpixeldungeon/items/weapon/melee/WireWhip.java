package com.leppa.prismaticpixeldungeon.items.weapon.melee;

import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;

public class WireWhip extends MeleeWeapon{

    {
        image = ItemSpriteSheet.WIREWHIP;

        tier = 5;
        RCH = 3;    //lots of extra reach
    }

    @Override
    public int max(int lvl) {
        return  4*(tier)-2  +   //18 base
                lvl*(tier-1);   //+4 per level
    }
}

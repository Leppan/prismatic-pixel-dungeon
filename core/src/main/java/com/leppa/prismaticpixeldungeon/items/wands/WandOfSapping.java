package com.leppa.prismaticpixeldungeon.items.wands;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.Burning;
import com.leppa.prismaticpixeldungeon.actors.buffs.Charm;
import com.leppa.prismaticpixeldungeon.actors.buffs.Healing;
import com.leppa.prismaticpixeldungeon.actors.buffs.Poison;
import com.leppa.prismaticpixeldungeon.actors.buffs.Weakness;
import com.leppa.prismaticpixeldungeon.items.weapon.melee.MagesStaff;
import com.leppa.prismaticpixeldungeon.mechanics.Ballistica;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;

public class WandOfSapping extends DamageWand {

    {
        image = ItemSpriteSheet.WAND_SAPPING;
    }

    @Override
    public int min(int lvl){
        return 2 + 2*lvl;
    }

    @Override
    public int max(int lvl){
        return 6 + 3*lvl;
    }

    protected int initialCharges() {
        return 3;
    }

    @Override
    protected void onZap(Ballistica attack){
        int cell = attack.collisionPos;

        Char ch = Actor.findChar(cell);
        if(ch != null){
            processSoulMark(ch, chargesPerCast());
            int damageAmount = damageRoll();
            ch.damage(damageAmount, this);
            Buff.affect(curUser, Healing.class).setHeal((int)(0.5*damageAmount), 0.333f, 0);

            ch.sprite.burst(0xFF9818FF, level() / 2 + 2);

            if (ch.properties().contains(Char.Property.UNDEAD)){
                Buff.affect(curUser, Weakness.class, Math.max(2, 7-level()));
            }else if(ch.properties().contains(Char.Property.ACIDIC)){
                Buff.affect(curUser, Poison.class).set(Math.max(2, 7-level()));
            }else if(ch.properties().contains(Char.Property.FIERY)){
                Buff.affect(curUser, Burning.class);
            }else{
                Buff.affect(curUser, Charm.class, Math.max(2, 7-level())).object = ch.id();
            }
        }else{
            Dungeon.level.press(attack.collisionPos, null, true);
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage){
        int damageAmount = (int)0.5*damageRoll();
        defender.damage(damageAmount, this);
        Buff.affect(curUser, Healing.class).setHeal(damageAmount, 0.333f, 0);
    }
}
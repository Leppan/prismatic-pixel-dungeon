package com.leppa.prismaticpixeldungeon.items.wands;

import com.leppa.prismaticpixeldungeon.Dungeon;
import com.leppa.prismaticpixeldungeon.actors.Actor;
import com.leppa.prismaticpixeldungeon.actors.Char;
import com.leppa.prismaticpixeldungeon.actors.blobs.Blob;
import com.leppa.prismaticpixeldungeon.actors.blobs.CorrosiveGas;
import com.leppa.prismaticpixeldungeon.actors.buffs.Buff;
import com.leppa.prismaticpixeldungeon.actors.buffs.FlavourBuff;
import com.leppa.prismaticpixeldungeon.effects.CellEmitter;
import com.leppa.prismaticpixeldungeon.effects.particles.CorrosionParticle;
import com.leppa.prismaticpixeldungeon.items.weapon.melee.MagesStaff;
import com.leppa.prismaticpixeldungeon.mechanics.Ballistica;
import com.leppa.prismaticpixeldungeon.messages.Messages;
import com.leppa.prismaticpixeldungeon.scenes.GameScene;
import com.leppa.prismaticpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfChanneling extends Wand{
    
    java.util.Random r = new java.util.Random();
    
    {
        image = ItemSpriteSheet.WAND_CHANNELING;
    }

    @Override
    protected void onZap(Ballistica attack) {
        int cell = attack.collisionPos;

        Char ch = Actor.findChar(cell);
        if(ch != null){
            processSoulMark(ch, chargesPerCast());

            ch.sprite.burst(0xFF561313, level() / 2 + 2);

            for(Buff buff : getMobEffectsBuffs()){
                if(buff instanceof FlavourBuff) Buff.affect(ch, ((FlavourBuff)buff).getClass(), 3+level());
                else Buff.affect(ch, buff.getClass());
            }
        }

        for(Blob blob : getAreaEffectsBlobs()){
            Blob blob1 = Blob.seed(cell, 50 + 10 * level(), blob.getClass());
            CellEmitter.center(cell).burst(CorrosionParticle.SPLASH, level() / 2 + 2);
            GameScene.add(blob1);
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        for(Buff buff : getMobEffectsBuffs()){
            if(buff instanceof FlavourBuff) Buff.affect(defender, ((FlavourBuff)buff).getClass(), 3+level());
            else Buff.affect(defender, buff.getClass());
        }
    }

    @Override
    public String statsDesc(){
        return Messages.get(this, "stats_desc", getAreaEffects(), getMobEffects());
    }

    String getAreaEffects(){
        int heroCell;
        String toDo = "";

        if (Dungeon.hero == null){
            heroCell = 0;
        }else{
            heroCell = Dungeon.hero.pos;
        }

        for (Blob blob:Dungeon.level.blobs.values()) {
            if(blob.volume > 0 && blob.cur[heroCell] > 0){
                if(toDo.length() == 0) toDo += "create ";
                toDo += Messages.get(blob, "name");
            }
        }

        if(toDo.length() == 0){
            return "do nothing";
        }

        return toDo;
    }

    ArrayList<Blob> getAreaEffectsBlobs(){
        int heroCell;
        ArrayList<Blob> blobs = new ArrayList<>();
        if (Dungeon.hero == null){
            heroCell = 0;
        }else{
            heroCell = Dungeon.hero.pos;
        }
        for (Blob blob:Dungeon.level.blobs.values()) {
            if(blob.volume > 0 && blob.cur[heroCell] > 0){
                blobs.add(blob);
            }
        }
        return blobs;
    }

    String getMobEffects(){
        if (Dungeon.hero == null) return "something or other";
        HashSet f = Dungeon.hero.buffs();
        ArrayList negativeBuffs = new ArrayList();
        for(Object b : f){
            if (b instanceof Buff && ((Buff) b).type == Buff.buffType.NEGATIVE) negativeBuffs.add(b);
        }
        if(negativeBuffs.size() == 0){
            return "do nothing";
        }else{
            String toDo = "";
            int effectCount = 0;
            for(Object x : negativeBuffs){
                effectCount++;
                toDo += Messages.get(x, "affect");
                if(effectCount != negativeBuffs.size()) toDo += ", ";
            }
            return toDo;
        }
    }

    ArrayList<Buff> getMobEffectsBuffs(){
        if (Dungeon.hero == null) return new ArrayList();
        HashSet f = Dungeon.hero.buffs();
        ArrayList negativeBuffs = new ArrayList();
        for(Object b : f){
            if (b instanceof Buff && ((Buff) b).type == Buff.buffType.NEGATIVE) negativeBuffs.add(b);
        }
        if(negativeBuffs.size() == 0){
            return new ArrayList();
        }else{
            ArrayList<Buff> buffs = new ArrayList<>();
            for(Object x : negativeBuffs){
                Buff negativeBuff = (Buff)x;
                buffs.add(negativeBuff);
            }
            return buffs;
        }
    }
    
    public void staffFx(MagesStaff.StaffParticle particle){
        particle.color(0x6d3030);
        particle.am = 0.6f;
        particle.setLifespan(3f);
        float amt = PointF.PI*0.25f + Random.Float(-PointF.PI/4, PointF.PI/4);
        particle.speed.polar(amt, 0.3f);
        particle.setSize(1f, 2f);
        particle.radiateXY(5f);
        
        particle.speed.polar(amt + PointF.PI, 4f);
    }
}
package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.init.InitStands;
import com.github.standobyte.jojo.action.stand.StandEntityActionModifier;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;
import net.minecraft.entity.Entity;

public class KraftWorkHeavyPunch extends StandEntityHeavyAttack {

    public KraftWorkHeavyPunch(Builder builder) {
        super(builder);
    }
    
    @Override
    protected StandEntityActionModifier getRecoveryFollowup(IStandPower standPower, StandEntity standEntity) {
        return InitStands.KRAFT_WORK_LOCK_ARMOR.get();
    }

    @Override
    public StandEntityPunch punchEntity(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
        double strength = stand.getAttackDamage();
        return super.punchEntity(stand, target, dmgSource)
                .addKnockback(0.5F + (float) (strength * 0.857142858) / (8 - stand.getLastHeavyFinisherValue() * 4));
    }
}

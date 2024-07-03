package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.init.InitStands;
import com.github.standobyte.jojo.action.stand.StandEntityActionModifier;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

public class KraftWorkHeavyPunch extends StandEntityHeavyAttack {

    public KraftWorkHeavyPunch(Builder builder) {
        super(builder);
    }
    
    @Override
    protected StandEntityActionModifier getRecoveryFollowup(IStandPower standPower, StandEntity standEntity) {
        return InitStands.KRAFT_WORK_LOCK_ARMOR.get();
    }
}

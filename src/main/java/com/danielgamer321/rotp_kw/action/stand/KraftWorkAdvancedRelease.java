package com.danielgamer321.rotp_kw.action.stand;

import com.danielgamer321.rotp_kw.client.ClientUtil;
import com.danielgamer321.rotp_kw.init.InitStands;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class KraftWorkAdvancedRelease extends StandAction {
    public KraftWorkAdvancedRelease(Builder builder) {
        super(builder);
    }

    @Override
    public StandAction[] getExtraUnlockable() {
        return new StandAction[] { InitStands.KRAFT_WORK_RELEASE_ALL_PROJECTILES.get(),
                InitStands.KRAFT_WORK_RELEASE_PROJECTILES_R.get(),
                InitStands.KRAFT_WORK_RELEASE_PROJECTILES_NR.get(),
                InitStands.KRAFT_WORK_RELEASE_ENDER_PEARL.get(),
                InitStands.KRAFT_WORK_RELEASE_BENEFICIAL.get() };
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (world.isClientSide()) {
            ClientUtil.openAdvancedReleaseUi();
        }
    }
}

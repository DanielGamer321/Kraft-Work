package com.danielgamer321.rotp_kw.init;

import com.danielgamer321.rotp_kw.entity.stand.stands.*;
import com.danielgamer321.rotp_kw.power.impl.stand.type.KraftWorkStandType;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject.EntityStandSupplier;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;

public class AddonStands {

    public static final EntityStandSupplier<KraftWorkStandType<StandStats>, StandEntityType<KraftWorkEntity>>
            KRAFT_WORK = new EntityStandSupplier<>(InitStands.STAND_KRAFT_WORK);
}

package com.danielgamer321.rotp_kw.action.stand;

import com.github.standobyte.jojo.action.stand.IStandPhasedAction;
import com.github.standobyte.jojo.action.stand.StandEntityActionModifier;

public abstract class KWActionModifier extends StandEntityActionModifier implements IStandPhasedAction {

    public KWActionModifier(AbstractBuilder<?> builder) {
        super(builder);
    }
    
    
    protected class TriggeredFlag {}
}

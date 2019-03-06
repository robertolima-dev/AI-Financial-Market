package com.crypfy.core.strategies.api;

import com.crypfy.core.entity.SnapshotTick;
import com.crypfy.core.entity.StrategyAssetsDistribution;
import java.util.List;

public interface Strategy {

    public StrategyAssetsDistribution getDistribution(List<SnapshotTick> ticks, int nAssets,double lowerLimit,double higherLimit);
}

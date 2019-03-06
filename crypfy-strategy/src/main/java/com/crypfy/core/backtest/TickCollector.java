package com.crypfy.core.backtest;

import com.crypfy.core.entity.SnapshotTick;
import com.crypfy.persistence.repository.HistoricalCoinSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TickCollector {

    @Autowired
    HistoricalCoinSnapshotRepository repository;

    public List<SnapshotTick> getTicks(int nMonths){

        //instances
        List<SnapshotTick> ticks = new ArrayList<SnapshotTick>();
        //4 ticks for initial calcs
        //nMonths = nMonths -4;

        //collect
        for(int weak=31-nMonths;weak<31;weak++){
            SnapshotTick tick = new SnapshotTick();
            tick.setWeakId(weak);
            tick.setSnapshots(repository.findByWeakId(weak));
            tick.setDate(tick.getSnapshots().get(0).getDate());
            //add
            ticks.add(tick);
        }

        return ticks;
    }

    public List<SnapshotTick> getTicksForCurrentPort(){
        //instances
        List<SnapshotTick> ticks = new ArrayList<SnapshotTick>();
        //collect
        for(int weak=25;weak<30;weak++){
            SnapshotTick tick = new SnapshotTick();
            tick.setWeakId(weak);
            tick.setSnapshots(repository.findByWeakId(weak));
            tick.setDate(tick.getSnapshots().get(0).getDate());
            //add
            ticks.add(tick);
        }
        Collections.reverse(ticks);
        return ticks;
    }

}

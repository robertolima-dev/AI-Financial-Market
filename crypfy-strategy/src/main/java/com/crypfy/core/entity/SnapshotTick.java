package com.crypfy.core.entity;

import com.crypfy.persistence.entity.HistoricalCoinSnapshot;
import java.util.List;

public class SnapshotTick {

    private int weakId;
    private String date;
    private List<HistoricalCoinSnapshot> snapshots;

    public int getWeakId() {
        return weakId;
    }

    public void setWeakId(int weakId) {
        this.weakId = weakId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<HistoricalCoinSnapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<HistoricalCoinSnapshot> snapshots) {
        this.snapshots = snapshots;
    }
}

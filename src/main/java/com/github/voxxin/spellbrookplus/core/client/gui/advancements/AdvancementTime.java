package com.github.voxxin.spellbrookplus.core.client.gui.advancements;

public class AdvancementTime {

    private final long inTime;
    private final long stayTime;
    private final long outTime;

    public AdvancementTime(long inTime, long stayTime, long outTime) {
        this.inTime = inTime;
        this.stayTime = stayTime;
        this.outTime = outTime;
    }

    public long getInTime() {
        return this.inTime;
    }
    public long getStayTime() {
        return stayTime;
    }
    public long getOutTime() {
        return outTime;
    }
}

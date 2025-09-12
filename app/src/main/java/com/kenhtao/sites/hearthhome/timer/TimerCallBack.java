package com.kenhtao.sites.hearthhome.timer;

public interface TimerCallBack {
    void onTimerSet(long durationMillis);
    void onTimerCancelled();
    default void onTimerFinished() {}

}

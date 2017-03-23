package com.fifed.viewpager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Fedir on 18.03.2017.
 */

class AutoScrollThread extends Thread {
    private OnNextPageInformer informer;
    private  AtomicBoolean stoped = new AtomicBoolean();
    private AtomicLong scrollingTime = new AtomicLong();
    private long intervalMS;

    AutoScrollThread(long intervalMS) {
        this.intervalMS = intervalMS;
        setDaemon(true);
        scrollingTime.set(System.currentTimeMillis() + intervalMS);
        start();
    }

    @Override
    public void run() {
        while (!stoped.get()){
            if(System.currentTimeMillis() >= scrollingTime.get()){
                if(informer!= null) {
                    informer.goToNextPage();
                }
                onChangedPage();
                try {
                    Thread.sleep(intervalMS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void onPause(){
        scrollingTime.set(Long.MAX_VALUE);
    }

    void onResume(){
        scrollingTime.set(System.currentTimeMillis() + intervalMS);
    }

    void onStop (){
        stoped.set(true);
    }

    void onTouch(){
        scrollingTime.set(System.currentTimeMillis() + intervalMS);
    }

    private void onChangedPage(){
        scrollingTime.set(System.currentTimeMillis() + intervalMS);
    }

    void setOnNextPageInformer(OnNextPageInformer informer){
        this.informer = informer;
    }

    interface OnNextPageInformer{
        void goToNextPage();
    }
}

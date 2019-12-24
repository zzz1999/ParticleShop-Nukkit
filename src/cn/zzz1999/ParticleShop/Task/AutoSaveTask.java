package cn.zzz1999.ParticleShop.Task;


import cn.zzz1999.ParticleShop.ParticleShop;


public class AutoSaveTask implements Runnable {

    @Override
    public void run() {
        ParticleShop.getInstance().getProvider().save();
    }

}

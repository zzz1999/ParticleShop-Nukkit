package cn.zzz1999.ParticleShop.Task;

import cn.nukkit.utils.Config;
import cn.zzz1999.ParticleShop.ParticleShop;

import java.io.File;

public class AutoSaveTask implements Runnable{

    @Override
    public void run() {
        ParticleShop.getInstance().getProvider().save();
    }

}

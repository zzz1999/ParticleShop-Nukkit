package cn.zzz1999.ParticleShop.Task;


import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockNetherPortal;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.*;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.zzz1999.ParticleShop.ParticleShop;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ParticleTask implements Runnable {
    private ParticleShop plugin;

    public ParticleTask (ParticleShop plugin){
        this.plugin = plugin ;
    }
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                ParticleShop.getInstance().getFollowList().forEach((String key, Integer value) ->{
                    Player p = ParticleShop.getInstance().getServer().getPlayer(key);
                    if(p != null){
                        p.getLevel().addParticle(getParticle(value,p.add(nextDouble(-1-1,1+1),0.2,nextDouble(-1-1,1+1))));
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static double nextDouble(final double min, final double max){
        if (min == max) {
            return min;
        }
        return min + ((max - min) * new Random().nextDouble());
    }
    public Particle getParticle(Integer type, Location pos){
        switch(type) {
            case 0:
                return new AngryVillagerParticle(pos);

            case 1:
                return new BlockForceFieldParticle(pos);

            case 2:
                return new BoneMealParticle(pos);

            case 3:
                return new BubbleParticle(pos);

            case 4:
                return new CriticalParticle(pos);

            case 5:
                return new DestroyBlockParticle(pos, Block.get(247, (int) (Math.random() * 3)));

            case 6:
                return new DustParticle(pos, 104, 204, 255);

            case 7:
                return new EnchantParticle(pos);

            case 8:
                return new EnchantmentTableParticle(pos);

            case 9:
                return new EntityFlameParticle(pos);

            case 10:
                return new ExplodeParticle(pos);

            case 11:
                return new FlameParticle(pos);

            case 12:
                return new GenericParticle(pos, (int) (Math.random() * 3));

            case 13:
                return new HappyVillagerParticle(pos);

            case 14:
                return new HeartParticle(pos);

            case 15:
                return new HugeExplodeParticle(pos);

            case 16:
                return new HugeExplodeSeedParticle(pos);

            case 17:
                return new InkParticle(pos);

            case 18:
                return new InstantEnchantParticle(pos);

            case 19:
                return new InstantSpellParticle(pos);

            case 20:
                return new ItemBreakParticle(pos, Item.get((int) (Math.random() * 91)));

            case 21:
                return new LavaDripParticle(pos);

            case 22:
                return new LavaParticle(pos);

            case 23:
                return new MobSpawnParticle(pos, 1, 2);

            case 24:
                return new PortalParticle(pos);

            case 25:
                return new PunchBlockParticle(pos, Block.get(247), BlockFace.DOWN);

            case 26:
                return new RainSplashParticle(pos);

            case 27:
                return new RedstoneParticle(pos);

            case 28:
                return new SmokeParticle(pos);

            case 29:
                return new SpellParticle(pos);

            case 30:
                return new SplashParticle(pos);

            case 31:
                return new SporeParticle(pos);

            case 32:
                return new TerrainParticle(pos, Block.get(247));

            case 33:
                return new WaterDripParticle(pos);

            case 34:
                return new WaterParticle(pos);

        }
        return new SmokeParticle(pos,6);
    }
}

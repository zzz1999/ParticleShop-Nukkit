package cn.zzz1999.ParticleShop;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.zzz1999.ParticleShop.Command.ParticleCommand;
import cn.zzz1999.ParticleShop.Provider.Provider;
import cn.zzz1999.ParticleShop.Provider.YamlProvider;
import cn.zzz1999.ParticleShop.Task.ParticleTask;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParticleShop extends PluginBase implements Listener {
    private static ParticleShop instance;
    private Map<String,ShopInstance> shops = new HashMap<>();
    private Provider provider;
    private Map<String, Integer> follow = new HashMap<>();

    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getCommandMap().register("ps", new ParticleCommand());
        ParticleTask task = new ParticleTask(this);
        new Thread(task, "ParticleShop Thread-01").start();
        new Thread(task, "ParticleShop Thread-02").start();
        new File(this.getDataFolder() + File.separator + "Players").mkdirs();
        this.provider = new YamlProvider(this);

        this.loadShops();
    }
    @SuppressWarnings("unchecked")
    public void loadShops(){
        Config c = new Config(new File(this.getDataFolder(),"Shops.yml"),Config.YAML);
        c.getAll().forEach((key,value)->{
            Map<String,Object> map = (Map<String,Object>)value;
            this.shops.put(key,new ShopInstance((Integer)map.get("X"),(Integer)map.get("Y"),(Integer)map.get("Z"),(String)map.get("Level"),(Integer)map.get("ParticleType"),(Double)map.get("Price")));
        });
        this.getLogger().info(TextFormat.GREEN+"已经加载了"+this.shops.size()+"个粒子商店");
    }

    public void onDisable(){
        getProvider().save();
    }
    public static ParticleShop getInstance(){
        return instance;
    }

    public Map<String,ShopInstance> getShops(){
        return this.shops;
    }
    public Map<String,Integer> getFollowList(){
        return follow;
    }
    public void setShops(Map<String,ShopInstance> map){this.shops = map;}
    public Provider getProvider(){return this.provider;}
    public Config getPlayerConfig(String player){
        return new Config(new File(this.getDataFolder()+File.separator+"Players",player+".yml"),Config.YAML);
    }
    public Config getPlayerConfig(Player player){
        return new Config(new File(this.getDataFolder()+File.separator+"Players",player.getName()+".yml"),Config.YAML);
    }
    public String getParticleName(Integer index){
        if(index >=0 && index < ParticleInfo.values().length){
            return ParticleInfo.values()[index].toString();
        }
        return null;
    }
    public Integer getParticleType(String type){
        Integer result = null;
        try{
            result = ParticleInfo.valueOf(type).ordinal();
        }catch(Exception ignore){

        }
        return result;
    }




}
enum ParticleInfo{
    AngryVillagerParticle,
    BlockForceFieldParticle,
    BoneMealParticle,
    BubbleParticle,
    CriticalParticle,
    DestroyBlockParticle,
    DustParticle,
    EnchantParticle,
    EnchantmentTableParticle,
    EntityFlameParticle,
    ExplodeParticle,
    FlameParticle,
    GenericParticle,
    HappyVillagerParticle,
    HeartParticle,
    HugeExplodeParticle,
    HugeExplodeSeedParticle,
    InkParticle,
    InstantEnchantParticle,
    InstantSpellParticle,
    ItemBreakParticle,
    LavaDripParticle,
    LavaParticle,
    MobSpawnParticle,
    PortalParticle,
    PunchBlockParticle,
    RainSplashParticle,
    RedstoneParticle,
    SmokeParticle,
    SpellParticle,
    SplashParticle,
    SporeParticle,
    TerrainParticle,
    WaterDripParticle,
    WaterParticle

}

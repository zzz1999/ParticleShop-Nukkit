package cn.zzz1999.ParticleShop;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.zzz1999.ParticleShop.Command.ParticleCommand;
import cn.zzz1999.ParticleShop.Language.TextTranslation;
import cn.zzz1999.ParticleShop.Provider.Provider;
import cn.zzz1999.ParticleShop.Provider.YamlProvider;
import cn.zzz1999.ParticleShop.Task.ParticleTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;


enum ParticleInfo {
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

/**
 * 某些语言文件是由Google 翻译而来 不保证翻译的准确性，我英语也不怎么好2333
 * 如果翻译错了还请大佬不要打我
 */

public class ParticleShop extends PluginBase implements Listener {
    private static ParticleShop instance;
    private Map<String, ShopInstance> shops = new HashMap<>();
    private Provider provider;
    private Map<String, Integer> follow = new HashMap<>();
    private String defaultLang = "zh_CN";
    private Map<String, String> fallbackLang = new HashMap<>();

    public static ParticleShop getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getCommandMap().register("ps", new ParticleCommand());
        this.loadLanguage();
        this.getLogger().info(new TextTranslation("logger.lang.choose", this.defaultLang).toString());
        this.loadShops();
        ParticleTask task = new ParticleTask();
        new Thread(task, "ParticleShop Thread-01").start();
        new Thread(task, "ParticleShop Thread-02").start();
        new File(this.getDataFolder() + File.separator + "Players").mkdirs();
        this.provider = new YamlProvider(this);
    }

    private void loadLanguage() {
        File folder = new File(this.getDataFolder() + File.separator + "resources" + File.separator + "lang");
        List list = Arrays.asList(folder.listFiles());
        String loc = Locale.getDefault().toString();
        if (list.contains(new File(this.getDataFolder() + File.separator + "resources" + File.separator + "lang" + File.separator + loc))) {
            this.defaultLang = loc;
        } else {
            this.getLogger().info("暂无你当前所在地区的语言资源文件,正在启动默认语言文件:[zh_CN]");
            this.getLogger().info("No language file for your current location, is starting the default language file:[zh_CN]");
        }
        try {
            FileInputStream file = new FileInputStream(new File(folder.getPath() + File.separator + this.defaultLang + File.separator + "lang.ini"));
            BufferedReader read = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
            String line;
            while ((line = read.readLine()) != null) {
                if (!line.matches("")) {
                    String[] par = line.split("=");
                    fallbackLang.put(par[0], par[1]);
                }
            }
            read.close();
        } catch (Exception e) {
            this.getLogger().info("语言文件加载发生错误");
            this.getLogger().info("There was an error happen when loading the language file");
            this.getLogger().info("插件已自动关闭");
            this.getLogger().info("This plugin has been automatically closed");
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    public String getLanguage() {
        return defaultLang;
    }

    public Map<String, String> getFallbackLang() {
        return fallbackLang;
    }

    @SuppressWarnings("unchecked")
    private void loadShops() {
        Config c = new Config(new File(this.getDataFolder(), "Shops.yml"), Config.YAML);
        c.getAll().forEach((key, value) -> {
            Map<String, Object> map = (Map<String, Object>) value;
            this.shops.put(key, new ShopInstance((Integer) map.get("X"), (Integer) map.get("Y"), (Integer) map.get("Z"), (String) map.get("Level"), (Integer) map.get("ParticleType"), (Double) map.get("Price")));
        });
        this.getLogger().info(new TextTranslation("logger.status", String.valueOf(this.shops.size())).toString());
    }

    @Override
    public void onDisable() {
        getProvider().save();
    }

    public Map<String, ShopInstance> getShops() {
        return this.shops;
    }

    public void setShops(Map<String, ShopInstance> map) {
        this.shops = map;
    }

    public Map<String, Integer> getFollowList() {
        return follow;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public Config getPlayerConfig(String player) {
        return new Config(new File(this.getDataFolder() + File.separator + "Players", player + ".yml"), Config.YAML);
    }

    public Config getPlayerConfig(Player player) {
        return new Config(new File(this.getDataFolder() + File.separator + "Players", player.getName() + ".yml"), Config.YAML);
    }

    public String getParticleName(Integer index) {
        if (index >= 0 && index < ParticleInfo.values().length) {
            return ParticleInfo.values()[index].toString();
        }
        return null;
    }

    public int getParticleType(String type) {
        int result = -1;
        try {
            result = ParticleInfo.valueOf(type).ordinal();
        } catch (Exception ignore) {

        }
        return result;
    }
}

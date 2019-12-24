package cn.zzz1999.ParticleShop.Provider;


import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import cn.zzz1999.ParticleShop.ParticleShop;
import cn.zzz1999.ParticleShop.ShopInstance;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlProvider implements Provider {
    private Config shop;

    public YamlProvider(ParticleShop plugin) {
        //for shop.yml alter
        this.shop = new Config(new File(plugin.getDataFolder(), "Shops.yml"), Config.YAML);
    }

    @Override
    public Boolean addShop(int x, int y, int z, Level level, int ParticleType, double price) {
        String key = x + "," + y + "," + z + "," + level.getName();
        if (!this.shop.exists(key)) {
            this.shop.set(key, new LinkedHashMap<String, Object>() {{
                put("X", x);
                put("Y", y);
                put("Z", z);
                put("Level", level.getFolderName());
                put("ParticleType", ParticleType);
                put("Price", price);
            }});
            return true;
        }
        return false;
    }

    @Override
    public Boolean addShop(int x, int y, int z, String levelname, int ParticleType, double price) {
        String key = x + "," + y + "," + z + "," + levelname;
        if (!this.shop.exists(key)) {
            this.shop.set(key, new LinkedHashMap<String, Object>() {{
                put("X", x);
                put("Y", y);
                put("Z", z);
                put("Level", levelname);
                put("ParticleType", ParticleType);
                put("Price", price);
            }});
            return true;
        }
        return false;
    }

    @Override
    public Boolean addShop(Position pos, int ParticleType, double price) {
        String key = pos.getX() + "," + pos.getY() + "," + pos.getZ() + "," + pos.getLevel().getFolderName();
        if (!this.shop.exists(key)) {
            this.shop.set(key, new LinkedHashMap<String, Object>() {{
                put("X", pos.getX());
                put("Y", pos.getY());
                put("Z", pos.getZ());
                put("Level", pos.getLevel().getName());
                put("ParticleType", ParticleType);
                put("Price", price);
            }});
            return true;
        }
        return false;
    }

    @Override
    public Boolean removeShop(int x, int y, int z, Level level) {
        String key = x + "," + y + "," + z + "," + level.getFolderName();
        if (this.shop.exists(key)) {
            this.shop.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public Boolean removeShop(Position pos) {
        String key = pos.getX() + "," + pos.getY() + "," + pos.getZ() + "," + pos.getLevel().getFolderName();
        if (this.shop.exists(key)) {
            this.shop.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public ShopInstance getShop(int x, int y, int z, Level level) {
        String key = x + "," + y + "," + z + "," + level.getFolderName();
        Map<String, ShopInstance> map = ParticleShop.getInstance().getShops();
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    @Override
    public ShopInstance getShop(Position pos) {
        String key = pos.getX() + "," + pos.getY() + "," + pos.getZ() + "," + pos.getLevel().getFolderName();
        Map<String, ShopInstance> map = ParticleShop.getInstance().getShops();
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    @Override
    public Map<String, Object> getAll() {
        return this.shop.getAll();
    }


    @Override
    public void setAll(LinkedHashMap<String, Object> map) {
        this.shop.setAll(map);
    }

    @Override
    public void save() {
        this.shop.save();
    }

    @Override
    public void close() {
        this.shop.save();
    }
}
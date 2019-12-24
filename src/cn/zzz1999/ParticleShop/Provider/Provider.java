package cn.zzz1999.ParticleShop.Provider;


import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.zzz1999.ParticleShop.ShopInstance;

import java.util.LinkedHashMap;
import java.util.Map;

public interface Provider {

    Boolean addShop(int x, int y, int z, Level level, int ParticleType, double price);

    Boolean addShop(int x, int y, int z, String levelname, int ParticleType, double price);

    Boolean addShop(Position pos, int ParticleType, double price);

    Boolean removeShop(int x, int y, int z, Level level);

    Boolean removeShop(Position pos);


    ShopInstance getShop(int x, int y, int z, Level level);

    ShopInstance getShop(Position pos);

    Map<String, Object> getAll();

    void setAll(LinkedHashMap<String, Object> map);

    void save();

    void close();


}
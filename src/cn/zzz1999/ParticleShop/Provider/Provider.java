package cn.zzz1999.ParticleShop.Provider;


import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.zzz1999.ParticleShop.ShopInstance;

import java.util.LinkedHashMap;
import java.util.Map;

public interface Provider{


    public Boolean addShop(Integer x,Integer y,Integer z, Level level ,int ParticleType ,Double price);
    public Boolean addShop(Integer x, Integer y, Integer z, String levelname , int ParticleType , Double price);
    public Boolean addShop(Position pos ,int ParticleType ,Double price);

    public Boolean removeShop(Integer x,Integer y,Integer z, Level level);
    public Boolean removeShop(Position pos);


    public ShopInstance getShop(Integer x,Integer y,Integer z,Level level);
    public ShopInstance getShop(Position pos);

    public Map<String, Object> getAll();

    void setAll(LinkedHashMap<String, Object> map);

    public void save();
    public void close();



}
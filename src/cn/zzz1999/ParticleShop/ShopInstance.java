package cn.zzz1999.ParticleShop;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.zzz1999.ParticleShop.Task.ParticleTask;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShopInstance {

    private Integer x;
    private Integer y;
    private Integer z;
    private Level level;
    private Integer ParticleType;
    private Double price;

    public ShopInstance(Integer x,Integer y,Integer z,String level ,Integer particleType,Double price){
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = ParticleShop.getInstance().getServer().getLevelByName(level);
        this.ParticleType = particleType;
        this.price = price;
    }
    public ShopInstance(Integer x,Integer y,Integer z,Level level ,Integer particleType,Double price){
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = level;
        this.ParticleType = particleType;
        this.price = price;
    }


    public Integer getX() {
        return x;
    }
    public Integer getY(){
        return y;
    }
    public Integer getZ(){
        return z;
    }
    public Level getLevel(){
        return level;
    }
    public Integer getParticleType(){
        return ParticleType;
    }
    public Double getPrice(){
        return price;
    }
    public Position getPosition(){
        return new Position(x,y,z,level);
    }

    public Map<String,Object> toMap(){
        return new LinkedHashMap<String,Object>(){{
            put("X",getX());
            put("Y",getY());
            put("Z",getZ());
            put("Level",getLevel());
            put("ParticleType",getParticleType());
            put("Price",getPrice());
        }};
    }
    public String toString(){
        return getX() + "," + getY() + "," + getZ() + "," + getLevel().getFolderName();
    }



}

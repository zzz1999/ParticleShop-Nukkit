package cn.zzz1999.ParticleShop;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShopInstance {

    private int x;
    private int y;
    private int z;
    private Level level;
    private int ParticleType;
    private double price;

    ShopInstance(int x, int y, int z, String level, int particleType, double price) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = ParticleShop.getInstance().getServer().getLevelByName(level);
        this.ParticleType = particleType;
        this.price = price;
    }

    ShopInstance(int x, int y, int z, Level level, int particleType, double price) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = level;
        this.ParticleType = particleType;
        this.price = price;
    }


    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    private int getZ() {
        return z;
    }

    private Level getLevel() {
        return level;
    }

    int getParticleType() {
        return ParticleType;
    }

    double getPrice() {
        return price;
    }

    public Position getPosition() {
        return new Position(x, y, z, level);
    }

    public Map<String, Object> toMap() {
        return new LinkedHashMap<String, Object>() {{
            put("X", getX());
            put("Y", getY());
            put("Z", getZ());
            put("Level", getLevel());
            put("ParticleType", getParticleType());
            put("Price", getPrice());
        }};
    }

    public String toString() {
        return getX() + "," + getY() + "," + getZ() + "," + getLevel().getFolderName();
    }


}

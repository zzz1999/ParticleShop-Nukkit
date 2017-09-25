package cn.zzz1999.ParticleShop;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import cn.zzz1999.ParticleShop.Language.TextTranslation;
import me.onebone.economyapi.EconomyAPI;

import java.util.*;
import java.util.regex.*;


public class EventListener implements Listener {
    private Map<String , Long> taps = new HashMap<>();//检测两次快速点击

    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
    /**
     * Line 0 : ParticleShop
     * Line 1 : ParticleType
     * Line 2 : Price
     * Line 3 : extract Data
     */
    @EventHandler(priority = EventPriority.LOWEST , ignoreCancelled = true)
    public void onShopCreate(SignChangeEvent event){
        if(event.getPlayer().isOp()){
            Player p = event.getPlayer();
            if(!Objects.equals(null,event.getLine(0))){
                if(new ArrayList<String>(){{
                    add("ps");add("particleshop");
                }}.contains(event.getLine(0).toLowerCase())){
                    Integer type =null;
                    Boolean b;
                    String line = event.getLine(1);

                    if(isNumeric(line)){
                        type = Integer.parseInt(line);
                        if (type >= 0 && type < ParticleInfo.values().length) {
                            b = true;
                        } else {
                            p.sendPopup(new TextTranslation("shop.type.error",String.valueOf(type)).toString());
                            b = false;
                        }

                    }else {
                        try {
                            type = ParticleInfo.valueOf(line).ordinal();
                            b = true;
                        } catch (IllegalArgumentException ex) {
                            p.sendPopup(new TextTranslation("shop.name.error",String.valueOf(line)).toString());
                            b = false;
                        }
                    }

                    Double price = null;
                    Boolean a;
                    if(isNumeric(event.getLine(2))){
                        price = Double.parseDouble(event.getLine(2));
                        a =true;
                    }else{
                        p.sendPopup(new TextTranslation("shop.price.error").toString());
                        a = false;
                    }

                    if(a && b){
                        Block block = event.getBlock();
                        Map<String,ShopInstance> map = ParticleShop.getInstance().getShops();
                        map.put(block.getX()+","+block.getY()+","+block.getZ()+","+block.getLevel().getFolderName(),new ShopInstance(block.getFloorX(),block.getFloorY(),block.getFloorZ(),block.getLevel(),type,price));
                        ParticleShop.getInstance().getProvider().addShop(block.getFloorX(),block.getFloorY(),block.getFloorZ(),block.getLevel(),type,price);
                        p.sendPopup(new TextTranslation("shop.create.succeed").toString());
                        event.setLine(0, new TextTranslation("shop.format.one").toString());
                        event.setLine(1, new TextTranslation("shop.format.two",ParticleShop.getInstance().getParticleName(type)).toString());
                        event.setLine(2, new TextTranslation("shop.format.three",String.valueOf(price)).toString());
                        event.setLine(3, new TextTranslation("shop.format.four").toString());

                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event){
        Block b = event.getBlock();
        String key = b.getX()+","+b.getY()+","+b.getZ()+","+b.getLevel().getFolderName();
        if(ParticleShop.getInstance().getShops().containsKey(key)) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled();
                event.getPlayer().sendMessage(new TextTranslation("permission.break.no").toString());
            } else {
                ParticleShop.getInstance().getProvider().removeShop(event.getBlock());
                ParticleShop.getInstance().getShops().remove(key);
                event.getPlayer().sendMessage(new TextTranslation("permission.break.true").toString());

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    @SuppressWarnings("unchecked")
    public void onClick(PlayerInteractEvent event){
        if(new ArrayList<Integer>(){
            {
                add(Item.SIGN);
                add(Item.SIGN_POST);
                add(Item.WALL_SIGN);
            }
        }.contains(event.getBlock().getId())){
            Player player  = event.getPlayer();
            Block block = event.getBlock();
            String key = block.getX()+","+block.getY()+","+block.getZ()+","+block.getLevel().getFolderName();
            if(ParticleShop.getInstance().getShops().containsKey(key)){
                ShopInstance shop  = ParticleShop.getInstance().getShops().get(key);
                if(!taps.containsKey(player.getName()) || taps.get(player.getName()) + 3000L < new Date().getTime()){
                    taps.put(player.getName(),new Date().getTime());
                    player.sendMessage(new TextTranslation("particle.buy.double").toString());
                    return ;
                }
                if(EconomyAPI.getInstance().myMoney(event.getPlayer()) < shop.getPrice()){
                    player.sendMessage(new TextTranslation("particle.buy.nomoney",EconomyAPI.getInstance().getMonetaryUnit()+shop.getPrice(),String.valueOf(shop.getPrice() - EconomyAPI.getInstance().myMoney(player))).toString());

                }else{
                    Config config = ParticleShop.getInstance().getPlayerConfig(player);
                    if(!config.exists("Particles")){
                        ArrayList<Integer> particles = new ArrayList<>();
                        particles.add(shop.getParticleType());
                        EconomyAPI.getInstance().reduceMoney(player,shop.getPrice());
                        config.set("Particles", particles);
                        config.set("Presence",null);
                        config.save();
                        player.sendMessage(new TextTranslation("particle.buy.succeed").toString());
                    }else{
                        ArrayList<Integer> particles = (ArrayList<Integer>) config.get("Particles");
                        if(particles.contains(shop.getParticleType())){
                            player.sendMessage(new TextTranslation("particle.buy.exist").toString());

                        }else{
                            particles.add(shop.getParticleType());
                            config.set("Particles", particles);
                            config.save();
                            EconomyAPI.getInstance().reduceMoney(player,shop.getPrice());
                            player.sendMessage(new TextTranslation("particle.buy.succeed").toString());
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST , ignoreCancelled = false)
    public void putFollowList(PlayerJoinEvent event){
        if(!ParticleShop.getInstance().getFollowList().containsKey(event.getPlayer().getName())){
            Player p = event.getPlayer();
            Config c = ParticleShop.getInstance().getPlayerConfig(p);
            if(c.get("Presence") !=null) {
                ParticleShop.getInstance().getFollowList().put(p.getName(), c.getInt("Presence"));
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST , ignoreCancelled = false)
    public void removeFollowList(PlayerQuitEvent event){
        if(ParticleShop.getInstance().getFollowList().containsKey(event.getPlayer().getName())){
            ParticleShop.getInstance().getFollowList().remove(event.getPlayer().getName());
        }
    }
}

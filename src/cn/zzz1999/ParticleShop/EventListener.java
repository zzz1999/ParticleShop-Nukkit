package cn.zzz1999.ParticleShop;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Event;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
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
                                    p.sendPopup(TextFormat.RED+"[ParticleShop] 不存在序号为" + type + "的粒子");
                                    b = false;
                                }

                            }else {
                                try {
                                    type = ParticleInfo.valueOf(line).ordinal();
                                    b = true;
                                } catch (IllegalArgumentException ex) {
                                    p.sendPopup(TextFormat.RED+"[ParticleShop] 不存在名字为" + line + "的粒子");
                                    b = false;
                                }
                            }

                    Double price = null;
                    Boolean a;
                    if(isNumeric(event.getLine(2))){
                        price = Double.parseDouble(event.getLine(2));
                        a =true;
                    }else{
                        p.sendPopup(TextFormat.RED+"[ParticleShop] 第三行输入错误");
                        a = false;
                    }

                    if(a && b){
                        Block block = event.getBlock();
                        Map<String,ShopInstance> map = ParticleShop.getInstance().getShops();
                        map.put(block.getX()+","+block.getY()+","+block.getZ()+","+block.getLevel().getFolderName(),new ShopInstance(block.getFloorX(),block.getFloorY(),block.getFloorZ(),block.getLevel(),type,price));
                        ParticleShop.getInstance().getProvider().addShop(block.getFloorX(),block.getFloorY(),block.getFloorZ(),block.getLevel(),type,price);
                        p.sendPopup(TextFormat.GOLD+"[ParticleShop] 粒子商店创建成功");
                        event.setLine(0, TextFormat.BOLD+""+TextFormat.LIGHT_PURPLE+"[ ParticleShop ]");
                        event.setLine(1, TextFormat.WHITE+"名字:"+ParticleShop.getInstance().getParticleName(type));
                        event.setLine(2, TextFormat.YELLOW+"价格:"+price);
                        event.setLine(3, TextFormat.AQUA+"双击购买粒子");
                    }


                }
            }
        }
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event){
        Block b = event.getBlock();
        String key = b.getX()+","+b.getY()+","+b.getZ()+","+b.getLevel().getFolderName();
        if(ParticleShop.getInstance().getShops().containsKey(key)) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled();
                event.getPlayer().sendMessage(TextFormat.RED+"[ParticleShop] 你没有权限破坏粒子商店");
            } else {
                ParticleShop.getInstance().getProvider().removeShop(event.getBlock());
                ParticleShop.getInstance().getShops().remove(key);
                event.getPlayer().sendMessage(TextFormat.GREEN+"[ParticleShop] 你以成功破坏粒子商店");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
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
                if(!taps.containsKey(player.getName()) || taps.get(player.getName()) + 500L < new Date().getTime()){
                    taps.put(player.getName(),new Date().getTime());
                    player.sendMessage(TextFormat.YELLOW+"[ParticleShop] 你确定需要购买吗,请再次点击确认");
                    return ;
                }
                if(EconomyAPI.getInstance().myMoney(event.getPlayer()) < shop.getPrice()){
                    player.sendMessage(TextFormat.GRAY+"[ParticleShop] 你没有足够的钱去购买这个粒子,购买这个粒子需要"+EconomyAPI.getInstance().getMonetaryUnit()+shop.getPrice()+".你还差"+String.valueOf(shop.getPrice() - EconomyAPI.getInstance().myMoney(player)));

                }else{
                    Config config = ParticleShop.getInstance().getPlayerConfig(player);
                    if(!config.exists("Particles")){
                        ArrayList<Integer> particles = new ArrayList<>();
                        particles.add(shop.getParticleType());
                        EconomyAPI.getInstance().reduceMoney(player,shop.getPrice());
                        config.set("Particles", particles);
                        config.set("Presence",null);
                        config.save();

                        player.sendMessage(TextFormat.GOLD+"[ParticleShop] 你以成功购买这个粒子");
                    }else{
                        ArrayList<Integer> particles = (ArrayList<Integer>) config.get("Particles");
                        if(particles.contains(shop.getParticleType())){
                            player.sendMessage(TextFormat.GRAY+"[ParticleShop] 你已经拥有了这个粒子无法重复购买");

                        }else{
                            particles.add(shop.getParticleType());
                            config.set("Particles", particles);
                            config.save();
                            EconomyAPI.getInstance().reduceMoney(player,shop.getPrice());
                            player.sendMessage(TextFormat.GOLD+"[ParticleShop] 购买成功");
                        }

                    }

                }
            }

        }
    }
}

package cn.zzz1999.ParticleShop.Command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.zzz1999.ParticleShop.ParticleShop;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParticleCommand extends Command {
    public ParticleCommand() {
        super("ps", "ParticleShop Command", "ps help", new String[]{"particleshop"});
        this.setPermission("ParticleShop.command.ps");
        this.commandParameters.clear();

    }
    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(CommandSender sender, String label, String[] args) {
        switch(args[0]){
            case "help":
                sendUsage(sender);
                break;
            case "set":
                if(args.length >=2) {
                    if(isNumeric(args[1])) {
                        Config c = ParticleShop.getInstance().getPlayerConfig(sender.getName());
                        ArrayList<Integer> arr = (ArrayList<Integer>) c.get("Particles");
                        if (!Objects.equals(null, arr)) {
                            if (arr.contains(Integer.parseInt(args[1]))) {
                                c.set("Presence", Integer.parseInt(args[1]));
                                c.save();
                                ParticleShop.getInstance().getFollowList().put(sender.getName(),Integer.parseInt(args[1]));
                                sender.sendMessage(TextFormat.GOLD+"[ParticleShop] 设置成功，你目前使用的粒子为: " + ParticleShop.getInstance().getParticleName(Integer.parseInt(args[1])));
                            } else {
                                sender.sendMessage(TextFormat.RED+"[ParticleShop] 你没有拥有 (" + Integer.parseInt(args[1]) + ") " + ParticleShop.getInstance().getParticleName(Integer.parseInt(args[1])) + " 粒子，请购买后重试");
                            }
                        } else {
                            sender.sendMessage(TextFormat.RED+"[ParticleShop] 你尚未拥有可用粒子，请购买后重试");
                        }
                    }else{
                        sender.sendMessage(TextFormat.GRAY+"[ParticleShop] /ps set <粒子编号>    | 设置你目前的跟随粒子");
                    }
                }else{
                    sender.sendMessage(TextFormat.RED+"[ParticleShop] 格式有误清重新输入");
                }

                break;
            case "list":
                Config config = ParticleShop.getInstance().getPlayerConfig(sender.getName());
                ArrayList<Integer> list = (ArrayList<Integer>) config.get("Particles");
                if(!Objects.equals(null,list)){
                    sender.sendMessage(TextFormat.AQUA+"--------你目前拥有的粒子有--------");
                    list.forEach((a)->{
                        sender.sendMessage("- ("+a+") "+ParticleShop.getInstance().getParticleName(a));
                    });

                }else{
                    sender.sendMessage(TextFormat.RED+"[ParticleShop] 你目前没有拥有任何粒子");
                }
                break;
            case "close":
            case "stop":
                Config cp = ParticleShop.getInstance().getPlayerConfig(sender.getName());
                cp.set("Presence", null);
                cp.save();
                if(ParticleShop.getInstance().getFollowList().containsKey(sender.getName()))
                    ParticleShop.getInstance().getFollowList().remove(sender.getName());
                sender.sendMessage(TextFormat.GRAY+"[ParticleShop] 你已经关闭了粒子跟随");
                break;
            default:
                sendUsage(sender);
                break;

        }
        return true;
    }
    private void sendUsage(CommandSender sender){
        sender.sendMessage(TextFormat.AQUA+"--------ParticleShop命令列表--------");
        sender.sendMessage(TextFormat.AQUA+"/ps set <粒子编号>    | 设置你目前的跟随粒子");
        sender.sendMessage(TextFormat.AQUA+"/ps stop | 关闭粒子跟随");
        sender.sendMessage(TextFormat.AQUA+"/ps list | 查看你目前拥有的所有粒子");
        sender.sendMessage(TextFormat.AQUA+"/ps help | 显示本插件帮助");
    }
}

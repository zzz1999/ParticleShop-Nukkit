package cn.zzz1999.ParticleShop.Command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import cn.zzz1999.ParticleShop.Language.TextTranslation;
import cn.zzz1999.ParticleShop.ParticleShop;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParticleCommand extends Command {
    public ParticleCommand() {
        super("ps", "ParticleShop Command", "ps help", new String[]{"particleshop"});
        this.setPermission("ParticleShop.command.ps");
        this.commandParameters.put("help", new CommandParameter[]{
                new CommandParameter("help", new String[]{"help"}),
        });
        this.commandParameters.put("set", new CommandParameter[]{
                new CommandParameter("set", new String[]{"set"}),
                new CommandParameter("Presence", CommandParameter.ARG_TYPE_INT),
        });
        this.commandParameters.put("list", new CommandParameter[]{
                new CommandParameter("list", new String[]{"help"}),
        });
        this.commandParameters.put("close", new CommandParameter[]{
                new CommandParameter("close | stop", new String[]{"close", "stop"}),
        });

    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 0)
            switch (args[0]) {
                case "set":
                    if (args.length >= 2) {
                        if (isNumeric(args[1])) {
                            Config c = ParticleShop.getInstance().getPlayerConfig(sender.getName());
                            ArrayList<Integer> arr = (ArrayList<Integer>) c.get("Particles");
                            if (!Objects.equals(null, arr)) {
                                if (arr.contains(Integer.parseInt(args[1]))) {
                                    c.set("Presence", Integer.parseInt(args[1]));
                                    c.save();
                                    ParticleShop.getInstance().getFollowList().put(sender.getName(), Integer.parseInt(args[1]));
                                    sender.sendMessage(new TextTranslation("command.set.succeed", ParticleShop.getInstance().getParticleName(Integer.parseInt(args[1]))).toString());
                                } else {
                                    sender.sendMessage(new TextTranslation("command.set.inexistence", ParticleShop.getInstance().getParticleName(Integer.parseInt(args[1]))).toString());
                                }
                            } else {
                                sender.sendMessage(new TextTranslation("command.set.empty").toString());
                            }
                        } else {
                            sender.sendMessage(new TextTranslation("command.set.help").toString());
                        }
                    } else {
                        sender.sendMessage(new TextTranslation("command.set.lack").toString());
                    }

                    break;
                case "list":
                    Config config = ParticleShop.getInstance().getPlayerConfig(sender.getName());
                    ArrayList<Integer> list = (ArrayList<Integer>) config.get("Particles");
                    if (!Objects.equals(null, list)) {
                        sender.sendMessage(new TextTranslation("command.list.title").toString());
                        list.forEach((a) -> sender.sendMessage("- (" + a + ") " + ParticleShop.getInstance().getParticleName(a)));

                    } else {
                        sender.sendMessage(new TextTranslation("command.list.empty").toString());
                    }
                    break;
                case "close":
                case "stop":
                    Config cp = ParticleShop.getInstance().getPlayerConfig(sender.getName());
                    cp.set("Presence", null);
                    cp.save();
                    ParticleShop.getInstance().getFollowList().remove(sender.getName());
                    sender.sendMessage(new TextTranslation("command.stop").toString());
                    break;

                case "help":
                default:
                    sendUsage(sender);
                    break;

            }
        return true;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(new TextTranslation("command.help.title").toString());
        sender.sendMessage(new TextTranslation("command.help.set").toString());
        sender.sendMessage(new TextTranslation("command.help.stop").toString());
        sender.sendMessage(new TextTranslation("command.help.list").toString());
        sender.sendMessage(new TextTranslation("command.help.help").toString());
    }
}

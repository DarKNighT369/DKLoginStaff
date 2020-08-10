package me.dark.night.dkloginstaff.commands;

import me.dark.night.dkloginstaff.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;

public class CommandLoginstaff implements CommandExecutor {

    public CommandLoginstaff(Main main) {
        main.getCommand("loginstaff").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lb, String[] a) {

        if (!s.hasPermission("dkloginstaff.admin")){
            s.sendMessage("§cVocê não possui permissão para executar este comando!");
        } else {
            if (a.length == 0) {
                s.sendMessage("");
                s.sendMessage("§8* §a/loginstaff add (nick) (discord id) §8- §fPara adicionar um jogador como staff!");
                s.sendMessage("§8* §a/loginstaff remove (nick) §8- §fPara remover um jogador da lista de staff!");
                s.sendMessage("");
            } else if (a[0].equalsIgnoreCase("add")){
                if (a.length <= 2){
                    s.sendMessage("§8* §a/loginstaff add (nick) (discord id) §8- §fPara adicionar um jogador como staff!");
                } else {
                    String nick = a[1];
                    if (Main.getInstance().getNickDiscord().containsKey(nick)){
                        s.sendMessage("§cEste jogador já existe na lista!");
                    } else {
                        String dcid = a[2];
                        Main.getInstance().getNickDiscord().put(nick, dcid);
                        Main.getInstance().getDiscordNick().put(dcid, nick);
                        List<String> playersConfig = Main.getInstance().getConfig().getStringList("Jogadores");
                        playersConfig.add(nick + ":" + dcid);
                        Main.getInstance().getConfig().set("Jogadores", playersConfig);
                        Main.getInstance().saveConfig();
                        s.sendMessage("§aJogador adicionado com sucesso!");
                    }
                }
            } else if (a[0].equalsIgnoreCase("remove")){
                if (a.length <= 1){
                    s.sendMessage("§8* §a/loginstaff remove (nick) §8- §fPara remover um jogador da lista de staff!");
                } else {
                    String nick = a[1];
                    if (Main.getInstance().getNickDiscord().containsKey(nick)){
                        Main.getInstance().getNickDiscord().remove(nick);
                        Main.getInstance().getDiscordNick().remove(Main.getInstance().getNickDiscord().get(nick));
                        List<String> playersConfig = Main.getInstance().getConfig().getStringList("Jogadores");
                        for (int i = 0; i < playersConfig.size(); i++){
                            if (playersConfig.get(i).contains(nick))
                                playersConfig.remove(i);
                        }
                        Main.getInstance().getConfig().set("Jogadores", playersConfig);
                        Main.getInstance().saveConfig();
                        s.sendMessage("§aJogador removido com suceso da lista!");
                    } else {
                        s.sendMessage("§cEste jogador não existe na lista!");
                    }
                }
            } else {
                s.sendMessage("");
                s.sendMessage("§8* §a/loginstaff add (nick) (discord id) §8- §fPara adicionar um jogador como staff!");
                s.sendMessage("§8* §a/loginstaff remove (nick) §8- §fPara remover um jogador da lista de staff!");
                s.sendMessage("");
            }
        }

        return false;
    }
}

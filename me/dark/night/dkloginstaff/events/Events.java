package me.dark.night.dkloginstaff.events;

import me.dark.night.dkloginstaff.Main;
import me.dark.night.dkloginstaff.bot.Bot;
import me.dark.night.dkloginstaff.utils.Title;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;

public class Events implements Listener {

    private String msgConfirm, prefix, msgDelayed, msgDelayedKick, actionbar, msgQuit, msgBlockChat, msgCmdBlock, msgOpenMenuBlock;
    private int delay;

    public Events(Main main){
        main.getServer().getPluginManager().registerEvents(this, main);
        msgConfirm = main.getConfig().getString("Mensagens.ConfirmarEntrada");
        prefix = main.getConfig().getString("Mensagens.Prefix");
        msgDelayed = main.getConfig().getString("Mensagens.Demorou");
        msgDelayedKick = main.getConfig().getString("Mensagens.DemorouKick").replace('&', '§');
        actionbar = main.getConfig().getString("Mensagens.Actionbar").replace('&', '§');
        msgBlockChat = main.getConfig().getString("Mensagens.ChatBlock").replace('&', '§');
        msgQuit = main.getConfig().getString("Mensagens.Deslogou");
        msgBlockChat = main.getConfig().getString("Mensagens.ChatBlock").replace('&', '§');
        msgCmdBlock = main.getConfig().getString("Mensagens.CmdBlock").replace('&', '§');
        msgOpenMenuBlock = main.getConfig().getString("Mensagens.OpenMenuBlock").replace('&', '§');
        delay = main.getConfig().getInt("Delay");
    }

    @EventHandler
    void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (Main.getInstance().getNickDiscord().containsKey(p.getName())) {
            Main.getInstance().getPlayerVerifing().add(p.getName());
            User user = Bot.jda.getUserById(Main.getInstance().getNickDiscord().get(p.getName()));

            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(prefix);
            eb.setDescription(msgConfirm.replace("{player}", p.getName()));
            eb.setColor(Color.yellow);

            TextChannel ch = Bot.jda.getTextChannelById(Main.getInstance().idChat);
            if (ch != null){
                ch.sendMessage("<@" + user.getId() + ">").queue();
                ch.sendMessage(eb.build()).queue(message -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❎").queue();
                    Main.getInstance().getIdMessage().put(message.getId(), user.getAsTag());
                });
            }
            eb.clear();

            new BukkitRunnable(){
                int tempo = delay;

                @Override
                public void run() {
                    if (tempo > 0){
                        if (!Main.getInstance().getPlayerVerifing().contains(p.getName())) {
                            cancel();
                            return;
                        }
                        Title.sendActionBar(actionbar.replace("{tempo}", "" + tempo), p);
                        tempo--;
                    } else {
                        EmbedBuilder eb2 = new EmbedBuilder();
                        eb2.setAuthor(prefix);
                        eb2.setDescription(msgDelayed.replace("{player}", p.getName()));
                        eb2.setColor(Color.red);
                        if (ch != null){
                            ch.sendMessage(eb2.build()).queue();
                        }
                        eb2.clear();
                        Main.getInstance().getPlayerVerifing().remove(p.getName());
                        p.kickPlayer(msgDelayedKick);
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 20, 20);

        }
    }

    @EventHandler
    void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if (Main.getInstance().getPlayerVerifing().contains(p.getName())){
            Location loc = new Location(e.getTo().getWorld(), e.getTo().getX(), e.getTo().getY(), e.getTo().getZ());
            p.teleport(loc);
        }
    }

    @EventHandler
    void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if (Main.getInstance().getPlayerVerifing().contains(p.getName())){
            Main.getInstance().getPlayerVerifing().remove(p.getName());
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(prefix);
            eb.setDescription(msgQuit.replace("{player}", p.getName()));
            eb.setColor(Color.red);

            TextChannel ch = Bot.jda.getTextChannelById(Main.getInstance().idChat);
            if (ch != null){
                ch.sendMessage(eb.build()).queue();
            }
            eb.clear();
        }
    }

    @EventHandler
    void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if (Main.getInstance().getPlayerVerifing().contains(p.getName())){
            e.setCancelled(true);
            p.sendMessage(msgBlockChat);
        }
    }

    @EventHandler
    void onCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if (Main.getInstance().getPlayerVerifing().contains(p.getName())){
            e.setCancelled(true);
            p.sendMessage(msgCmdBlock);
        }
    }

    @EventHandler
    void onOpenMenu(InventoryOpenEvent e){
        Player p = (Player) e.getPlayer();
        if (Main.getInstance().getPlayerVerifing().contains(p.getName())){
            e.setCancelled(true);
            p.sendMessage(msgOpenMenuBlock);
        }
    }

}

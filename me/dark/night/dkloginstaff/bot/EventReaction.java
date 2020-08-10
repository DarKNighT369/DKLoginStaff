package me.dark.night.dkloginstaff.bot;

import me.dark.night.dkloginstaff.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.awt.*;

public class EventReaction extends ListenerAdapter {

    private static String msgConfirm, msgDenied, msgKick, msgConfirmChat, prefix;

    static{
        msgConfirm = Main.getInstance().getConfig().getString("Mensagens.Autorizado");
        msgConfirmChat = Main.getInstance().getConfig().getString("Mensagens.AutorizadoChat").replace('&', '§');
        msgDenied = Main.getInstance().getConfig().getString("Mensagens.Negou");
        msgKick = Main.getInstance().getConfig().getString("Mensagens.NegouKick").replace('&', '§');
        prefix = Main.getInstance().getConfig().getString("Mensagens.Prefix");
    }

    @SubscribeEvent
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if(e.getTextChannel().getId().equalsIgnoreCase(Main.getInstance().idChat) &&
                Main.getInstance().getIdMessage().getOrDefault(e.getMessageId(), "0").
                        equals(e.getMember().getUser().getAsTag())) {
            String nickInGame = Main.getInstance().getDiscordNick().get(e.getMember().getUser().getId());
            Player p = Bukkit.getPlayer(nickInGame);
            Main.getInstance().getIdMessage().remove(e.getMessageId());

            if (e.getReaction().getReactionEmote().getEmoji().equals("✅")){
                if (p == null)
                    return;
                p.sendMessage(msgConfirmChat);
                Main.getInstance().getPlayerVerifing().remove(p.getName());
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(prefix);
                eb.setDescription(msgConfirm);
                eb.setColor(Color.green);

                TextChannel ch = Bot.jda.getTextChannelById(Main.getInstance().idChat);
                if (ch != null){
                    ch.sendMessage(eb.build()).queue();
                }

                eb.clear();
            } else if (e.getReaction().getReactionEmote().getEmoji().equals("❎")){
                if (p == null)
                    return;
                Main.getInstance().getPlayerVerifing().remove(p.getName());
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        p.kickPlayer(msgKick);
                        cancel();
                    }
                }.runTaskTimer(Main.getInstance(), 1, 1);

                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(prefix);
                eb.setDescription(msgDenied);
                eb.setColor(Color.red);

                TextChannel ch = Bot.jda.getTextChannelById(Main.getInstance().idChat);
                if (ch != null){
                    ch.sendMessage(eb.build()).queue();
                }

                eb.clear();
            }
        }
    }

}

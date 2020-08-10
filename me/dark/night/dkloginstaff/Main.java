package me.dark.night.dkloginstaff;

import me.dark.night.dkloginstaff.bot.Bot;
import me.dark.night.dkloginstaff.commands.CommandLoginstaff;
import me.dark.night.dkloginstaff.events.Events;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    public String tokenBot, idChat;
    private Map<String, String> idMessage, nickDiscord, discordNick;
    private List<String> playerVerifing;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        setup();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    private void setup(){
        idMessage = new HashMap<>();
        nickDiscord = new HashMap<>();
        discordNick = new HashMap<>();
        playerVerifing = new ArrayList<>();
        tokenBot = getConfig().getString("Discord.BotToken");
        idChat = getConfig().getString("Discord.ChatID");
        for (String key : getConfig().getStringList("Jogadores")) {
            nickDiscord.put(key.split(":")[0], key.split(":")[1]);
            discordNick.put(key.split(":")[1], key.split(":")[0]);
        }

        new Bot();
        new CommandLoginstaff(this);
        new Events(this);
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    public Map<String, String> getIdMessage(){
        return idMessage;
    }

    public Map<String, String> getNickDiscord(){
        return nickDiscord;
    }

    public Map<String, String> getDiscordNick(){
        return discordNick;
    }

    public List<String> getPlayerVerifing() {
        return playerVerifing;
    }

}

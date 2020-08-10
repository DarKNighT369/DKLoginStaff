package me.dark.night.dkloginstaff.bot;

import me.dark.night.dkloginstaff.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import javax.security.auth.login.LoginException;

public class Bot {

    public static JDA jda;

    @Deprecated
    public Bot() {
        try {
            jda = new JDABuilder(Main.getInstance().tokenBot).build().awaitReady();
            jda.addEventListener(new EventReaction());
        } catch (LoginException | InterruptedException e) {
            Bukkit.getConsoleSender().sendMessage("§cNão foi possível conectar ao bot, desligando o plugin...");
            Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
            return;
        }
    }

}

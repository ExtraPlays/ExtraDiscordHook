package me.extraplays.discordhook;

import br.com.blecaute.inventory.InventoryHelper;
import me.extraplays.discordhook.Commands.LinkCommand;
import me.extraplays.discordhook.Listeners.ChatListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.HashMap;

public final class DiscordHook extends JavaPlugin implements Listener {

    private JDA jdaBuilder;
    private static DiscordHook instance;
    public HashMap<String, String> tokens;
    public HashMap<String, String> userID;
    public File accounts;
    FileConfiguration fileConfiguration;

    @Override
    public void onEnable() {
        instance = this;
        InventoryHelper.enable(this);

        saveDefaultConfig();
        accounts = new File(getDataFolder(), "accounts.yml");
        fileConfiguration = YamlConfiguration.loadConfiguration(accounts);

        try {
            jdaBuilder = JDABuilder.createDefault(getConfig().getString("token"),
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MEMBERS
            ).disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE).build();

            jdaBuilder.setAutoReconnect(true);

        } catch (LoginException e) {
            e.printStackTrace();
        }

        tokens = new HashMap<>();
        userID = new HashMap<>();
        Bukkit.getPluginCommand("vincular").setExecutor(new LinkCommand());
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public JDA getJdaBuilder() {
        return jdaBuilder;
    }

    public FileConfiguration getAccountsConfig() {
        return this.fileConfiguration;
    }

    public static DiscordHook getInstance() {
        return instance;
    }
}

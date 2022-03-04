package me.extraplays.discordhook.Listeners;

import me.extraplays.discordhook.DiscordHook;
import me.extraplays.discordhook.Utils.ActionBarUtil;
import me.extraplays.discordhook.Utils.Rewards;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;

public class ChatListener implements Listener {



    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) throws IOException {

        if (!DiscordHook.getInstance().tokens.containsKey(e.getPlayer().getName())) return;


        // TROQUEI DE CONTAINS PARA EQUALS
        if (e.getMessage().equals(DiscordHook.getInstance().tokens.get(e.getPlayer().getName()))){

            ActionBarUtil.sendActionBar(e.getPlayer(), "&aConta vinculada com sucesso!");

            Rewards.giveRewards(e.getPlayer());
            DiscordHook.getInstance().tokens.remove(e.getPlayer().getName());
            DiscordHook.getInstance().userID.remove(e.getPlayer().getName());
            DiscordHook.getInstance().getAccountsConfig().set("vinculed." + e.getPlayer().getName(), e.getMessage());
            DiscordHook.getInstance().getAccountsConfig().save(DiscordHook.getInstance().accounts);

            if (DiscordHook.getInstance().getConfig().getBoolean("sounds.enable")){

                String sound = DiscordHook.getInstance().getConfig().getString("sounds.success");
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(sound), 1f, 1f);

            }

            e.setCancelled(true);
        }
    }


}

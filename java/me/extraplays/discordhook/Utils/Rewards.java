package me.extraplays.discordhook.Utils;

import me.extraplays.discordhook.DiscordHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Rewards {

    public static void giveRewards(Player p){

        for (String s : DiscordHook.getInstance().getConfig().getStringList("rewards.items")){
            String[] item = s.split(";");
            int qtd = Integer.parseInt(item[1]);
            ItemStack itemStack = new ItemStack(Material.matchMaterial(item[0]), qtd);
            p.getInventory().addItem(itemStack);
        }

        for (String s : DiscordHook.getInstance().getConfig().getStringList("rewards.commands")){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s.replace("@player", p.getName()));
        }

        Guild guild = DiscordHook.getInstance().getJdaBuilder().getGuildById(DiscordHook.getInstance().getConfig().getString("guildId"));
        Role role = guild.getRoleById(DiscordHook.getInstance().getConfig().getString("roles.linked"));
        guild.addRoleToMember(DiscordHook.getInstance().userID.get(p.getName()), role).queue();


    }

}

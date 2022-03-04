package me.extraplays.discordhook.Commands;

import br.com.blecaute.inventory.InventoryBuilder;
import me.extraplays.discordhook.DiscordHook;
import me.extraplays.discordhook.Enum.Icon;
import me.extraplays.discordhook.Utils.ActionBarUtil;
import me.extraplays.discordhook.Utils.ColorUtil;
import me.extraplays.discordhook.Utils.Token;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LinkCommand implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("vincular")){
            if (!(sender instanceof Player)){
                sender.sendMessage("Esse comando não pode ser usado no console");
                return false;
            }

            Player p = (Player) sender;

            if (args.length < 1) {

                boolean linked = DiscordHook.getInstance().getAccountsConfig().getString("vinculed." + p.getName()) != null;

                new InventoryBuilder<Icon>("&8Vincular", 3).withObjects(Arrays.asList(Icon.values()), click -> {

                    Icon icon = click.getObject();

                    switch (Objects.requireNonNull(icon)){
                        case CLOSE -> p.closeInventory();
                        case STATUS -> {
                            ItemMeta itemMeta = click.getItemStack().getItemMeta();
                            itemMeta.setLore(ColorUtil.colored(List.of("", "&6✪ &7Status da conta: " + (linked ? "&aVinculado" : "&cNão Vinculado"))));
                            click.getItemStack().setItemMeta(itemMeta);
                        }
                        case LINK -> {
                            p.closeInventory();
                            p.sendMessage(ColorUtil.colored("&7Para vincular sua conta utilize: \n&a/vincular <SeuID>"));
                        }
                    }

                }).build(p);

                return false;
            }

            if (DiscordHook.getInstance().getAccountsConfig().getString("vinculed." + p.getName()) != null){
                p.sendMessage(ChatColor.RED + "Você já vinculou a sua conta!");
                return false;
            }

            p.sendMessage("");
            p.sendMessage(ChatColor.GREEN + "Vinculação Iniciada");
            p.sendMessage(ChatColor.GREEN + "O Token foi enviado para a sua DM");
            p.sendMessage("");
            p.sendMessage(ChatColor.RED + "Digite o token recebido");
            p.sendMessage("");

            Guild guild = DiscordHook.getInstance().getJdaBuilder().getGuildById(DiscordHook.getInstance().getConfig().getString("guildId"));

            if (!DiscordHook.getInstance().tokens.containsKey(p.getName())){

                Token token = new Token();

                DiscordHook.getInstance().tokens.put(p.getName(), token.getToken());
                DiscordHook.getInstance().userID.put(p.getName(), args[0]);

                guild.retrieveMemberById(args[0]).complete().getUser()
                        .openPrivateChannel().flatMap(ch -> ch.sendMessage("Não compartilhe esse token com ninguem. \nToken: **" + token.getToken() + "**")).queue();


                Bukkit.getScheduler().runTaskLater(DiscordHook.getInstance(), () -> {

                    DiscordHook.getInstance().tokens.remove(p.getName());

                    if (DiscordHook.getInstance().getAccountsConfig().getString("vinculed." + p.getName()) == null){
                        ActionBarUtil.sendActionBar(p, "&cVinculação Cancelada!");

                        if (DiscordHook.getInstance().getConfig().getBoolean("sounds.enable")){
                            String sound = DiscordHook.getInstance().getConfig().getString("sounds.fail");
                            p.playSound(p.getLocation(), Sound.valueOf(sound), 1f, 1f);
                        }
                    }

                }, 20L * DiscordHook.getInstance().getConfig().getInt("timeToCancel"));


            }else {
                p.sendMessage(ChatColor.RED + "Você já iniciou a vinculação.");
            }

        }

        return false;
    }
}

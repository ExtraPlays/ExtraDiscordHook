package me.extraplays.discordhook.Enum;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventorySlot;
import me.extraplays.discordhook.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum Icon implements InventorySlot {

    CLOSE(26, new ItemBuilder(Material.BARRIER, "&cFechar", List.of("", "&7Clique aqui para fechar"))),
    STATUS(12, new ItemBuilder(Material.NAME_TAG, "&6Informações", List.of("", "&6✪ &7Status da conta: &6Clique para revelar"))),
    LINK(14, new ItemBuilder(Material.REDSTONE_BLOCK, "&6Vincular", List.of("", "&7Clique aqui para &avincular &7a sua conta", "")));

    private final ItemStack itemStack;
    private final int slot;

    Icon(int slot, ItemBuilder builder) {
        this.itemStack = builder.build();
        this.slot = slot;
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull Inventory inventory, @NotNull InventoryProperty property) {
        return this.itemStack;
    }
}

package com.cota.pets.gui;

import com.cota.cotacore.core.managers.menu.AbstractMenu;
import com.cota.cotacore.core.managers.menu.buttons.BlockButton;
import com.cota.cotacore.main.files.data.config.ConfigUtils;
import com.cota.pets.utils.VaultUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayersGui extends AbstractMenu {


    private String mob;

    public PlayersGui(String mob) {
        this.mob = mob;
        setSize(6);
        setTitle("Players");
        createMenu();
        initialize();
    }

    private void initialize() {
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {

            BlockButton button = new BlockButton(this) {

                @Override
                public void onButtonClick(Player player, ClickType clickType, Inventory inventory, ItemStack itemStack) {

                    SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

                    OfflinePlayer add_player = meta.getOwningPlayer();


                    VaultUtils.getPerms().playerAdd(player.getWorld().getName(), add_player, "pets." + mob.toLowerCase());
                     player.sendMessage(ConfigUtils.getStringSection("pets-added")
                             .replaceAll("%player%", add_player.getName()).replaceAll("%pet%", mob)); ;

                     if (add_player.isOnline()) {
                         Player op = add_player.getPlayer();
                         op.sendMessage(ConfigUtils.getStringSection("pets-received")
                                 .replaceAll("%pet%", mob));
                     }

                }
            };

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(p);
            meta.setDisplayName(ChatColor.GOLD + p.getName());
            item.setItemMeta(meta);

            button.setItemStack(item);
            button.initializeButton();
        }
    }
}

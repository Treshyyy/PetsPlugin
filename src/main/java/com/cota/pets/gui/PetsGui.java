package com.cota.pets.gui;

import com.cota.cotacore.core.managers.ItemManager;
import com.cota.cotacore.core.managers.menu.AbstractMenu;
import com.cota.cotacore.core.managers.menu.buttons.BlockButton;
import com.cota.cotacore.main.files.data.config.ConfigUtils;
import com.cota.cotacore.main.files.data.files.PlayerData;
import com.cota.pets.Pets;
import com.cota.pets.PetsManager;
import com.cota.pets.files.FormsUtils;
import com.cota.pets.files.PlayerDataForm;
import com.cota.pets.pathfinders.PetGoal;
import com.cota.pets.pathfinders.listeners.PlayerHandler;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.SpawnEgg;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.css.RGBColor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PetsGui extends AbstractMenu {


    private boolean admin;
    private Player p;

    public PetsGui(boolean admin, Player p) {
        this.admin = admin;
        this.p = p;
        setSize(2);
        setTitle("Pets");
        createMenu();
        initialize();
    }

    ArrayList<EntityType> mobs = new ArrayList<>(List.of(EntityType.CAT, EntityType.WOLF,  EntityType.HORSE,  EntityType.RABBIT,  EntityType.WITHER,  EntityType.DONKEY,
            EntityType.GUARDIAN,  EntityType.ZOMBIE,  EntityType.SKELETON,  EntityType.CREEPER,  EntityType.ALLAY,  EntityType.VEX,  EntityType.PARROT,  EntityType.ENDERMAN));


    private void initialize() {

        for (EntityType mob : mobs) {
            if (!p.hasPermission("pets." + mob.name().toLowerCase())) continue;
            BlockButton button = new BlockButton(this) {

                @Override
                public void onButtonClick(Player player, ClickType clickType, Inventory inventory, ItemStack itemStack) {

                    if (clickType == ClickType.LEFT) {
                         String mob_name = itemStack.getType().name().replace("_SPAWN_EGG", "");
                         EntityType entity = EntityType.valueOf(mob_name);
                         new PetsManager(player, entity).summonPet();
                         player.sendMessage(ConfigUtils.getStringSection("pet-summoned").replaceAll("%pet%", mob_name));

                    }else if (clickType == ClickType.RIGHT && !admin) {

                            new PetsManager(player, mob).removePet();
                        player.sendMessage(ConfigUtils.getStringSection("pet-unsummoned").replaceAll("%pet%", mob.name()));

                    }else if (clickType == ClickType.SHIFT_RIGHT) {
                        new PetsManager(player, mob).removePet();
                        player.sendMessage(ConfigUtils.getStringSection("pet-unsummoned").replaceAll("%pet%", mob.name()));
                    }else {
                        new PlayersGui(mob.name().toLowerCase()).displayTo(player);
                    }
                }
            };

            ItemStack item = new ItemStack(Material.valueOf(mob.name() + "_SPAWN_EGG"));
            ItemMeta meta = item.getItemMeta();
            String mob_name = mob.name().toLowerCase();
            mob_name = mob_name.substring(0, 1).toUpperCase() + mob_name.substring(1);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + mob_name + " Pet")) ;
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to summon!"));
            if (admin) {
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7Right click to add it to someone!"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7Shift-Right click to unsummon!"));
            }else {
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7Right click to unsummon!"));
            }

            meta.setLore(lore);
            item.setItemMeta(meta);

            button.setItemStack(item);
            button.initializeButton();
        }

    }
}

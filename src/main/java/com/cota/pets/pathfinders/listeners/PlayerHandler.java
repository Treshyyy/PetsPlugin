package com.cota.pets.pathfinders.listeners;

import com.cota.cotacore.core.listener.CotaListener;
import com.cota.pets.Pets;
import com.cota.pets.PetsManager;
import com.cota.pets.files.FormsUtils;
import com.cota.pets.files.PlayerDataForm;
import com.cota.pets.pathfinders.PetGoal;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerHandler extends CotaListener implements Listener {

    @EventHandler
    private void join(PlayerJoinEvent event) {

        Player p = event.getPlayer();

        PlayerDataForm pdf = FormsUtils.getPlayerDataForm(p);

        if (pdf != null) {
            EntityType entity = pdf.getEntity();

            new PetsManager(p, entity).summonPet();
        }
    }
}

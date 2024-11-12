package com.cota.pets;

import com.cota.cotacore.CotaCore;
import com.cota.pets.commands.PetsCommand;
import com.cota.pets.pathfinders.PetGoal;
import com.cota.pets.pathfinders.listeners.PlayerHandler;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public final class Pets extends CotaCore {

    public HashMap<Player, Mob> summoned_mobs = new HashMap<>();

    private static Permission perms = null;
    public static Pets INSTANCE;

    @Override
    public void onCoreLoad() {


        saveDefaultConfig();
        INSTANCE = this;
        new PetsCommand().register();
        new PlayerHandler().register();
        setupPermissions();


    }
    private boolean setupPermissions() {

        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }


    public Permission getPerms() {
        return perms;
    }

    public JavaPlugin getUserPlugin() {
        return this;
    }


    public String getVersion() {
        return "1.0.0";
    }

    public String getPluginName() {
        return "Pets";
    }



    @Override
    public void onCoreUnLoad() {
        for (Mob mob : summoned_mobs.values()) {
            mob.remove();
        }
    }
}

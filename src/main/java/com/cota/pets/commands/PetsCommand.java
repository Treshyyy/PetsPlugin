package com.cota.pets.commands;

import com.cota.cotacore.core.commands.main.CotaCommand;
import com.cota.pets.gui.PetsGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PetsCommand extends CotaCommand {

    public PetsCommand() {

    }

    public String permission() {
        return null;
    }



    public boolean onlyPlayer() {
        return true;
    }

    public String usage() {
        return "usage: /pets";
    }

    public String name() {
        return "pets";
    }


    public void execute(@NotNull CommandSender sender) {
        if (sender instanceof Player p) {
            if (p.hasPermission("pets.admin")) {
                new PetsGui(true,p).displayTo(p);
                return;
            }
            new PetsGui(false, p).displayTo(p);
        }

    }



}

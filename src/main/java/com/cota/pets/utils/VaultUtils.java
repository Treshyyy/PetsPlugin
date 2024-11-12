package com.cota.pets.utils;

import com.cota.pets.Pets;
import net.milkbowl.vault.permission.Permission;


public class VaultUtils {

    public static Permission getPerms() {
        return Pets.INSTANCE.getPerms();
    }

}

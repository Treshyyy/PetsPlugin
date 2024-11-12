package com.cota.pets.files;

import com.cota.cotacore.main.files.data.files.PlayerData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class FormsUtils {

    public static PlayerDataForm getPlayerDataForm(Player p) {
        PlayerData pd = new PlayerData(p);

        if (pd.getConfig().getString("active_entity") != null) {
            EntityType entity = EntityType.valueOf(pd.getConfig().getString("active_entity"));
            return new PlayerDataForm( entity, p);
        }

        return null;

    }

    public static void updatePlayerDataForm(PlayerDataForm form) {
        PlayerData pd = new PlayerData(form.getP());
        if (form.getEntity() != null) {
            pd.getConfig().set("active_entity", form.getEntity().name());

        }else {
            pd.getConfig().set("active_entity", null);
        }
        pd.save();
    }

}

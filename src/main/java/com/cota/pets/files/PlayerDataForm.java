package com.cota.pets.files;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayerDataForm {

    private EntityType entity;
    private Player p;

    public PlayerDataForm(EntityType entity, Player p) {
        this.entity = entity;
        this.p = p;
    }

    public EntityType getEntity() {
        return entity;
    }

    public Player getP() {
        return p;
    }

    public void setEntity(EntityType entity) {
        this.entity = entity;
    }
}

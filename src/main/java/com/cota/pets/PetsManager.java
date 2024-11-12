package com.cota.pets;

import com.cota.pets.files.FormsUtils;
import com.cota.pets.files.PlayerDataForm;
import com.cota.pets.pathfinders.PetGoal;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;

public class PetsManager {

    private Player p;
    private EntityType e;

    public PetsManager(Player p, EntityType e) {
        this.p = p;
        this.e = e;
    }

    public void summonPet() {
        Entity mob = p.getWorld().spawnEntity(p.getLocation(), e) ;

        PlayerDataForm pdf = FormsUtils.getPlayerDataForm(p);
        if (pdf == null) {
            PlayerDataForm new_file = new PlayerDataForm(e, p);
            FormsUtils.updatePlayerDataForm(new_file);
        }else {

            pdf.setEntity(e);
            FormsUtils.updatePlayerDataForm(pdf);
        }

        if (Pets.INSTANCE.summoned_mobs.containsKey(p)) {
            Pets.INSTANCE.summoned_mobs.get(p).remove();

        }


        if (mob instanceof LivingEntity livingEntity) {
            if (!livingEntity.getType().equals(EntityType.WOLF) && !livingEntity.getType().equals(EntityType.CAT) &&
                    !livingEntity.getType().equals(EntityType.RABBIT) && !livingEntity.getType().equals(EntityType.VEX) &&
                    !livingEntity.getType().equals(EntityType.PARROT) && !livingEntity.getType().equals(EntityType.ALLAY)) {
                livingEntity.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(.5F);
            }



            if (livingEntity instanceof Mob m) {

                if (livingEntity instanceof Wither w) {
                    BossBar bar = w.getBossBar();
                    bar.setVisible(false);

                }
                Bukkit.getMobGoals().addGoal(m, 0, new PetGoal(m, p, false, false));
                Pets.INSTANCE.summoned_mobs.put(p, m);

            }



        }


    }
    public void removePet() {
        if (Pets.INSTANCE.summoned_mobs.containsKey(p)) {
            Pets.INSTANCE.summoned_mobs.get(p).remove();
        }

        PlayerDataForm pdf = FormsUtils.getPlayerDataForm(p);
        if (pdf != null) {
            pdf.setEntity(null);
            FormsUtils.updatePlayerDataForm(pdf);
        }
    }

}

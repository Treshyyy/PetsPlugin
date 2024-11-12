package com.cota.pets.pathfinders;

import com.cota.pets.Pets;
import com.cota.pets.files.FormsUtils;
import com.cota.pets.files.PlayerDataForm;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class PetGoal implements Goal<Mob>, Listener {

    private final GoalKey key = GoalKey.of(Mob.class, new NamespacedKey(Pets.INSTANCE, "pet"));

    private final Mob mob;
    private final LivingEntity owner;

    private LivingEntity target;
    private boolean attackPlayer = false;
    private boolean attackTarget = false;
    private double moveDistance = 3.5D;
    private double maxDistance = 16D;
    private double maxForgetTargetDistance = 40D;

    public PetGoal(Mob mob, LivingEntity owner) {
        this.mob = mob;
        this.owner = owner;

    }

    public PetGoal(Mob mob, LivingEntity owner, boolean attackPlayer, boolean attackTarget) {

        this.mob = mob;
        this.owner = owner;

        this.attackPlayer = attackPlayer;
        this.attackTarget = attackTarget;

    }

    @Override
    public boolean shouldActivate() {
        return true;
    }

    @Override
    public boolean shouldStayActive() {
        return true;
    }

    @Override
    public void start() {

        Bukkit.getPluginManager().registerEvents(this, Pets.INSTANCE);

    }

    @Override
    public void stop() {

        HandlerList.unregisterAll(this);
    }

    @Override
    public void tick() {

      //  Bukkit.broadcastMessage(mob.toString());
        if (target == null) {
            if (!owner.getWorld().equals(mob.getWorld())) {
                mob.teleport(owner);
                return;
            }
            mob.setTarget(null);
            double distance = owner.getLocation().distance(mob.getLocation());
            if (distance > maxDistance) {
                mob.teleport(owner);
                return;
            }
            if (distance > moveDistance) mob.getPathfinder().moveTo(owner);
            return;
        }
        if (!isValidTarget(target)) {
            target = null;
            return;
        }

        double distance = owner.getLocation().distance(mob.getLocation());

        if (distance > maxForgetTargetDistance) {
            target = null;
            return;
        }
        mob.setTarget(target);
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public boolean isValidTarget(LivingEntity target) {
        if (!target.isValid() || target.equals(owner)) {
            return false;
        }
        if (target instanceof Player p ) return p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR;
        else if (target instanceof Mob m) {
            Goal<Mob> goal =Bukkit.getMobGoals().getGoal(m, key);
            if (goal instanceof PetGoal petGoal) {
                return !petGoal.getOwner().equals(owner);
            }
        }

        return true;
    }

    public void setTarget(LivingEntity target) {
        if (isValidTarget(target))  this.target = target;

    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.TARGET);
    }


    @EventHandler
    private void entityDamage(EntityDamageEvent event) {
        if (event.getEntity().equals(mob)) {
            if  (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
            event.setCancelled(true);

        }

    }
    @EventHandler
    private void entityDamagebyEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().equals(mob)) {
            event.setCancelled(true);
        }
        if (event.getDamager().equals(owner)) {
            if ((event.getEntity()).equals(mob)) {
                event.setCancelled(true);
                return;
            }
            if (!(event.getEntity() instanceof LivingEntity victim) || target != null) return;

            if (victim instanceof Player p && attackPlayer) setTarget(victim);
            else if (victim instanceof Mob  && attackTarget) setTarget(victim);

        }

        if (event.getEntity().equals(owner) || event.getEntity().equals(mob)) {
            if (!(event.getDamager() instanceof LivingEntity damager) || target != null) return;
            if (damager instanceof Player p && attackPlayer) setTarget(damager);
            else if (damager instanceof Mob  && attackTarget) setTarget(damager);
        }
    }

    @EventHandler
    private void removeFromWorld(EntityRemoveFromWorldEvent e) {
        if (e.getEntity().equals(mob)) HandlerList.unregisterAll(this);
    }

    @EventHandler
    private void entityDamagebyEntity(EntityCombustEvent event) {

        if (event.getEntity().equals(mob)) event.setCancelled(true);

    }
    @EventHandler
    private void withershoot(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Wither w) {
            if (w.equals(mob)) event.setCancelled(true);
        }

    }

    @EventHandler
    private void portal(EntityPortalEnterEvent event) {
        if (event.getEntity().equals(owner)) {

            Pets.INSTANCE.summoned_mobs.get(owner).remove();
            HandlerList.unregisterAll(this);

        }

    }

    @EventHandler
    private void leave(PlayerQuitEvent event) {

        if (event.getPlayer().equals(owner)) {

            Pets.INSTANCE.summoned_mobs.get(owner).remove();
            HandlerList.unregisterAll(this);
        }
    }





}

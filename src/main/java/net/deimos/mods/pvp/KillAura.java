package net.deimos.mods.pvp;

import net.deimos.api.event.EventManager;
import net.deimos.api.event.impl.TickEvent;
import net.deimos.api.gui.settings.BoolSetting;
import net.deimos.api.gui.settings.SliderSetting;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.interfaces.EventHandler;
import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.Category;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

@Module(
        name = "KillAura",
        description = "Hits entities within a radius.",
        category = Category.Combat
)
public class KillAura extends ModuleBuilder {

    BoolSetting weaponOnly = new BoolSetting.Builder()
            .setName("Weapon Only")
            .setDescription("Only attack when player is holding a weapon")
            .setDefaultValue(true)
            .build();
    SliderSetting radius = new SliderSetting.Builder()
            .setName("Radius")
            .setDescription("The radius in which the player attacks the nearest entity")
            .setDefaultValue(4)
            .setMin(1)
            .setMax(10)
            .build();
    BoolSetting players = new BoolSetting.Builder()
            .setName("Players")
            .setDescription("Attack players")
            .setDefaultValue(true)
            .build();
    BoolSetting hostiles = new BoolSetting.Builder()
            .setName("Hostiles")
            .setDescription("Attack hostiles")
            .setDefaultValue(true)
            .build();
    BoolSetting misc = new BoolSetting.Builder()
            .setName("Misc")
            .setDescription("Misc enemies")
            .setDefaultValue(false)
            .build();
    BoolSetting rotate = new BoolSetting.Builder()
            .setName("Rotate")
            .setDefaultValue(true)
            .setDescription("")
            .build();
    BoolSetting grim = new BoolSetting.Builder()
            .setName("Grim")
            .setDefaultValue(false)
            .setDescription("grim anticheat")
            .build();

    private int tickCounter = 0;
    private static final int ATTACK_DELAY = 25;

    public KillAura() {
        addSetting(weaponOnly);
        addSetting(radius);
        addSetting(players);
        addSetting(hostiles);
        addSetting(misc);
        addSetting(rotate);
        addSetting(grim);

        EventManager.register(this);
    }

    @EventHandler
    public void tick(TickEvent.Post event) {
        if (!getEnabled() || client.player == null || client.world == null) {
            return;
        }

        if (weaponOnly.getValue() && !isWeapon()) {
            return;
        }

        tickCounter++;
        if (tickCounter < ATTACK_DELAY) {
            return;
        }
        tickCounter = 0;

        LivingEntity target = findTarget();
        if (target == null) {
            return;
        }

        if (rotate.getValue()) {
            rotateToTarget(target);
        }

        attackTarget(target);
    }

    private void rotateToTarget(Entity target) {
        if (grim.getValue()) {
            RotationManager.grim(target.getPos());
        } else {
            RotationManager.ncp(target.getPos());
        }
    }

    private void attackTarget(Entity target) {
        if (client.player == null || client.interactionManager == null) {
            return;
        }

        client.player.swingHand(Hand.MAIN_HAND);
        client.interactionManager.attackEntity(client.player, target);
    }

    private LivingEntity findTarget() {
        if (client.world == null || client.player == null) {
            return null;
        }

        LivingEntity bestTarget = null;
        double closestDistance = radius.getValue();

        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof LivingEntity) || entity == client.player) {
                continue;
            }

            LivingEntity livingEntity = (LivingEntity) entity;

            if (!isValidTarget(livingEntity)) {
                continue;
            }

            double distance = client.player.distanceTo(livingEntity);
            if (distance < closestDistance) {
                closestDistance = distance;
                bestTarget = livingEntity;
            }
        }

        return bestTarget;
    }

    private boolean isValidTarget(Entity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            return players.getValue() && !player.isCreative() && player != client.player;
        } else if (entity instanceof HostileEntity) {
            return hostiles.getValue();
        } else {
            return misc.getValue();
        }
    }

    private boolean isWeapon() {
        if (client.player == null) {
            return false;
        }

        return client.player.getMainHandStack().isOf(Items.WOODEN_SWORD) ||
                client.player.getMainHandStack().isOf(Items.STONE_SWORD) ||
                client.player.getMainHandStack().isOf(Items.IRON_SWORD) ||
                client.player.getMainHandStack().isOf(Items.DIAMOND_SWORD) ||
                client.player.getMainHandStack().isOf(Items.NETHERITE_SWORD);
    }
}
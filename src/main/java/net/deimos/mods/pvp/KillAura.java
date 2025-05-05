package net.deimos.mods.pvp;

import net.deimos.api.event.impl.TickEvent;
import net.deimos.api.gui.settings.BoolSetting;
import net.deimos.api.gui.settings.SliderSetting;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.util.TickDelayHandler;
import net.deimos.api.i.EventHandler;
import net.deimos.api.i.Module;
import net.deimos.api.mods.Category;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

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

    public KillAura() {
        addSetting(weaponOnly);
        addSetting(radius);
        addSetting(players);
        addSetting(hostiles);
        addSetting(misc);
        addSetting(rotate);
        addSetting(grim);
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (!this.enabled || client.player == null || client.world == null) return;

        if (weaponOnly.value && !isHoldingSword()) return;

        Entity target = getNearestEntity();
        if (!(target instanceof LivingEntity)) return;

        if (rotate.value) {
            if (grim.value) {
                this.RotationManager.grim(target.getPos());
            } else {
                this.RotationManager.ncp(target.getPos());
            }
        }


        TickDelayHandler.runAfterTicks(() -> {

            client.player.swingHand(client.player.getActiveHand());
            assert client.interactionManager != null;
            client.interactionManager.attackEntity(client.player, target);

        }, 25);
    }
    private Entity getNearestEntity() {
        Entity nearest = null;
        double closest = radius.value;

        assert client.world != null;
        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof LivingEntity) || entity == client.player) continue;

            if (!isValidTarget(entity)) continue;

            double dist = entity.distanceTo(client.player);
            if (dist < closest) {
                closest = dist;
                nearest = entity;
            }
        }

        return nearest;
    }

    private boolean isValidTarget(Entity entity) {
        if (entity instanceof PlayerEntity && players.value) {
            return entity != client.player && !((PlayerEntity) entity).isCreative();
        }
        if (entity instanceof HostileEntity && hostiles.value) {
            return true;
        }
        if (!(entity instanceof PlayerEntity) && !(entity instanceof HostileEntity) && misc.value) {
            return true;
        }
        return false;
    }

    private boolean isHoldingSword() {
        assert client.player != null;
        return client.player.getMainHandStack().isOf(Items.WOODEN_SWORD)
                || client.player.getMainHandStack().isOf(Items.STONE_SWORD)
                || client.player.getMainHandStack().isOf(Items.IRON_SWORD)
                || client.player.getMainHandStack().isOf(Items.DIAMOND_SWORD)
                || client.player.getMainHandStack().isOf(Items.NETHERITE_SWORD);
    }
}

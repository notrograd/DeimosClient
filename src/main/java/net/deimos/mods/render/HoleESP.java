package net.deimos.mods.render;

import net.deimos.api.event.impl.RenderEvent;
import net.deimos.api.gui.settings.SliderSetting;
import net.deimos.api.interfaces.EventHandler;
import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.Category;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.util.RenderUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Module(name = "HoleESP",
        description = "Show safe bedrock / obsidian holes nearby",
        category = Category.Render)
public class HoleESP extends ModuleBuilder {
    private final ArrayList<BlockPos> holes = new ArrayList<>();
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private final SliderSetting range = new SliderSetting.Builder()
            .setName("Range")
            .setDescription("Range")
            .setMax(20)
            .setMin(3)
            .build();

    public HoleESP() {
        this.addSetting(range);
    }

    @EventHandler
    public void onRender(RenderEvent event) {
        if (!this.enabled) return;
        holes.clear();
        scanForHoles(mc.world);

        for (BlockPos holePos : holes) {
            RenderUtil.drawBoxOutline(new Box(holePos), new Color(241, 20, 20, 98), 1);
            RenderUtil.drawBoxFill(new Box(holePos), new Color(157, 27, 27, 27));
        }
    }

    private void scanForHoles(World world) {
        if (world == null || mc.player == null) return;

        BlockPos playerPos = mc.player.getBlockPos();
        int scanRange = (range.getValue() != null) ? range.getValue() : 10;

        for (int x = -scanRange; x < scanRange; x++) {
            for (int z = -scanRange; z < scanRange; z++) {
                for (int y = -5; y < 5; y++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    if (isSafeHole(world, pos)) {
                        holes.add(pos);
                    }
                }
            }
        }
    }

    private boolean isSafeHole(World world, BlockPos pos) {
        if (!world.getBlockState(pos).isAir()) return false;
        if (!world.getBlockState(pos.up()).isAir()) return false;
        if (!world.getBlockState(pos.up(2)).isAir()) return false;

        return isObsidianOrBedrock(world, pos.down()) &&
                isObsidianOrBedrock(world, pos.north()) &&
                isObsidianOrBedrock(world, pos.south()) &&
                isObsidianOrBedrock(world, pos.east()) &&
                isObsidianOrBedrock(world, pos.west());
    }

    private boolean isObsidianOrBedrock(World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(Blocks.OBSIDIAN) || world.getBlockState(pos).isOf(Blocks.BEDROCK);
    }
}
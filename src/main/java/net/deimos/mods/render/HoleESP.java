package net.deimos.mods.render;

import net.deimos.api.event.impl.TickEvent;
import net.deimos.api.interfaces.EventHandler;
import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.Category;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Module(name = "HoleESP",
        description = "Show safe bedrock / obsidian holes nearby",
        category = Category.Render)
public class HoleESP extends ModuleBuilder {

    private final List<BlockPos> holes = new ArrayList<>();

    @EventHandler
    public void tick(TickEvent.Post e) {
        if (!this.getEnabled()) {
            RenderUtil.cleanup();
            holes.clear();
            return;
        }

        if (client.player == null || client.world == null) return;

        holes.clear();
        BlockPos playerPos = client.player.getBlockPos();

        int range = 6;

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -2; y <= 0; y++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    if (isSafeHole(pos)) {
                        holes.add(pos);
                    }
                }
            }
        }

        for (BlockPos holes : holes) {
            RenderUtil.drawBoxOutline(holes, new Color(222, 44, 44), 0.6f);
            RenderUtil.drawBoxFill(new Box((BlockPos) holes), new Color(222, 44, 44));
        }
    }

    private boolean isSafeHole(BlockPos pos) {
        if (!isAir(pos) || !isAir(pos.up()) || !isAir(pos.up(2))) return false;

        if (isAir(pos.up(3)) || isAir(pos.up(1)) || isAir(pos.up(2))) return false;

        if (isSolid(pos.down())) return false;
        if (isSolid(pos.north())) return false;
        if (isSolid(pos.south())) return false;
        if (isSolid(pos.east())) return false;
        if (isSolid(pos.west())) return false;

        return true;
    }

    private boolean isAir(BlockPos pos) {
        assert client.world != null;
        Block block = client.world.getBlockState(pos).getBlock();
        return block == Blocks.AIR;
    }

    private boolean isSolid(BlockPos pos) {
        assert client.world != null;
        Block block = client.world.getBlockState(pos).getBlock();
        return block != Blocks.BEDROCK && block != Blocks.OBSIDIAN;
    }
}

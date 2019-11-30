package kjk.blockdegradation;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;

public class ModEvents {

    int updateLCG = 1;
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        Iterator<Chunk> chunks = getTickableChunks(world);
        int randomTickSpeed = world.getGameRules().getInt("randomTickSpeed");
        if (chunks == null)
            return;

        chunks.forEachRemaining((chunk) -> {
            int x = chunk.x * 16;
            int z = chunk.z * 16;

            for (ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
                if (storage != Chunk.NULL_BLOCK_STORAGE) {
                    for (int i = 0; i < randomTickSpeed; i++) {
                        this.updateLCG = this.updateLCG * 3 + 1013904223;
                        int magic = this.updateLCG >> 2;
                        int dx = magic & 15;
                        int dz = magic >> 8 & 15;
                        int dy = magic >> 16 & 15;
                        BlockPos pos = new BlockPos(dx + x, dy + storage.getYLocation(), dz + z);
                        degrade(world, pos);
                    }
                }
            }

        });
    }

    private static void degrade(World world, BlockPos pos) {
        IBlockState bs = world.getBlockState(pos);
        Rule rule = ModConfig.ruleMap.get(bs);
        if (rule != null) {
            if (rule.probability() >= world.rand.nextDouble())
                world.setBlockState(pos, rule.output);
        }
    }

    private static Iterator<Chunk> getTickableChunks(World world) {
        try {
            WorldServer ws = world.getMinecraftServer().worlds[world.provider.getDimension()];
            return world.getPersistentChunkIterable(ws.getPlayerChunkMap().getChunkIterator());
        } catch (Exception e) {
            return null;
        }
    }
}

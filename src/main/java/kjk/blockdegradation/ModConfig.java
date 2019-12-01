package kjk.blockdegradation;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Config(modid = BlockDegradation.MODID)
public class ModConfig {
    @Config.Comment("Format: <input> <output> <half-life(in seconds)>")
    public static String[] rules = {
            "minecraft:stonebrick#0 minecraft:stonebrick#2 50400"
    };

    @Config.Ignore
    public static Map<IBlockState, Rule> ruleMap = new HashMap<>();

    public static void parse() {
        ruleMap.clear();
        for(String rule: rules) {
            String[] parts = rule.split(" ");
            IBlockState input = stringToState(parts[0]);
            IBlockState output = stringToState(parts[1]);
            double halflife = Double.parseDouble(parts[2]);
            ruleMap.put(input, new Rule(input, output, halflife));
        }
    }

    private static IBlockState stringToState(String bs) {
        String[] parts = bs.split("#");
        String name = parts[0];
        int meta = Integer.parseInt(parts[1]);
        return Block.getBlockFromName(name).getStateFromMeta(meta);
    }

    @Mod.EventBusSubscriber(modid = BlockDegradation.MODID)
    static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(BlockDegradation.MODID)) {
                ConfigManager.sync(BlockDegradation.MODID, Config.Type.INSTANCE);
                parse();
            }
        }
    }
}

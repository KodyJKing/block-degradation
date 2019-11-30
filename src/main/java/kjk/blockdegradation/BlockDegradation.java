package kjk.blockdegradation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = BlockDegradation.MODID, name = BlockDegradation.NAME, version = BlockDegradation.VERSION)
public class BlockDegradation {
    public static final String MODID = "blockdegradation";
    public static final String NAME = "Block Degradation";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ModEvents());
        ModConfig.parse();
    }
}

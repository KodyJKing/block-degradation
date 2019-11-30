package kjk.blockdegradation;

import net.minecraft.block.state.IBlockState;

public class Rule {

    public IBlockState input;
    public IBlockState output;
    public double halflife;

    public Rule(IBlockState input, IBlockState output, double halflife) {
        this.input = input;
        this.output = output;
        this.halflife = halflife;
    }

    public double probability() {
        int chunkletVolume = 4096; // = 16^3
        int attemptsPerChunkletPerTick = 3; // TODO: Parameterize this.
        int ticksPerSecond = 20;
        double attempts = halflife * ticksPerSecond * attemptsPerChunkletPerTick;
        double p = chunkletVolume * (1 - Math.pow(2, -1 / attempts));
        return p;
    }

}

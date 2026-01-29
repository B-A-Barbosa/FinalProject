import java.util.ArrayList;

public class BitCrusher extends AudioEffect {
    private int keepBits;

    public BitCrusher(int keepBits) {
        this.keepBits = keepBits;
    }

    private static short crushSample(short s, int keepBits) {
        int drop = 16 - keepBits;
        //make a int version of our sample to work with
        int v = s;
        //remove drop number of bits, and then shift back to replace those dropped bits with 0s. This reduces the quality of the sound by representing it with less bits.
        v = (v >> drop) << drop;
        return (short) v;
    }

    @Override
    public AudioClip applyEffect(AudioClip clip) {
        ArrayList<Short> out = new ArrayList<>(clip.getSamples());
        //go through each sample and crush it
        for (int i = 0; i < clip.getSamples().size(); i++) {
            out.set(i, crushSample(out.get(i), keepBits));
        }
        return new AudioClip(clip.getFileName() + "_bitcrush_" + keepBits + "bit", out, clip.getHeader().clone());
    }
}

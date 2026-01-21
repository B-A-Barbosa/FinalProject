import java.util.ArrayList;

public class BitCrusher extends AudioEffect {
    private short crushSample(short sample) {
        System.out.println("Original: " + sample + " Crushed: " + Math.round(sample / 4096.0) * 4096);
        return (short) ByteManager.ClampShort((int) (Math.round(sample / 4096.0) * 4096));
    }

    @Override
    public AudioClip applyEffect(AudioClip clip) {
        ArrayList<Short> newSamples = new ArrayList<>(clip.getSamples().size());
        for (Short sample : clip.getSamples()) {
            newSamples.add(crushSample(sample));
        }
        return new AudioClip(clip.getFileName() + "_bitCrushed", newSamples, clip.getHeader().clone());
    }
}
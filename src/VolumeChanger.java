import java.util.ArrayList;

public class VolumeChanger extends AudioEffect {
    private double volumeFactor;

    public VolumeChanger(double volumeFactor) {
        this.volumeFactor = volumeFactor;
    }
    
    @Override
    public AudioClip applyEffect(AudioClip clip) {
        ArrayList<Short> newSamples = new ArrayList<>(clip.getSamples().size());
        for (short sample :clip.getSamples()){
            short newSample = ByteManager.ClampShort((int) Math.round(sample * volumeFactor));
            newSamples.add(newSample);
        }
        if (volumeFactor > 1.0) {
            return new AudioClip(clip.getFileName() + "_louder", newSamples, clip.getHeader().clone());
        } else {
            return new AudioClip(clip.getFileName() + "_quieter", newSamples, clip.getHeader().clone());
        }
    }
}

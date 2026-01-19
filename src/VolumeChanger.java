import java.util.ArrayList;

public class VolumeChanger extends AudioEffect {
    private double volumeFactor;

    public VolumeChanger(double volumeFactor) {
        this.volumeFactor = volumeFactor;
    }
    public static int Clamp(int value){
        if (value > Short.MAX_VALUE) {
            return Short.MAX_VALUE;
        } else if (value < Short.MIN_VALUE) {
            return Short.MIN_VALUE;
        } else {
            return value;
        }
    }
    @Override
    public AudioClip applyEffect(AudioClip clip) {
        ArrayList<Short> newSamples = new ArrayList<>(clip.samples.size());
        for (short sample :clip.samples){
            int newSample = Clamp((int) Math.round(sample * volumeFactor));
            sample = (short) newSample;
            newSamples.add(sample);
        }
        return new AudioClip(newSamples, clip.header.clone());
    }
    
}

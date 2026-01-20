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
        ArrayList<Short> newSamples = new ArrayList<>(clip.getSamples().size());
        for (short sample :clip.getSamples()){
            int newSample = Clamp((int) Math.round(sample * volumeFactor));
            sample = (short) newSample;
            newSamples.add(sample);
        }
        if (volumeFactor > 1.0) {
            return new AudioClip(clip.getFileName() + "_louder", newSamples, clip.getHeader().clone());
        } else {
            return new AudioClip(clip.getFileName() + "_quieter", newSamples, clip.getHeader().clone());
        }
    }
}

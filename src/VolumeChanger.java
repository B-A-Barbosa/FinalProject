import java.util.ArrayList;
public class VolumeChanger extends AudioEffect {
    private double volumeFactor;

    public VolumeChanger(double volumeFactor) {
        this.volumeFactor = volumeFactor;
    }
    
    @Override
    public AudioClip applyEffect(AudioClip clip) {
        ArrayList<Short> newSamples = new ArrayList<>(clip.getSamples().size());
        //here we go through each sample and multiply it by the volume factor to increase or decrease the volume,
        //This is why compression happens when audio is increaed (and how overdrive works)
        //eventually if you keep increasing the volume, the samples will hit the max value of a short
        //this causes the audio to have less range and makes it sound distorted/clipped
        //we add the alterated samples to a new arraylist and then make a new AudioClip with it
        for (short sample :clip.getSamples()){
            short newSample = ByteManager.ClampShort((int) Math.round(sample * volumeFactor));
            newSamples.add(newSample);
        }
        if (volumeFactor > 1.0) {
            return new AudioClip(clip.getFileName() + "_louder", newSamples, clip.getHeader().clone());
        }
        else if (volumeFactor < 1.0){
            return new AudioClip(clip.getFileName() + "_quieter", newSamples, clip.getHeader().clone());
        }
        else{
            return new AudioClip(clip.getFileName(), newSamples, clip.getHeader().clone());
        }
    }
}
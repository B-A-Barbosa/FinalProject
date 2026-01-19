
public class PitchEffect extends AudioEffect {
    private final double pitchFactor;

    public PitchEffect(double pitchFactor) {
        this.pitchFactor = pitchFactor;
    }

    @Override
    public AudioClip applyEffect(AudioClip clip) {
        int originalSampleRate = clip.getSampleRate();
        clip.setSampleRate((int) Math.round(originalSampleRate * pitchFactor));

        return new AudioClip(clip.samples, clip.header.clone());
    }

    
}
public class PitchEffect extends AudioEffect {
    private double pitchFactor;

    public PitchEffect(double pitchFactor) {
        this.pitchFactor = pitchFactor;
    }

    @Override
    public AudioClip applyEffect(AudioClip clip) {
        int originalSampleRate = clip.getSampleRate();
        clip.setSampleRate((int) Math.round(originalSampleRate * pitchFactor));

        if (pitchFactor > 1.0) {
            return new AudioClip(clip.getFileName() + "_spedUp", clip.getSamples(), clip.getHeader().clone());
        }
        else{
            return new AudioClip(clip.getFileName() + "_slowedDown", clip.getSamples(), clip.getHeader().clone());
        }
    }
}
public class PitchEffect extends AudioEffect {
    private double pitchFactor;

    public PitchEffect(double pitchFactor) {
        this.pitchFactor = pitchFactor;
    }

    @Override
    public AudioClip applyEffect(AudioClip clip) {
        //To make a pitch effect, we can simply change the sample rate
        //the sample rate is how many samples are played per second (samples = "snapshots" of audio)
        //so by increasing the rate, we play more samples per second which makes it go by faster, this also makes the pitch higher because of frequency
        int originalSampleRate = clip.getSampleRate();
        clip.setSampleRate((int) Math.round(originalSampleRate * pitchFactor));

        //add to the file name to indicate the effect applied
        if (pitchFactor > 1.0) {
            return new AudioClip(clip.getFileName() + "_spedUp", clip.getSamples(), clip.getHeader().clone());
        }
        else if (pitchFactor < 1.0) {
            return new AudioClip(clip.getFileName() + "_slowedDown", clip.getSamples(), clip.getHeader().clone());
        }
        else{
            return new AudioClip(clip.getFileName(), clip.getSamples(), clip.getHeader().clone());
        }
    }
}
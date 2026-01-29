import java.util.ArrayList;

public class ReverseEffect extends AudioEffect{
    @Override
    public AudioClip applyEffect(AudioClip clip) {
        ArrayList<Short> newSamples = new ArrayList<>(clip.getSamples().size());
        // we get an arraylist of samples, and then litterally just add them in reverse order to a new arraylist
        //this plays the sound backwards
        for (int i = clip.getSamples().size() - 1; i >= 0; i--) {
            newSamples.add(clip.getSamples().get(i));
        }
        return new AudioClip(clip.getFileName() + "_reversed", newSamples, clip.getHeader().clone());
    }
}
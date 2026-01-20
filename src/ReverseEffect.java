import java.util.ArrayList;

public class ReverseEffect extends AudioEffect{
    @Override
    public AudioClip applyEffect(AudioClip clip) {
        ArrayList<Short> newSamples = new ArrayList<>(clip.getSamples().size());
        for (int i = clip.getSamples().size() - 1; i >= 0; i--) {
            newSamples.add(clip.getSamples().get(i));
        }
        return new AudioClip(clip.getFileName() + "_reversed", newSamples, clip.getHeader().clone());
    }
}
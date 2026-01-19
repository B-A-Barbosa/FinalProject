import java.util.ArrayList;

public class NothingDoer extends AudioEffect {

    public AudioClip applyEffect(AudioClip clip) {
        return new AudioClip(new ArrayList<>(clip.samples), clip.header);
    }

    
}

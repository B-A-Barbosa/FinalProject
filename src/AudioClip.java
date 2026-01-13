import java.util.ArrayList;
public class AudioClip {
    private ArrayList<Short> samples;
    
    public AudioClip(ArrayList<Short> samples){
        this.samples = samples;
    }
    
    public ArrayList<Short> getSamples(){
        return samples;
    }
}
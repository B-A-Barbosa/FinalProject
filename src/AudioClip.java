import java.util.ArrayList;
public class AudioClip {
    public ArrayList<Short> samples;
    public byte[] header;
    
    public AudioClip(ArrayList<Short> samples, byte[] header){
        this.samples = samples;
        this.header = header;
    }
    
    public ArrayList<Short> getSamples(){
        return samples;
    }
}
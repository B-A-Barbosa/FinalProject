import java.io.FileInputStream;
import java.util.ArrayList;

public class AudioReader {
    public static ArrayList<Short> ReadAudio(){
        try (FileInputStream in = new FileInputStream("Audio.wav")){
            ArrayList<Short> samples = new ArrayList<Short>();
            in.skip(44); //skip header
            while (in.available() > 0){
                int low = in.read(); //read low byte first
                int high = in.read();
                short sample = (short) ((high << 8) | low); //convert from LE to short
                samples.add(sample);
            }
            return samples;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
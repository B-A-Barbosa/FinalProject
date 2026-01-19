import java.io.FileInputStream;
import java.util.ArrayList;

public class AudioReader {
    public static AudioClip readAudio(String filePath) {
        try (FileInputStream in = new FileInputStream(filePath)) {
            byte[] header = in.readNBytes(44);

            ArrayList<Short> samples = new ArrayList<>();
            while (true) {
                int low = in.read();
                if (low == -1) break;
                int high = in.read();
                if (high == -1) break;
                short sample = (short) ((high << 8) | (low & 0xFF));
                samples.add(sample);
            }

            return new AudioClip(samples, header);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

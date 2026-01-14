import java.io.FileOutputStream;
import java.io.IOException;

public class FileCreator {
    public static void writeAudioClip(AudioClip clip, String filePath) throws IOException {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            //Write header bytes directly
            out.write(clip.header);

            //Write sample data in little endian format
            for (short sample : clip.getSamples()) {
                Util.writeLEShort(out, sample);
            }
        }
    }
}
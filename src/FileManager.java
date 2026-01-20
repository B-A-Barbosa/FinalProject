import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public final class FileManager {
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

            return new AudioClip(filePath, samples, header);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeAudioClipMinimal(AudioClip clip, String filePath) throws IOException {
        int numChannels = clip.getNumChannels();
        int sampleRate = clip.getSampleRate();
        int bitsPerSample = clip.getBitsPerSample();

        if (bitsPerSample != 16) {
            // Convert beforehand or change the writer to the correct bit depth.
            throw new IllegalStateException("This writer expects 16-bit PCM samples.");
        }

        try (FileOutputStream out = new FileOutputStream("export/" + filePath + ".wav")) {
            writePcm16Minimal(clip.getSamples(), sampleRate, numChannels, out);
        }
    }

    /** Writes a minimal RIFF/WAVE (fmt + data only) as signed 16-bit little-endian. */
    private static void writePcm16Minimal(List<Short> samples, int sampleRate, int numChannels, OutputStream out)
            throws IOException {

        final int bitsPerSample  = 16;
        final int bytesPerSample = bitsPerSample / 8; // 2
        final int blockAlign     = numChannels * bytesPerSample;
        final int byteRate       = sampleRate * blockAlign;

        final int dataBytes = samples.size() * bytesPerSample;
        final int chunkSize = 36 + dataBytes; // fileSize - 8

        // RIFF
        out.write(new byte[]{'R','I','F','F'});
        ByteManager.writeLEInt(out, chunkSize);
        out.write(new byte[]{'W','A','V','E'});

        // fmt
        out.write(new byte[]{'f','m','t',' '});
        ByteManager.writeLEInt(out, 16);                 // Subchunk1Size for PCM
        ByteManager.writeLEShort(out, (short)1);         // AudioFormat = 1 (PCM)
        ByteManager.writeLEShort(out, (short)numChannels);
        ByteManager.writeLEInt(out, sampleRate);
        ByteManager.writeLEInt(out, byteRate);
        ByteManager.writeLEShort(out, (short)blockAlign);
        ByteManager.writeLEShort(out, (short)bitsPerSample);

        // data
        ByteManager.writeASCII(out, "data");
        ByteManager.writeLEInt(out, dataBytes);

        // samples
        for (short s : samples) {
            ByteManager.writeLEShort(out, s); // little-endian, signed
        }
    }
}


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public final class FileCreator {
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
        Utilx.writeLEInt(out, chunkSize);
        out.write(new byte[]{'W','A','V','E'});

        // fmt
        out.write(new byte[]{'f','m','t',' '});
        Utilx.writeLEInt(out, 16);                 // Subchunk1Size for PCM
        Utilx.writeLEShort(out, (short)1);         // AudioFormat = 1 (PCM)
        Utilx.writeLEShort(out, (short)numChannels);
        Utilx.writeLEInt(out, sampleRate);
        Utilx.writeLEInt(out, byteRate);
        Utilx.writeLEShort(out, (short)blockAlign);
        Utilx.writeLEShort(out, (short)bitsPerSample);

        // data
        Utilx.writeASCII(out, "data");
        Utilx.writeLEInt(out, dataBytes);

        // samples
        for (short s : samples) {
            Utilx.writeLEShort(out, s); // little-endian, signed
        }
    }
}


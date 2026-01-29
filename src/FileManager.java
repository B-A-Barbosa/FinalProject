import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public final class FileManager {
    public static AudioClip readAudio(String filePath) {
        try (FileInputStream in = new FileInputStream(filePath)) {
            //read the header, (first 44 bytes)
            byte[] header = in.readNBytes(44);

            //create an arraylist to hold the samples
            ArrayList<Short> samples = new ArrayList<>();
            //this loop reads each number in the file two bytes at a time, and flips the order to make it little-endian.
            //It continues until -1 is returned, because that indicates the end of the file.
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
    
    public static void CreateFile(AudioClip clip, String targetPath) throws IOException {

        //make sure the clip is 16 bit pcm so that the writer works correctly
        if (clip.getBitsPerSample() != 16) {
            throw new IllegalStateException("This writer expects 16-bit PCM samples.");
        }

        //make a new file and 
        File f = new File(targetPath + ".wav");

        try (FileOutputStream out = new FileOutputStream(f)) {
            WriteFile(clip.getSamples(), clip.getSampleRate(), clip.getNumChannels(), out);
        }
    }

    //writes a wav file to the output stream using the paramters provided
    private static void WriteFile(List<Short> samples, int sampleRate, int numChannels, OutputStream out) throws IOException {
        //figuring out all the values needed for the header, whether theyre constant or calculated
        final int bitsPerSample = 16;
        final int bytesPerSample = bitsPerSample / 8; // 2
        final int blockAlign = numChannels * bytesPerSample;
        final int byteRate = sampleRate * blockAlign;
        final int dataBytes = samples.size() * bytesPerSample;
        final int chunkSize = 36 + dataBytes; //fileSize - 8

        //writing the first part of the header (what kind of file it is)
        out.write(new byte[]{'R','I','F','F'});
        ByteManager.writeLEInt(out, chunkSize);
        out.write(new byte[]{'W','A','V','E'});

        //writing the fmt part (parameters of the file)
        out.write(new byte[]{'f','m','t',' '});
        ByteManager.writeLEInt(out, 16);//subchunk1 size for PCM
        ByteManager.writeLEShort(out, (short)1);//PCM format (uncompressed)
        ByteManager.writeLEShort(out, (short)numChannels);
        ByteManager.writeLEInt(out, sampleRate);
        ByteManager.writeLEInt(out, byteRate);
        ByteManager.writeLEShort(out, (short)blockAlign);
        ByteManager.writeLEShort(out, (short)bitsPerSample);

        //write the data info
        ByteManager.writeASCII(out, "data");
        ByteManager.writeLEInt(out, dataBytes);

        //go through each sample and write it to the file using my little endian short writer
        for (short s : samples) {
            ByteManager.writeLEShort(out, s);
        }
    }
}


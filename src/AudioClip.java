import java.io.File;
import java.util.ArrayList;

public class AudioClip {
    private ArrayList<Short> samples;
    private byte[] header;
    private String fileName;

    public AudioClip(String filePathOrName, ArrayList<Short> samples, byte[] header){
        this.samples = samples;
        this.header = header;

        // Keep just the file name without directories, strip .wav (case-insensitive)
        String base = new File(filePathOrName).getName();
        this.fileName = base.replaceFirst("(?i)\\.wav$", "");
    }
    //apply the audio effect to the temporary clip, and then copy over all the data
    public void applyEffect(AudioEffect effect){
        AudioClip temp = effect.applyEffect(this);
        this.samples = temp.getSamples();
        this.header = temp.getHeader();
        this.fileName = temp.getFileName();
    }

    //getters
    public String getFileName(){ return fileName; }
    public ArrayList<Short> getSamples(){ return samples; }
    public byte[] getHeader(){ return header; }
    //getters using specific byte locations in the header
    public int getNumChannels() { return ByteManager.readUShortLE(header, 22); }
    public int getBitsPerSample() { return ByteManager.readUShortLE(header, 34); }
    public int getSampleRate() { return ByteManager.readUIntLE(header, 24); }
    
    //to set the sample rate, we go through the header bytes and set the appropriate bytes to the new sample rate and convert it to little endian
    public void setSampleRate(int sampleRate) {
        header[24] = (byte)(sampleRate & 0xFF);
        header[25] = (byte)((sampleRate >>> 8) & 0xFF);
        header[26] = (byte)((sampleRate >>> 16) & 0xFF);
        header[27] = (byte)((sampleRate >>> 24) & 0xFF);
    }
}

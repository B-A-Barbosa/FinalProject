import java.util.ArrayList;
public class AudioClip {
    private ArrayList<Short> samples;
    private byte[] header;
    private String fileName;

    public AudioClip(String filePath, ArrayList<Short> samples, byte[] header){
        this.samples = samples;
        this.header = header;
        this.fileName = filePath.replace(".wav", "");
    }

    public String getFileName(){
        return fileName;
    }
    public ArrayList<Short> getSamples(){
        return samples;
    }
    public byte[] getHeader(){
        return header;
    }
    
    public int getNumChannels() {
        return ByteManager.readUShortLE(header, 22);
    }

    public int getBitsPerSample() {
        return ByteManager.readUShortLE(header, 34);
    }
    
    public int getSampleRate() {
        return ByteManager.readUIntLE(header, 24);
    }
    public void setSampleRate(int sampleRate) {
        header[24] = (byte)(sampleRate & 0xFF);
        header[25] = (byte)((sampleRate >>> 8) & 0xFF);
        header[26] = (byte)((sampleRate >>> 16) & 0xFF);
        header[27] = (byte)((sampleRate >>> 24) & 0xFF);
    }
}
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
        return u16LE(header, 22);
    }

    public int getBitsPerSample() {
        return u16LE(header, 34);
    }

    private static int u16LE(byte[] b, int off) {
        return (b[off] & 0xFF) | ((b[off+1] & 0xFF) << 8);
    }
    
    public int getSampleRate() {
        return u32LE(header, 24);
    }
    public void setSampleRate(int sampleRate) {
        header[24] = (byte)(sampleRate & 0xFF);
        header[25] = (byte)((sampleRate >>> 8) & 0xFF);
        header[26] = (byte)((sampleRate >>> 16) & 0xFF);
        header[27] = (byte)((sampleRate >>> 24) & 0xFF);
    }

    //TODO what
    private static int u32LE(byte[] b, int off) {
        return (b[off] & 0xFF) | ((b[off+1] & 0xFF) << 8) | ((b[off+2] & 0xFF) << 16) | ((b[off+3] & 0xFF) << 24);
}


}
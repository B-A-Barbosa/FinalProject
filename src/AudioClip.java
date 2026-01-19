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

    private static int u32LE(byte[] b, int off) {
        return  (b[off] & 0xFF)
          | ((b[off+1] & 0xFF) << 8)
          | ((b[off+2] & 0xFF) << 16)
          | ((b[off+3] & 0xFF) << 24);
}


}
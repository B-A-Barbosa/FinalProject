import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    static FileOutputStream out = null;
    
    private static void WriteLEShort(Short value) throws IOException{
            out.write(value & 0xFF);
            out.write((value >> 8) & 0xFF);
    }
    private static void writeLEInt(int value) throws IOException{
            out.write(value & 0xFF);
            out.write((value >> 8) & 0xFF);
            out.write((value >> 16) & 0xFF);
            out.write((value >> 24) & 0xFF);
    }
    private static void WriteASCII(String s) throws IOException{
            out.write(s.getBytes("US-ASCII"));
    }

    public static void main(String[] args) {
        try {
            out = new FileOutputStream("sine.wav");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Non changing values:
        String RIFF = "RIFF";
        String fmt = "fmt ";
        String data = "data";
        String WAVE = "WAVE";
        short FormatType = 1; // PCM/uncompressed = 1
        int FileSize = 44; //44 for standard pcm file, more if metadata is added

        //possibly changing values:
        short NumChannels = 1; // Stereo = 2, Mono = 1
        int SampleRate = 44100; //cd quality
        short BitsPerSample = 16; // 8 bits = 8, 16 bits /cd quality = 16

        //changing values
        int DataSize = 0; //TODO figure out how to calculate

        //calculated values:
        int BtyeRate = SampleRate * NumChannels * BitsPerSample / 8;
        short BlockAlign = (short) (NumChannels * BitsPerSample / 8);

        
        try {
            WriteASCII(RIFF);
            writeLEInt((short) (FileSize - 8)); //TODO ??
            WriteASCII(WAVE);
            WriteASCII(fmt);
            WriteLEShort((short) 16);
            WriteLEShort(FormatType);
            WriteLEShort(NumChannels);
            writeLEInt(SampleRate);
            writeLEInt(BtyeRate);
            WriteLEShort(BlockAlign);
            WriteLEShort(BitsPerSample);
            WriteASCII(data);
            writeLEInt(DataSize);
            //TODO data



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Main {
    
    private static void writeLEShort(FileOutputStream out, short value) throws IOException{
            out.write(value & 0xFF);
            out.write((value >>> 8) & 0xFF);
    }
    private static void writeLEInt(FileOutputStream out, int value) throws IOException{
            out.write(value & 0xFF);
            out.write((value >>> 8) & 0xFF);
            out.write((value >>> 16) & 0xFF);
            out.write((value >>> 24) & 0xFF);
    }
    private static void writeASCII(FileOutputStream out, String s) throws IOException{
            out.write(s.getBytes(StandardCharsets.US_ASCII));
    }

    public static void main(String[] args) {
        //Non changing values:
        String RIFF = "RIFF";
        String fmt = "fmt ";
        String data = "data";
        String WAVE = "WAVE";
        short FormatType = 1; // PCM/uncompressed = 1
        //possibly changing values:
        short NumChannels = 1; // Stereo = 2, Mono = 1
        int SampleRate = 44100; //cd quality
        short BitsPerSample = 16; // 8 bits = 8, 16 bits /cd quality = 16
        
        int Duration = 5;

        //calculated values:
        int BtyeRate = SampleRate * NumChannels * BitsPerSample / 8; //bytes per second
        short BlockAlign = (short) (NumChannels * BitsPerSample / 8); //Bytes per sample
        int numSamples = SampleRate * Duration;

        //changing values
        int Frequency = 440; //440Hz is standard A note
        long DataSize = BlockAlign * numSamples; //Samplerate * duration = total # 0f samples. Multiply by BlockAlign for total bytes
        long FileSize = DataSize + 36; //DataSize + size of header - 8 bytes

        
        if (DataSize > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("File too large for standard WAV format");
        }

        try (FileOutputStream out = new FileOutputStream("Audio.wav")){
            writeASCII(out, RIFF);
            writeLEInt(out, (int) FileSize);
            writeASCII(out, WAVE);
            writeASCII(out, fmt);
            writeLEInt(out, 16);
            writeLEShort(out, FormatType);
            writeLEShort(out, NumChannels);
            writeLEInt(out, SampleRate);
            writeLEInt(out, BtyeRate);
            writeLEShort(out, BlockAlign);
            writeLEShort(out, BitsPerSample);
            writeASCII(out, data);
            writeLEInt(out, (int) DataSize);
            
            double amplitude = 0.9 * Short.MAX_VALUE; // 90% of max volume
            
            for (int i = 0; i < numSamples; i++){
              double time = i / (double) SampleRate;
              double sample = Math.sin(2.0 * Math.PI * Frequency * time);
              short pcmValue = (short) (sample * amplitude);
              writeLEShort(out, pcmValue);
            }
            ArrayList<Short> samples = AudioReader.ReadAudio();
            System.out.println(samples);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
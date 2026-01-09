import java.io.FileOutputStream;
import java.io.IOException;

public class AI {
    // Helper method: writes a 2‑byte number in LITTLE-ENDIAN format.
    // Little-endian means: low byte first, then high byte.
    // Example: 0x1234 becomes 0x34 0x12 in the file.
    private static void writeLEShort(FileOutputStream out, short value) throws IOException {
        out.write(value & 0xFF);        // Write lower 8 bits
        out.write((value >> 8) & 0xFF); // Write upper 8 bits
    }

    // Helper method: writes a 4‑byte integer in little-endian format.
    // Example: 0x11223344 becomes: 44 33 22 11 in the file.
    // WAV files require all multibyte numbers to be little-endian.
    private static void writeLEInt(FileOutputStream out, int value) throws IOException {
        out.write(value & 0xFF);          // byte 1 (least significant)
        out.write((value >> 8) & 0xFF);   // byte 2
        out.write((value >> 16) & 0xFF);  // byte 3
        out.write((value >> 24) & 0xFF);  // byte 4 (most significant)
    }

    // Helper method: writes text (like "RIFF", "WAVE", "fmt ") as ASCII bytes.
    // These are the literal words required in the WAV file format.
    private static void writeASCII(FileOutputStream out, String s) throws IOException {
        out.write(s.getBytes("US-ASCII")); // Convert characters to bytes
    }

    public static void main(String[] args) throws IOException {

        // ------------------------------------------------------------
        // Settings the user can change
        // ------------------------------------------------------------
        short formatType    = 1;        // 1 = PCM (uncompressed audio)
        short numChannels   = 1;        // 1 = mono, 2 = stereo
        int   sampleRate    = 44100;    // Samples per second (CD-quality)
        short bitsPerSample = 16;       // 16-bit audio sample
        double durationSec  = 5.0;      // Length in seconds
        double frequencyHz  = 440.0;    // Tone frequency: 440 Hz (A4)

        // ------------------------------------------------------------
        // WAV file derived values (calculated automatically)
        // ------------------------------------------------------------

        // Bytes per second of audio
        int byteRate = sampleRate * numChannels * bitsPerSample / 8;

        // Number of bytes per "frame" (one sample for each channel)
        short blockAlign = (short) (numChannels * bitsPerSample / 8);

        // Number of audio samples to generate
        int numSamples = (int) (durationSec * sampleRate);

        // Size of raw audio data in bytes
        int dataSize = numSamples * numChannels * (bitsPerSample / 8);

        // RIFF chunk size = total file size - 8 bytes
        int chunkSize = 36 + dataSize;  // (44-byte header - 8) + dataSize

        // ------------------------------------------------------------
        // Open a file called "sine.wav" for writing.
        // The file will appear in the SAME directory where you run the program.
        //
        // Example:
        // If you run:  java Main
        // Then the file appears next to Main.class.
        //
        // If you run inside IntelliJ or VS Code:
        // It appears inside the project root folder.
        // ------------------------------------------------------------
        try (FileOutputStream out = new FileOutputStream("sine.wav")) {

            // ------------------------------------------------------------
            // WRITE THE 44-BYTE WAV HEADER
            // ------------------------------------------------------------

            // --- RIFF chunk ---
            writeASCII(out, "RIFF");        // Chunk ID (must literally be "RIFF")
            writeLEInt(out, chunkSize);     // File size minus 8 bytes
            writeASCII(out, "WAVE");        // File format = "WAVE"

            // --- fmt subchunk ---
            writeASCII(out, "fmt ");        // Subchunk1 ID "fmt "
            writeLEInt(out, 16);            // Subchunk1Size = 16 for PCM
            writeLEShort(out, formatType);  // AudioFormat = 1 (PCM)
            writeLEShort(out, numChannels); // Mono or stereo
            writeLEInt(out, sampleRate);    // Samples per second
            writeLEInt(out, byteRate);      // Bytes per second
            writeLEShort(out, blockAlign);  // Bytes per sample frame
            writeLEShort(out, bitsPerSample); // Bits in one sample (8 or 16)

            // --- data subchunk ---
            writeASCII(out, "data");        // Subchunk2 ID "data"
            writeLEInt(out, dataSize);      // Number of bytes of audio data

            // ------------------------------------------------------------
            // Generate the sine wave sample data (the actual sound)
            // ------------------------------------------------------------

            double amplitude = 0.9 * Short.MAX_VALUE;  // Max amplitude for 16-bit

            for (int i = 0; i < numSamples; i++) {

                double t = (double) i / sampleRate; // Time in seconds for sample i

                // Sine wave formula: A * sin(2π f t)
                double sample = amplitude * Math.sin(2.0 * Math.PI * frequencyHz * t);

                // Convert double → 16‑bit integer
                short pcm = (short) Math.round(sample);

                // Write 2 bytes of audio
                writeLEShort(out, pcm);
            }
        }

        // Final message in console
        System.out.println("Wrote sine.wav (" + durationSec + "s @ " + frequencyHz + " Hz)");
    }
}

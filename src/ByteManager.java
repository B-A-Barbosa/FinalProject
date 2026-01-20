import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ByteManager {
    public static void writeLEShort(OutputStream out, short value) throws IOException{
        out.write(value & 0xFF);
        out.write((value >>> 8) & 0xFF);
    }
    public static void writeLEInt(OutputStream out, int value) throws IOException{
        out.write(value & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 24) & 0xFF);
    }
    public static void writeASCII(OutputStream out, String s) throws IOException{
        out.write(s.getBytes(StandardCharsets.US_ASCII));
    }
    //TODO convert to using byte writer class
    public static int readUShortLE(byte[] b, int off) {
        return (b[off] & 0xFF) | ((b[off+1] & 0xFF) << 8);
    }
    public static int readUIntLE(byte[] b, int off) {
        return (b[off] & 0xFF) | ((b[off+1] & 0xFF) << 8) | ((b[off+2] & 0xFF) << 16) | ((b[off+3] & 0xFF) << 24);
    }
}
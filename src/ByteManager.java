import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ByteManager {
    //The comments are ai generated explanations of the code, but my own explanations are found in the docs
    //writes a little-endian short to the output stream
    public static void writeLEShort(OutputStream out, short value) throws IOException{
        out.write(value & 0xFF);
        out.write((value >>> 8) & 0xFF);
    }
    //writes a little-endian int to the output stream
    public static void writeLEInt(OutputStream out, int value) throws IOException{
        out.write(value & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 24) & 0xFF);
    }
    public static void writeASCII(OutputStream out, String s) throws IOException{
        out.write(s.getBytes(StandardCharsets.US_ASCII));
    }
    //reads a little-endian unsigned short from the byte array at the given offset
    public static int readUShortLE(byte[] b, int off) {
        return (b[off] & 0xFF) | ((b[off+1] & 0xFF) << 8);
    }
    //reads a little-endian unsigned int from the byte array at the given offset
    public static int readUIntLE(byte[] b, int off) {
        return (b[off] & 0xFF) | ((b[off+1] & 0xFF) << 8) | ((b[off+2] & 0xFF) << 16) | ((b[off+3] & 0xFF) << 24);
    }
    //clamps an int value to the range of a short and returns it as a short
    public static short ClampShort(int value){
        if (value > Short.MAX_VALUE) {
            return Short.MAX_VALUE;
        } else if (value < Short.MIN_VALUE) {
            return Short.MIN_VALUE;
        } else {
            return (short) value;
        }
    }
    //clamps an int value to the range of a short and returns it as an int
    //this is used for calculations where we want to keep it as an int but ensure its in the short range
    public static int ClampInt(int value){
        if (value > Short.MAX_VALUE) {
            return Short.MAX_VALUE;
        } else if (value < Short.MIN_VALUE) {
            return Short.MIN_VALUE;
        } else {
            return value;
        }
    }
}
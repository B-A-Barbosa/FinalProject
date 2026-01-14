import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Util {
    public static void writeLEShort(FileOutputStream out, short value) throws IOException{
            out.write(value & 0xFF);
            out.write((value >>> 8) & 0xFF);
    }
    public static void writeLEInt(FileOutputStream out, int value) throws IOException{
            out.write(value & 0xFF);
            out.write((value >>> 8) & 0xFF);
            out.write((value >>> 16) & 0xFF);
            out.write((value >>> 24) & 0xFF);
    }
    public static void writeASCII(FileOutputStream out, String s) throws IOException{
            out.write(s.getBytes(StandardCharsets.US_ASCII));
    }
}

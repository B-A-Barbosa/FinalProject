import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Utilx {
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
}

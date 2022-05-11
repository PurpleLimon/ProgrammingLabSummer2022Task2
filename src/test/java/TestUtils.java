import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TestUtils {

    public static String genRandomKey() {
        int keyLength = (char)(Math.random() * 255 + 1);
        StringBuilder sb = new StringBuilder(keyLength);
        while (sb.length() < keyLength) {
            sb.append(Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE)));
        }
        sb.setLength(keyLength);
        return sb.toString();
    }

    public static void genRandomFile(String fileName) throws IOException {
        try (BufferedOutputStream randFile = new BufferedOutputStream(new FileOutputStream(fileName))) {
            int fileLength = (int) (Math.random() * 50 + 50);
            for (int j = 0; j < fileLength; j++) {
                randFile.write((char)(Math.random() * 255));
            }
        }
    }
}

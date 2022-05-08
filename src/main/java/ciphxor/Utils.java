package ciphxor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Utils {

    public interface FunctionVoid {
        void run() throws IOException;
    }

    public static class IO {
        public static String genOutputFileName(String base, String postfix) {
            String[] tokens = base.split("\\.(?=[^\\.]+$)");
            StringBuilder sb = new StringBuilder();
            sb.append(tokens[0]);
            sb.append(postfix);
            sb.append((tokens.length > 1)? "." + tokens[tokens.length - 1]: "");
            return sb.toString();
        }
        public static void UseArtifactDir(String dirName, FunctionVoid func) throws IOException {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdir();
            }

            func.run();

            File[] artifacts = dir.listFiles();
            if (artifacts != null) {
                for (File file : artifacts) {
                    file.delete();
                }
            }
            dir.delete();
        }
    }

    public static class StringUtils {
        public static int readHexAt(String str, int from, int to) {
            String number;
            if (to <= str.length()) {
                number = str.substring(from, to);
            } else {
                number = str.substring(from) + str.substring(0, to - str.length());
            }
            return Integer.parseInt(number, 16);
        }

        public static String genRandomKey() {
            int keyLength = (char)(Math.random() * 255 + 1);
            StringBuilder sb = new StringBuilder(keyLength);
            while (sb.length() < keyLength) {
                sb.append(Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE)));
            }
            sb.setLength(keyLength);
            return sb.toString();
        }
    }

}

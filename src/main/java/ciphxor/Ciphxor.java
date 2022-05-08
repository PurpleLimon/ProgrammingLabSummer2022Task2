package ciphxor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Queue;

public class Ciphxor {

    private final String inputFileName;
    private final String outputFileName;
    private final String key;

    public Ciphxor(String inputName, String key, String outputName) {
        inputFileName = inputName;
        this.key = key;
        if (outputName == null) {
            outputFileName = Utils.IO.genOutputFileName(inputName, "_o");
            return;
        }
        outputFileName = outputName;
    }

    public void applyAlgorithm() throws IOException {
        try (InputStream input = new FileInputStream(inputFileName)){
            try (OutputStream output = new FileOutputStream(outputFileName)) {
                byte[] inputBytes = new byte[1];
                int bytesRead = input.readNBytes(inputBytes, 0, 1);
                int keyIndex = 0;
                while (bytesRead > 0){
                    for (int i = 0; i < bytesRead; i ++) {
                        output.write(inputBytes[i] ^ Utils.StringUtils.readHexAt(key, keyIndex, keyIndex + 2));
                        keyIndex = (keyIndex + 2) % key.length();
                    }
                    bytesRead = input.readNBytes(inputBytes, 0, 1);
                }
            }
        }
    }

}

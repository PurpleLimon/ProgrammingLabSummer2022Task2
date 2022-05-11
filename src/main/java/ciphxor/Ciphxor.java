package ciphxor;

import java.io.*;

public class Ciphxor {

    private final String inputFileName;
    private final String outputFileName;
    private final HexKey key;

    public Ciphxor(String inputName, String key, String outputName) {
        inputFileName = inputName;
        this.key = new HexKey(key);
        outputFileName = outputName != null? outputName : IOUtils.genOutputFileName(inputName, "_o");
    }

    public void applyAlgorithm() throws IOException {
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(inputFileName))) {
            try (BufferedOutputStream output = new BufferedOutputStream (new FileOutputStream(outputFileName))) {
                byte[] inputBytes = new byte[key.getLength()];
                int bytesRead = input.readNBytes(inputBytes, 0, key.getLength());
                while (bytesRead > 0){
                    output.write(key.applyXorTo(inputBytes, bytesRead));
                    bytesRead = input.readNBytes(inputBytes, 0, key.getLength());
                }
            }
        }
    }

}

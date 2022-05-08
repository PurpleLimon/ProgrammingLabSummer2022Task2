import ciphxor.Utils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import ciphxor.Ciphxor;

public class Tests {
    private void assertEqualsFilesContent(String input, String expectedOutput) {
        try (
                InputStream inp = new FileInputStream(input);
                InputStream expOutp = new FileInputStream(expectedOutput)
        ) {
            var inpCh = inp.read();
            var expOutpCh = expOutp.read();
            while (inpCh == expOutpCh && inpCh != -1) {
                    inpCh = inp.read();
                    expOutpCh = expOutp.read();
                }
            assertEquals(inpCh, expOutpCh);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void assertNotEqualsFilesContent(String input, String unexpectedOutput) throws IOException {
        try (
                InputStream inp = new FileInputStream(input);
                InputStream expOutp = new FileInputStream(unexpectedOutput)
        ) {
            final double EQUALITY_THRESHOLD = 0.05; // [0;1]
            var inpCh = inp.read();
            var expOutpCh = expOutp.read();
            if (inpCh == -1 && expOutpCh == -1) {
                return;
            }

            double amount = 0;
            double amountEqualities = 0;
            do {
                if (inpCh == expOutpCh) {
                    amountEqualities++;
                }
                amount++;
                inpCh = inp.read();
                expOutpCh = expOutp.read();
            } while (inpCh != -1 && expOutpCh != -1);
            final double equalityPercentage = amountEqualities/amount;
            if (equalityPercentage > EQUALITY_THRESHOLD) {
                System.out.printf("[WARNING] Unexpectedly high equality rang of %f%n", equalityPercentage);
            }
        }
    }

    @Test
    void mainTests() throws IOException {
        String dirName = ".testArtifacts";
        Utils.IO.UseArtifactDir("src/inputs/" + dirName, () -> {
            final int TESTS_AMOUNT = 7;
            for(int i = 1; i <= TESTS_AMOUNT - 1; i++) {
                String key = Utils.StringUtils.genRandomKey();
                String inputName = String.format("src/inputs/input_%03d.txt", i);
                String encryptedName = String.format("src/inputs/%s/output_%03d_coded.txt",dirName, i);
                String decryptedName = String.format("src/inputs/%s/output_%03d_decoded.txt", dirName, i);

                //check if encrypts
                new Ciphxor(inputName, key, encryptedName).applyAlgorithm();
                assertNotEqualsFilesContent(inputName, encryptedName);

                //check if decrypts successfully
                new Ciphxor(encryptedName, key, decryptedName).applyAlgorithm();
                assertEqualsFilesContent(decryptedName, inputName);
            }

            final String KEY = Utils.StringUtils.genRandomKey();
            String inputName = String.format("src/inputs/input_%03d.txt", TESTS_AMOUNT);
            String encryptedName = String.format("src/inputs/input_%03d_o.txt", TESTS_AMOUNT);
            String decryptedName = String.format("src/inputs/input_%03d_o_decoded.txt",TESTS_AMOUNT);

            //check if encrypts
            new Ciphxor(inputName, KEY, null).applyAlgorithm();
            assertNotEqualsFilesContent(inputName, encryptedName);

            //check if decrypts successfully
            new Ciphxor(encryptedName, KEY, decryptedName).applyAlgorithm();
            assertEqualsFilesContent(decryptedName, inputName);
        });
    }

    @Test
    void randomTests() throws IOException {
        String dirName = ".testArtifacts";
        Utils.IO.UseArtifactDir("src/inputs/" + dirName, () -> {
            final int TESTS_AMOUNT = 20;
            for (int i = 1; i <= TESTS_AMOUNT; i++) {
                try (OutputStream randFile = new FileOutputStream(
                        String.format("src/inputs/%s/genInput_%03d.txt", dirName, i)
                )) {
                    int fileLength = (int) (Math.random() * 50 + 50);
                    for (int j = 0; j < fileLength; j++) {
                        randFile.write((char)(Math.random() * 255));
                    }
                }
            }

            for (int i = 1; i <= TESTS_AMOUNT - 1; i++) {
                String key = Utils.StringUtils.genRandomKey();
                //check if encrypted
                new Ciphxor(String.format("src/inputs/%s/genInput_%03d.txt", dirName, i),
                        key,
                        String.format("src/inputs/%s/output_%03d_coded.txt", dirName, i)).applyAlgorithm();
                assertNotEqualsFilesContent(
                        String.format("src/inputs/%s/genInput_%03d.txt", dirName, i),
                        String.format("src/inputs/%s/output_%03d_coded.txt", dirName, i)
                );

                //check if decrypted successfully
                new Ciphxor(String.format("src/inputs/%s/output_%03d_coded.txt", dirName, i),
                        key,
                        String.format("src/inputs/%s/output_%03d_decoded.txt", dirName, i)).applyAlgorithm();
                assertEqualsFilesContent(
                        String.format("src/inputs/%s/output_%03d_decoded.txt", dirName, i),
                        String.format("src/inputs/%s/genInput_%03d.txt", dirName, i)
                );
            }

            final String KEY = "0a00";
            new Ciphxor(String.format("src/inputs/%s/genInput_%03d.txt", dirName, TESTS_AMOUNT),
                    KEY,
                    String.format("src/inputs/%s/output_%03d_coded.txt", dirName, TESTS_AMOUNT)).applyAlgorithm();
            assertNotEqualsFilesContent(
                    String.format("src/inputs/%s/genInput_%03d.txt", dirName, TESTS_AMOUNT),
                    String.format("src/inputs/%s/output_%03d_coded.txt", dirName, TESTS_AMOUNT)
            );

            //check if decrypted successfully
            new Ciphxor(String.format("src/inputs/%s/output_%03d_coded.txt", dirName, TESTS_AMOUNT),
                    KEY,
                    String.format("src/inputs/%s/output_%03d_decoded.txt", dirName, TESTS_AMOUNT)).applyAlgorithm();
            assertEqualsFilesContent(
                    String.format("src/inputs/%s/output_%03d_decoded.txt", dirName, TESTS_AMOUNT),
                    String.format("src/inputs/%s/genInput_%03d.txt", dirName, TESTS_AMOUNT)
            );
        });
    }
}

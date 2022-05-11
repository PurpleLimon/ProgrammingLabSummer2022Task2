import ciphxor.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import ciphxor.Ciphxor;
import org.junit.jupiter.api.io.TempDir;

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

    private void singleTest (String inputName, String encryptedName, String decryptedName, String key) throws IOException {
        //check if encrypts
        new Ciphxor(inputName, key, encryptedName).applyAlgorithm();
        if (key.replace("0", "").isEmpty()) {
            assertEqualsFilesContent(inputName, encryptedName);
        } else {
            assertNotEqualsFilesContent(inputName, encryptedName);
        }

        //check if decrypts successfully
        new Ciphxor(encryptedName, key, decryptedName).applyAlgorithm();
        assertEqualsFilesContent(decryptedName, inputName);
    }

    private void singleTest (String inputName, String key) throws IOException {
        String tempInputCopy = tempDir.getPath() + "\\" + FilenameUtils.getName(inputName);
        FileUtils.copyFile(new File(inputName), new File(tempInputCopy));

        String encryptedName = IOUtils.genOutputFileName(tempInputCopy, "_o");
        String decryptedName = IOUtils.genOutputFileName(encryptedName, "_decoded");

        //check if encrypts
        new Ciphxor(tempInputCopy, key, null).applyAlgorithm();
        if (key.replace("0", "").isEmpty()) {
            assertEqualsFilesContent(inputName, encryptedName);
        } else {
            assertNotEqualsFilesContent(inputName, encryptedName);
        }

        //check if decrypts successfully
        new Ciphxor(encryptedName, key, decryptedName).applyAlgorithm();
        assertEqualsFilesContent(decryptedName, inputName);
    }

    @TempDir
    static File tempDir;

    static final String RES_PATH = "src/test/resources/inputs";

    @Test
    void mainTests() throws IOException {
        final int TESTS_AMOUNT = 7;
        for(int i = 1; i <= TESTS_AMOUNT; i++) {
            String key = TestUtils.genRandomKey();
            String inputName = String.format("%s/input_%03d.txt", RES_PATH, i);
            String encryptedName = String.format("%s/output_%03d_coded.txt", tempDir.getPath(), i);
            String decryptedName = String.format("%s/output_%03d_decoded.txt", tempDir.getPath(), i);

            singleTest(inputName, encryptedName, decryptedName, key);
        }
    }

    @Test
    void nullOutputNameTest() throws IOException {
        final int TESTS_AMOUNT = 7;
        for(int i = 1; i <= TESTS_AMOUNT; i++) {
            final String KEY = TestUtils.genRandomKey();
            String inputName = String.format("%s/input_%03d.txt", RES_PATH, i);
            singleTest(inputName, KEY);
        }
    }

    @Test
    void randomTests() throws IOException {
        final int TESTS_AMOUNT = 20;
        for (int i = 1; i <= TESTS_AMOUNT; i++) {
            TestUtils.genRandomFile(String.format("%s/genInput_%03d.txt", tempDir.getPath(), i));
        }

        for (int i = 1; i <= TESTS_AMOUNT - 1; i++) {
            String key = TestUtils.genRandomKey();
            String inputName = String.format("%s/genInput_%03d.txt", tempDir.getPath(), i);
            String encryptedName = String.format("%s/output_%03d_coded.txt", tempDir.getPath(), i);
            String decryptedName = String.format("%s/output_%03d_decoded.txt", tempDir.getPath(), i);

            singleTest(inputName, encryptedName, decryptedName, key);
        }

        final String KEY = "0000";

        String inputName = String.format("%s/genInput_%03d.txt", tempDir.getPath(), TESTS_AMOUNT);
        String encryptedName = String.format("%s/output_%03d_coded.txt", tempDir.getPath(), TESTS_AMOUNT);
        String decryptedName = String.format("%s/output_%03d_decoded.txt", tempDir.getPath(), TESTS_AMOUNT);
        singleTest(inputName, encryptedName, decryptedName, KEY);
    }
}

package ciphxor;

import org.kohsuke.args4j.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

class CiphxorLauncher {

    @Argument(required = true, metaVar = "Encryption", usage = "HEX-value key for encryption/decryption")
    private String key;

    @Argument(index = 1, required = true, metaVar = "InputName", usage = "Input file name")
    private String inputName;

    @Option(name = "-o", metaVar = "OutputName", usage = "Output file name")
    private String outputName;

    private CmdLineParser parser;

    private void printError(Throwable e) {
        System.err.println(e.getMessage());
        System.err.println("java -jar ciphxor.jar key InputName [-o OutputName]");
        parser.printUsage(System.err);
    }

    public static void main(String[] args) {
        new CiphxorLauncher().launch(args);
    }

    private void launch(String[] args) {
        parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            printError(e);
            return;
        }

        for (char ch : key.toCharArray()) {
            try {
                Integer.parseInt(Character.toString(ch), 16);
            } catch (NumberFormatException e) {
                printError(new CmdLineException(parser, "HEX value expected", new IllegalArgumentException()));
                return;
            }
        }

        if (!new File(inputName).isFile()) {
            printError(new CmdLineException(parser, "No such file found", new IllegalArgumentException()));
            return;
        }

        try {
            Paths.get(outputName);
        } catch (InvalidPathException e) {
            printError(new CmdLineException(parser, "Invalid output file name given", e));
            return;
        }

        Ciphxor ciphxor = new Ciphxor(inputName, key, outputName);

        try {
            ciphxor.applyAlgorithm();
        } catch (IOException e) {
            printError(e);
        }
    }

}

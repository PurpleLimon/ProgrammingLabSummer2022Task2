package ciphxor;

import org.apache.commons.io.FilenameUtils;

public class IOUtils {
    public static String genOutputFileName(String base, String postfix) {
        return FilenameUtils.removeExtension(base) + postfix + "." + FilenameUtils.getExtension(base);
    }
}

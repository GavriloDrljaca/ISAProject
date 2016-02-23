package src.main.java.util;

import java.io.File;
import java.io.IOException;

public class HrFileUtil {
	public static void createFolderIfNotExist(String path) throws IOException {
        final File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to create " + dir.getAbsolutePath());
        }
    }

}

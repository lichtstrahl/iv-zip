package iv.zip.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileManager {

    public static FileManager create() {
        return new FileManager();
    }

    public boolean exist(String fullPath) {
        return new File(fullPath).exists();
    }

    public boolean isDir(String fullPath) {
        return new File(fullPath).isDirectory();
    }
}

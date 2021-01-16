package iv.zip.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileManager {

    public static FileManager create() {
        return new FileManager();
    }

    public List<File> listChildFiles(File dir) {
        List<File> allFiles = new ArrayList<>();

        for (File file : Optional.ofNullable(dir.listFiles()).orElse(new File[0])) {
            if (file.isFile()) {
                allFiles.add(file);
            } else {
                List<File> innerFiles = listChildFiles(file);
                allFiles.addAll(innerFiles);
            }
        }

        return allFiles;
    }
}

package iv.zip.archivator;

import iv.zip.IvZipException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Archivator {
    private static final int FILE_COUNT_LIMIT = 1;

    private List<String> fileNames;

    public static Archivator create(String ... files) {
        Archivator archivator = new Archivator(Arrays.asList(files));
        archivator.checkCountFiles(files.length);
        return archivator;
    }

    // ---
    // PRIVATE
    // ---

    private void checkCountFiles(int count) {
        if (count != FILE_COUNT_LIMIT)
            throw new IvZipException(
                    String.format("Передано файлов: %d. А поддерживается только %d", count, FILE_COUNT_LIMIT)
            );
    }
}

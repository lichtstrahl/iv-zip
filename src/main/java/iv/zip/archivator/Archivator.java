package iv.zip.archivator;

import iv.zip.IvZipException;
import iv.zip.file.FileManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Archivator {
    private static final int FILE_COUNT_LIMIT = 1;

    private List<String> fileNames;
    private FileManager fileManager;
    private String outputZipFilePath;

    public static Archivator create(String ... files) {
        Archivator archivator = new Archivator(
                Arrays.asList(files),
                FileManager.create(),
                "D:/output.zip"
        );
        archivator.checkCountFiles(files.length);
        return archivator;
    }

    // Сжать все переданные файлы
    public void zipAll() {
        fileNames.stream()
                .filter(fileManager::exist)
                .filter(fileManager::isDir)
                .map(File::new)
                .forEach(this::zipDirectory);
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

    private void zipDirectory(File inputDir) {
        // Создание дочерних директорий для будущего zip-файла
        File zipFile = new File(outputZipFilePath);
        zipFile.getParentFile().mkdirs();

        String inputDirPath = inputDir.getAbsolutePath();

        try (
                FileOutputStream outputFile = new FileOutputStream(zipFile);
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputFile);
        ) {
            for (File file : fileManager.listChildFiles(inputDir)) {
                String absolutePath = file.getAbsolutePath();
                System.out.println("Zipping: " + absolutePath);

                String entryName = absolutePath.substring(inputDirPath.length() + 1);
                ZipEntry zipEntry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(zipEntry);
                addDataToZip(absolutePath, zipOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Добавление данных в архив
    private void addDataToZip(String absoluteFile, OutputStream zipOutputStream) {
        try (
                FileInputStream inputStream = new FileInputStream(absoluteFile)
        ) {
            IOUtils.copyLarge(inputStream, zipOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

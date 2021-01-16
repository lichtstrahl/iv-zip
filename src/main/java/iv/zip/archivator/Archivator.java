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
    private List<String> filePaths;
    private FileManager fileManager;
    private String outputZipFilePath;

    public static Archivator create(String ... files) {
        Archivator archivator = new Archivator(
                Arrays.asList(files),
                FileManager.create(),
                "D:/output.zip"
        );
        return archivator;
    }

    // Сжать все переданные файлы
    public void zipAll() {
        // Создание дочерних директорий для будущего zip-файла
        File zipFile = new File(outputZipFilePath);
        zipFile.getParentFile().mkdirs();

        try (
                FileOutputStream outputFile = new FileOutputStream(zipFile);
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputFile);
        ) {
            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (file.exists()) {
                    if (file.isDirectory())
                        zipDir(file, zipOutputStream);
                    if (file.isFile())
                        zipFile(file, file.getName(), zipOutputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---
    // PRIVATE
    // ---

    private void zipDir(File inputDir, ZipOutputStream zipOutputStream) throws IOException {
        String inputDirPath = inputDir.getAbsolutePath();

        for (File file : fileManager.listChildFiles(inputDir)) {
            zipFile(file, file.getAbsolutePath().substring(inputDirPath.length()+1), zipOutputStream);
        }
    }

    private void zipFile(File file, String fileName, ZipOutputStream zipOutputStream) throws IOException {
        String absolutePath = file.getAbsolutePath();
        System.out.println("Zipping: " + absolutePath);

        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);
        addDataToZip(absolutePath, zipOutputStream);
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

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
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Archivator {
    private static final String DEFAULT_OUTPUT_FILE = "output.zip";

    private List<String> filePaths;
    private FileManager fileManager;
    private String outputZipFilePath;      // Вывод результата в файл, без pipe
    private boolean pipeOut;               // Использовать ли pipe в качестве выхода

    public static Archivator createDefault(String ... files) {
        // Убираем первый элемент. Т.к. это флаг
        List<String> fileList = Arrays.asList(files);
        fileList.remove(0);

        return new Archivator(
                fileList,
                FileManager.create(),
                DEFAULT_OUTPUT_FILE,
                false
        );
    }

    public static Archivator createPipe(String ... files) {
        return new Archivator(
                Arrays.asList(files),
                FileManager.create(),
                null,
                true
        );
    }

    // Сжать все переданные файлы
    public void zipAll() {
        try (
                FileOutputStream outputFile = pipeOut ? null : new FileOutputStream(prepareOutput());
                ZipOutputStream zipOutputStream = new ZipOutputStream(
                        Optional.ofNullable((OutputStream)outputFile).orElse(System.out)
                )
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

    // Подготовка вывода
    private File prepareOutput() {
        // Создание дочерних директорий для будущего zip-файла
        File zipFile = new File(outputZipFilePath);
        zipFile.getParentFile().mkdirs();

        return zipFile;
    }
}

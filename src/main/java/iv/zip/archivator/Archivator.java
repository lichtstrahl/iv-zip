package iv.zip.archivator;

import iv.zip.Logger;
import iv.zip.ZipStreamHolder;
import iv.zip.file.FileManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
        List<String> fileList = Arrays.asList(files).subList(1, files.length);

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
                FileOutputStream outputFile = pipeOut ? null : new FileOutputStream(new File(outputZipFilePath));
                ZipOutputStream zipOutputStream = new ZipOutputStream(
                        Optional.ofNullable((OutputStream)outputFile).orElse(System.out)
                )
        ) {
            ZipStreamHolder zipStreamHolder = ZipStreamHolder.create(zipOutputStream);
            filePaths.stream()
                    .map(File::new)
                    .filter(File::exists)
                    .forEach(file -> {
                        if (file.isDirectory())
                            zipDir(file, zipStreamHolder);
                        if (file.isFile())
                            zipStreamHolder.writeZipData(file.getName(), file);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unzip(File outputDir, InputStream inputStream) {
        if (!outputDir.exists())
            outputDir.mkdirs();

        try (
                ZipInputStream zipStream = new ZipInputStream(inputStream)
        ) {
            for (ZipEntry entry = zipStream.getNextEntry(); Objects.nonNull(entry); entry = zipStream.getNextEntry()) {
                String entryName = entry.getName();
                String outFileName = outputDir.getAbsolutePath() + File.separator + entryName;
                System.out.println("Unzip: " + outFileName);

                File parentDir = new File(outFileName).getParentFile();
                if (!parentDir.exists())
                    parentDir.mkdirs();
                getDataFromZip(outFileName, zipStream);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---
    // PRIVATE
    // ---

    private void zipDir(File inputDir, ZipStreamHolder zipStreamHolder) {
        final String inputDirPath = inputDir.getAbsolutePath();
        final int inputDirPathLength = inputDirPath.length()+1;

        fileManager.listChildFiles(inputDir)
                .stream()
                .forEach(file -> {
                    Logger.INSTANCE.log("File: %s, thread: %s\n", file.getName(), Thread.currentThread().getName());
                    zipStreamHolder.writeZipData(file.getAbsolutePath().substring(inputDirPathLength), file);
                });
    }

    // Чтение данных из архива
    private void getDataFromZip(String absolutePath, InputStream zipInputStream) {
        try (
                FileOutputStream outputStream = new FileOutputStream(absolutePath)
                ) {
            IOUtils.copyLarge(zipInputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

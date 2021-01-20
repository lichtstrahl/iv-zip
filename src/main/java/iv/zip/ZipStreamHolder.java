package iv.zip;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipStreamHolder {

    private ZipOutputStream zipOutputStream;

    public static ZipStreamHolder create(ZipOutputStream stream) {
        ZipStreamHolder holder = new ZipStreamHolder();

        holder.zipOutputStream = stream;

        return holder;
    }

    // Сжатие отдельного файла. Метод synchronyzed для будущей многопоточности
    public synchronized void writeZipData(String fileName, File inputFile) {
        String absoluteInputPath = inputFile.getAbsolutePath();

        try (
                FileInputStream inputStream = new FileInputStream(absoluteInputPath)
        ) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);
            IOUtils.copyLarge(inputStream, zipOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

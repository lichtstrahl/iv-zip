package iv.zip;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StreamHolder {

    private ZipOutputStream zipOutputStream;

    public static StreamHolder create(ZipOutputStream stream) {
        StreamHolder holder = new StreamHolder();

        holder.zipOutputStream = stream;

        return holder;
    }

    public synchronized void writeZipData(String fileName, InputStream inputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);
        IOUtils.copyLarge(inputStream, zipOutputStream);
    }

}

package iv.zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public enum Logger {
    INSTANCE;

    private FileOutputStream outputStream;
    Logger() {
        File file = new File("log.txt");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException notFoundException) {
            notFoundException.printStackTrace();
        }
    }

    public synchronized void log(String format, Object ... args) {
        String msg = String.format(format, args);

        try {
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

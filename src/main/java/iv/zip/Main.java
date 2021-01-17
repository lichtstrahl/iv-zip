package iv.zip;

import iv.zip.archivator.Archivator;

import java.io.File;
import java.io.IOException;

public class Main {

    // Можно передать ключ --non-pipe и тогда архиватор оформит выход в виде файла output.zip по умолчнию.
    // Передавать флаг можно только первым аргументом
    private static final String NON_PIPE_KEY = "--non-pipe";

    public static void main(String[] args) {
        switch (inputPipePocessing()) {
            case ZIP:
                Archivator archivator = args[0].equals(NON_PIPE_KEY)
                        ? Archivator.createDefault(args)
                        : Archivator.createPipe(args);
                archivator.zipAll();
                break;

            case UNZIP:
                Archivator unzipper = Archivator.createPipe();
                unzipper.unzip(new File("./output"), System.in);
                break;
        }
    }

    // Обработка входного pipe
    private static TypeProcess inputPipePocessing() {
        try {
            int available = System.in.available();

            return available == 0 ? TypeProcess.ZIP : TypeProcess.UNZIP;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return TypeProcess.ZIP;
    }


    private enum TypeProcess {
        ZIP, UNZIP
    }
}

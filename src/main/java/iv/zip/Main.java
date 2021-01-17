package iv.zip;

import iv.zip.archivator.Archivator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    // Можно передать ключ --non-pipe и тогда архиватор оформит выход в виде файла output.zip по умолчнию.
    // Передавать флаг можно только первым аргументом
    private static final String NON_PIPE_KEY = "--non-pipe";

    public static void main(String[] args) {
        boolean needZip = inputPipePocessing();

        if (needZip) {
            Archivator archivator = args[0].equals(NON_PIPE_KEY)
                    ? Archivator.createDefault(args)
                    : Archivator.createPipe(args);
            archivator.zipAll();
        }
    }

    // Обработка входного pipe
    private static boolean inputPipePocessing() {
        try {
            int available = System.in.available();
            System.out.println("In available " + available);

            return available == 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean hasInputPipe() {
        try (
                InputStreamReader isReader = new InputStreamReader(System.in)
        ) {
            return isReader.ready();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int sizeInputPipe() {
        try {
            return System.in.available();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
}

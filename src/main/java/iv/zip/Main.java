package iv.zip;

import iv.zip.archivator.Archivator;

import java.util.Arrays;

public class Main {

    // Можно передать ключ --non-pipe и тогда архиватор оформит выход в виде файла output.zip по умолчнию.
    private static final String NON_PIPE_KEY = "--non-pipe";

    public static void main(String args[]) {
        checkArgsCount(args);

        boolean nonPipe = Arrays.asList(args).contains(NON_PIPE_KEY);

        Archivator archivator = nonPipe
                ? Archivator.createDefault(args)
                : Archivator.createPipe(args);
        archivator.zipAll();
    }

    private static void checkArgsCount(String[] args) {
        if (args.length == 0) {
            throw new IvZipException("Нужно передать параметры");
        }
    }
}

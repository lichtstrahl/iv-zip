package iv.zip;

import iv.zip.archivator.Archivator;

public class Main {

    // Можно передать ключ --non-pipe и тогда архиватор оформит выход в виде файла output.zip по умолчнию.
    // Передавать флаг можно только первым аргументом
    private static final String NON_PIPE_KEY = "--non-pipe";

    public static void main(String[] args) {
        checkArgsCount(args);

        Archivator archivator = args[0].equals(NON_PIPE_KEY)
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

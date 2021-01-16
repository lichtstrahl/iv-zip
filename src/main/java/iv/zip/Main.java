package iv.zip;

import iv.zip.archivator.Archivator;

public class Main {
    public static void main(String args[]) {
        checkArgsCount(args);

        Archivator.create(args);
    }

    private static void checkArgsCount(String[] args) {
        if (args.length == 1)
            System.out.println("Нужно передать параметры");
    }
}

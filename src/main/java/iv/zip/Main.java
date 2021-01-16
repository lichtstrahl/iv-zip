package iv.zip;

import iv.zip.archivator.Archivator;

public class Main {
    public static void main(String args[]) {
        checkArgsCount(args);

        Archivator archivator = Archivator.create(args);
        archivator.zipAll();
    }

    private static void checkArgsCount(String[] args) {
        if (args.length == 0)
            System.out.println("Нужно передать параметры");
    }
}

package iv.zip;

import java.util.Arrays;

public class Main {
    public static void main(String args[]) {
        System.out.println("Hello, World!");

        Arrays.stream(args)
                .forEach(Main::printArg);
    }

    private static void printArg(String arg) {
        System.out.printf("Arg: %s%n", arg);
    }
}

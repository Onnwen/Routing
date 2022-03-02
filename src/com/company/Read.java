package com.company;

import java.util.Scanner;

public class Read {
    public static int getInt(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\uDBC0\uDD8A ");

        try {
            int input = scanner.nextInt();
            if (input >= min && input <= max) {
                return input;
            }
        } catch(Exception e) {}

        return getInt(min, max);
    }

    public static String getString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

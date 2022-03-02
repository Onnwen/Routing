package com.company;

import java.util.Scanner;
import java.util.UUID;

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
        System.out.print("\uDBC0\uDD8A ");

        return scanner.nextLine();
    }

    public static UUID getUUID() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\uDBC0\uDD8A ");
        try {
            return UUID.fromString(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("\uDBC0\uDD84 UUID invalido.");
            return getUUID();
        }
    }
}

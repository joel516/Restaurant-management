package com.rest.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import com.rest.models.Reservation;

/** Safe console input helpers (never crash on bad input). */
public class Input {
    private static final Scanner SC = new Scanner(System.in);

    public static String text(String prompt) {
        System.out.print(prompt);
        return SC.hasNextLine() ? SC.nextLine().trim() : "";
    }

    public static int integer(String prompt) {
        while (true) {
            String s = text(prompt);
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("  Please enter a valid whole number."); }
            if (!SC.hasNextLine()) return 0;
        }
    }

    public static double decimal(String prompt) {
        while (true) {
            String s = text(prompt);
            try { return Double.parseDouble(s); }
            catch (NumberFormatException e) { System.out.println("  Please enter a valid number."); }
            if (!SC.hasNextLine()) return 0;
        }
    }

    public static LocalDateTime dateTime(String prompt) {
        while (true) {
            String s = text(prompt);
            try { return LocalDateTime.parse(s, Reservation.FMT); }
            catch (DateTimeParseException e) { System.out.println("  Use format yyyy-MM-dd HH:mm (e.g. 2026-10-25 19:30)."); }
            if (!SC.hasNextLine()) return LocalDateTime.now();
        }
    }
}

package utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class utils {

    public static void clear() {

        for(int i = 0; i < 50; i++)
        {
            System.out.println();
        }
        /*
            System.out.print("\033[H\033[2J");
            System.out.flush();

         */
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("^\\d*$");
    }

    public static int getRange(Map<String, Integer> map, int start, int end, String input)
    {
        //default outputs are -1 if invalid, then based on map, return value.
        //first assert that input is numeric
        if(map.containsKey(input))
        {
            return map.get(input);
        }
        if(assertInt(input))
        {
            if(Integer.parseInt(input) >= start && Integer.parseInt(input) <= end)
            {
                return Integer.parseInt(input);
            }
        }
        return -1;
    }

    public static boolean assertInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean assertFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean assertIntRange(int min, int max, String input) {
        try {
            int value = Integer.parseInt(input);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean assertFloatRange(float min, float max, String input) {
        try {
            float value = Float.parseFloat(input);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void waitForKey() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

}

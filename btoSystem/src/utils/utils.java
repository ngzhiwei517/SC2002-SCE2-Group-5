package utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        if(isNumeric(input))
        {
            if(Integer.parseInt(input) >= start && Integer.parseInt(input) <= end)
            {
                return Integer.parseInt(input);
            }
        }
        return -1;
    }

}

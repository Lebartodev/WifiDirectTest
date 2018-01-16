package com.example.lebarto.wifidirecttest.actions;

/**
 * Created by lebarto on 03.01.2018.
 */

public class WordCount {
    public static int process(String str) {
        int res = 0;
        String[] wordArray =
            str.trim().split("\\s+");
        res += wordArray.length;
        return res;
    }
}

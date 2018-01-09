package com.example.lebarto.wifidirecttest.actions;

/**
 * Created by lebarto on 03.01.2018.
 */

public class WordCount {
    public static int proccess(String str) {
        int res = 0;
        for (int i = 0; i < 30; i++) {
            String[] wordArray =
                str.trim().split("\\s+");
            res += wordArray.length;
        }
        return res;
    }
}

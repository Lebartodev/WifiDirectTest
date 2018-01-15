package com.example.logic_model.util;

import com.example.logic_model.model.Action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 11.01.2018.
 */

public class ActionUtil {

    public static Action<String> fromTextFile(String path) throws IOException {
        List<String> res = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            for (int i = 0; i < 10000; i++) {
                line = reader.readLine();
                res.add(line);
            }
        }
        Action<String> resAction = new Action();
        return resAction.setData(res);
    }
}

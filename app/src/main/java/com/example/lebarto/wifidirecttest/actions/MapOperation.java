package com.example.lebarto.wifidirecttest.actions;

import java.io.Serializable;

/**
 * Created by lebarto on 10.01.2018.
 */

public class MapOperation implements Operation, Serializable {
    SAM s2;

    public MapOperation(Object obj) {
        s2 = (SAM & Serializable) obj;
    }

    @Override
    public void process(Action o) {
        o.setResult(s2.action(o.getResult()));
    }

    @FunctionalInterface
    public interface SAM {
        Object action(Object s);
    }
}

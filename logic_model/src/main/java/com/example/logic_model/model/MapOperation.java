package com.example.logic_model.model;

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
    public Object process(Object o) {
        return s2.action(o);
    }

    public interface SAM {
        Object action(Object s);
    }
}

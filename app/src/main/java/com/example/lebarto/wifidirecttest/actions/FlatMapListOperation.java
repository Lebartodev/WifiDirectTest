package com.example.lebarto.wifidirecttest.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 10.01.2018.
 */

public class FlatMapListOperation implements Operation, Serializable {
    ListSAM listOperator;

    public FlatMapListOperation(Object obj) {
        listOperator = (ListSAM & Serializable) obj;
    }

    @Override
    public Object process(Object o) {
        List<Object> result = new ArrayList<>();
        for (Object o1 : (List<Object>) o) {
            result.addAll(listOperator.action(o1));
        }
        return result;
    }

    public interface ListSAM {
        List<Object> action(Object s);
    }
}

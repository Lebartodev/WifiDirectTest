package com.example.logic_model.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 10.01.2018.
 */

public class FlatMapOperation implements Operation, Serializable {
    SAM operator;

    public FlatMapOperation(Object obj) {
        operator = (SAM & Serializable) obj;
    }

    @Override
    public Object process(Object o) {
        List<Object> result = new ArrayList<>();
        for (Object o1 : (List<Object>) o) {
            if (operator != null) {
                result.add(operator.action(o1));
            }
        }
        return result;
    }

    public interface SAM {
        Object action(Object s);
    }
}

package com.example.logic_model.model.functions;

import com.example.logic_model.model.Action;

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
    public void process(Action o) {
        List<Object> result = new ArrayList<>();
        for (Object o1 : (List<Object>) o.getResult()) {
            if (operator != null) {
                result.add(operator.action(o1));
            }
        }

        o.setResult(result);
    }

    @FunctionalInterface
    public interface SAM<T, R> extends Serializable {
        R action(T s);
    }
}

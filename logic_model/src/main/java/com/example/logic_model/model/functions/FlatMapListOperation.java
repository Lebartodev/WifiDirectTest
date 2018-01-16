package com.example.logic_model.model.functions;

import com.example.logic_model.model.Action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 10.01.2018.
 */

public class FlatMapListOperation implements Operation, Serializable {
    private ListSAM listOperator;

    public FlatMapListOperation(Object obj) {
        listOperator = (ListSAM & Serializable) obj;
    }

    @Override
    public void process(Action o) {
        List<Object> result = new ArrayList<>();
        for (Object o1 : (List<Object>) o.getResult()) {
            result.addAll(listOperator.action(o1));
        }

        o.setResult(result);
    }

    @FunctionalInterface
    public interface ListSAM<T, R> extends Serializable {
        List<R> action(T s);
    }
}

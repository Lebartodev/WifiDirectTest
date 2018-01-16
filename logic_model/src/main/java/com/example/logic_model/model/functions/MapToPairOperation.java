package com.example.logic_model.model.functions;

import com.example.logic_model.model.Action;
import com.example.logic_model.model.Tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 10.01.2018.
 */

public class MapToPairOperation implements Operation, Serializable {
    private PairFunction mapOperator;

    public MapToPairOperation(Object obj) {
        mapOperator = (PairFunction & Serializable) obj;
    }

    @Override
    public void process(Action o) {
        List<Tuple> tuples = new ArrayList<>();
        List<Object> data = (List<Object>) o.getResult();
        for (Object datum : data) {
            tuples.add(mapOperator.action(datum));
        }
        o.setResult(tuples);
    }

    @FunctionalInterface
    public interface PairFunction<T, K, V> extends Serializable {
        Tuple<K, V> action(T t);
    }
}

package com.example.logic_model.model;

import com.example.logic_model.model.functions.FlatMapListOperation;
import com.example.logic_model.model.functions.FlatMapOperation;
import com.example.logic_model.model.functions.MapOperation;
import com.example.logic_model.model.functions.MapToPairOperation;
import com.example.logic_model.model.functions.Operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 10.01.2018.
 */

public class Action<S> implements Serializable {
    private List<S> data;
    private List<Operation> operations = new ArrayList<>();
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    private Action<S> add(Operation operation) {
        operations.add(operation);
        return this;
    }

    public <R> Action<S> map(MapOperation.SAM<S, R> obj) {
        return add(new MapOperation(obj));
    }

    public <R> Action<S> flatMapList(FlatMapListOperation.ListSAM<S, R> obj) {
        return add(new FlatMapListOperation(obj));
    }

    public <R> Action<R> flatMap(FlatMapOperation.SAM<S, R> obj) {
        return (Action<R>) add(new FlatMapOperation(obj));
    }

    public <T, K, V> Action<S> mapToPair(MapToPairOperation.PairFunction<T, K, V> obj) {
        return add(new MapToPairOperation(obj));
    }

    public Action setData(List<S> data) {
        this.data = data;
        return this;
    }

    public Action collect() {
        return this;
    }

    public List<S> getData() {
        return data;
    }

    public Object process() {
        result = data;
        for (Operation operation : operations) {
            operation.process(this);
        }
        return result;
    }
}

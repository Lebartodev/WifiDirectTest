package com.example.logic_model.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 10.01.2018.
 */

public class Action<T extends Serializable> implements Serializable {
    private List<T> data;
    private List<Operation> operations = new ArrayList<>();

    public List<Operation> getOperations() {
        return operations;
    }

    public Action add(Operation operation) {
        operations.add(operation);
        return this;
    }

    public Action setData(List<T> data) {
        this.data = data;
        return this;
    }

    public Action collect() {
        return this;
    }

    public List<T> getData() {
        return data;
    }


    public Object process() {
        Object o = data;
        for (Operation operation : operations) {
            o = operation.process(o);
        }
        return o;
    }
}

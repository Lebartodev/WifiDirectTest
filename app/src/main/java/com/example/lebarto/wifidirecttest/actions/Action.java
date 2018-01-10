package com.example.lebarto.wifidirecttest.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 10.01.2018.
 */

public class Action implements Serializable {
    private Serializable data;
    private List<Operation> operations = new ArrayList<>();

    public Action add(Operation operation) {
        operations.add(operation);
        return this;
    }

    public Action setData(Serializable data) {
        this.data = data;
        return this;
    }

    public Object process() {
        Object o = data;
        for (Operation operation : operations) {
            o = operation.process(o);
        }
        return o;
    }
}

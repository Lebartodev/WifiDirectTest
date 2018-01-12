package com.example.lebarto.wifidirecttest.actions;

import com.example.lebarto.wifidirecttest.model.Client;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 10.01.2018.
 */

public class Action<T extends Serializable> implements Serializable {
    private List<T> data;
    private List<Operation> operations = new ArrayList<>();
    private Object result;

    public Action add(Operation operation) {
        operations.add(operation);
        return this;
    }

    public Action<T> map(Object obj) {
        Object a = obj;
        operations.add(new MapOperation(a));
        return this;
    }

    public Action<T> flatMapList(Object obj) {
        operations.add(new FlatMapListOperation(obj));
        return this;
    }
    public Action<T> flatMap(Object obj) {
        operations.add(new FlatMapOperation(obj));
        return this;
    }

    public Action setData(List<T> data) {
        this.data = data;
        return this;
    }

    public void send(List<Client> clients) throws IOException {
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).getOos().writeObject(prepareAction(i, clients.size()));
            clients.get(i).getOos().flush();
        }
    }

    public Action sendAndCompute(List<Client> clients) throws IOException {
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).getOos().writeObject(prepareAction(i, clients.size() + 1));
            clients.get(i).getOos().flush();
        }
        return prepareAction(clients.size() + 1, clients.size() + 1);
    }

    public Action collect() {
        return this;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    private Action<T> prepareAction(int current, int count) {
        Action<T> action = new Action<T>();
        action.operations = new ArrayList<>(this.operations);
        int startIndex = data.size() / count * current;
        int endIndex = data.size() / count * (current + 1) - 1;
        action.data = new ArrayList<>(this.data.subList(startIndex, endIndex));
        return action;
    }

    public List<T> getData() {
        return data;
    }

    public Object getResult() {
        return result;
    }

    public Object process() {
        result = data;
        for (Operation operation : operations) {
            operation.process(this);
        }
        return result;
    }
}

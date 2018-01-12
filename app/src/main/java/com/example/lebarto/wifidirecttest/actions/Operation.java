package com.example.lebarto.wifidirecttest.actions;

import java.io.Serializable;

/**
 * Created by lebarto on 10.01.2018.
 */

public interface Operation<T extends Serializable> {
    void process(Action<T> action);
}

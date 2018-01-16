package com.example.logic_model.model.functions;

import com.example.logic_model.model.Action;

import java.io.Serializable;

/**
 * Created by lebarto on 10.01.2018.
 */

public interface Operation {
     void process(Action action);
}

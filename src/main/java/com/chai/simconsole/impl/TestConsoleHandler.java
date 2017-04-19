package com.chai.simconsole.impl;

import com.chai.simconsole.api.ConsoleHandler;

import java.io.PrintStream;

/**
 * Created by chaishipeng on 2017/4/19.
 */
public class TestConsoleHandler implements ConsoleHandler {
    public String getHelp() {
        return "test for input test";
    }

    public void _test(String args, PrintStream ps){
        ps.println(args);
    }
}

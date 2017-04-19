package com.chai.simconsole.api;

import java.io.PrintStream;

/**
 * Created by chaishipeng on 2017/4/19.
 */
public interface Console {

    String readLine();

    void start();

    PrintStream getPrinter();

}

package com.chai.simconsole.impl;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by chaishipeng on 2017/4/19.
 */
public class MultiPrintStream extends PrintStream {
    public MultiPrintStream(OutputStream out) {
        super(out, true);
    }
}

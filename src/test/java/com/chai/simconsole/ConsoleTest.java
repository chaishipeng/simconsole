package com.chai.simconsole;

import com.chai.simconsole.api.Console;
import com.chai.simconsole.api.ConsoleHandler;
import com.chai.simconsole.impl.CommandConsole;
import com.chai.simconsole.impl.TcpConsole;
import com.chai.simconsole.impl.TestConsoleHandler;

import java.io.PrintStream;

/**
 * Created by chaishipeng on 2017/4/19.
 */
public class ConsoleTest {

    public static void main(String[] args) throws InterruptedException {
        CommandConsole commandConsole = new CommandConsole();
        commandConsole.addConsoleHandler(new TestConsoleHandler());
        commandConsole.start();

        Thread.currentThread().sleep(1000000);


    }
}

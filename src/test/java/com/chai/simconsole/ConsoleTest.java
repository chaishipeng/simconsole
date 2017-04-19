package com.chai.simconsole;

import com.chai.simconsole.api.Console;
import com.chai.simconsole.impl.TcpConsole;

/**
 * Created by chaishipeng on 2017/4/19.
 */
public class ConsoleTest {

    public static void main(String[] args) throws InterruptedException {
        final Console console = new TcpConsole();
        console.start();
        new Thread(new Runnable() {
            public void run() {
                while(true){
                    String line  = console.readLine();
                    console.getPrinter().println("Read:" + line);
                }
            }
        }).start();

        Thread.currentThread().sleep(1000000);


    }
}

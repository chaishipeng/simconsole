package com.chai.simconsole.impl;

import com.chai.simconsole.api.Console;
import com.chai.simconsole.api.ConsoleHandler;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaishipeng on 2017/4/19.
 */
public class CommandConsole implements Console {

    private TcpConsole tcpConsole = new TcpConsole();

    private int port;

    private List<ConsoleHandler> consoleHandlerList = new ArrayList<ConsoleHandler>();

    private boolean isRun;

    private String prefix = "command:";

    public void addConsoleHandler (ConsoleHandler consoleHandler) {
        consoleHandlerList.add(consoleHandler);
    }

    public String readLine() {
        throw new RuntimeException("Not support");
    }

    public void start() {
        isRun = true;
        tcpConsole.start();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while(isRun){
                    tcpConsole.getPrinter().print(prefix);
                    String line = tcpConsole.readLine();
                    handlerLine(line);
                    tcpConsole.getPrinter().println();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void handlerLine(String line){
        if (line!=null && line.trim().length() <= 0) {
            return;
        }
        String[] commands = line.split(" ");
        String command = commands[0];
        String args = null;
        if (commands.length > 1){
            args = commands[1];
        }
        callHandler(command, args);
    }

    private void callHandler(String command, String args){
        for (ConsoleHandler consoleHandler : consoleHandlerList){
            if ("help".equals(command)){
                tcpConsole.getPrinter().println(consoleHandler.getHelp());
                continue;
            }
            Method method = null;
            try {
                method = consoleHandler.getClass().getMethod("_" + command, String.class, PrintStream.class);
                method.invoke(consoleHandler, args, tcpConsole.getPrinter());
            } catch (NoSuchMethodException e) {
                tcpConsole.getPrinter().println("No Method" + command + "!!!");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public PrintStream getPrinter() {
        return tcpConsole.getPrinter();
    }

    public void setPort(int port) {
        this.port = port;
        tcpConsole.setPort(port);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

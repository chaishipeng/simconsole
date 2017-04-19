package com.chai.simconsole.impl;

import com.chai.simconsole.api.Console;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chaishipeng on 2017/4/19.
 */
public class TcpConsole implements Console {

    private boolean isRunning;

    private BlockingQueue<String> lineQ = new ArrayBlockingQueue<String>(1);

    private MultiOutputStream multiOutputStream = new MultiOutputStream();

    private MultiPrintStream multiPrintStream = new MultiPrintStream(multiOutputStream);

    private ExecutorService consoleExecutorService = Executors.newFixedThreadPool(20);

    private int port = 8089;

    public String readLine() {
        try {
            return lineQ.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void start() {
        isRunning = true;
        multiOutputStream.addOutputStream(System.out);
        startSystemConsole();
        if (port > 0){
            startTcpConsole();
        }
    }

    public PrintStream getPrinter() {
        return multiPrintStream;
    }

    private void startSystemConsole(){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                readLineByIs(System.in);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void startTcpConsole() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while(isRunning){
                    try {
                        final Socket socket = serverSocket.accept();
                        consoleExecutorService.submit(new Runnable() {
                            public void run() {
                                try {
                                    readLineBySocket(socket);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void readLineBySocket(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStream os = socket.getOutputStream();
        multiOutputStream.addOutputStream(os);
        boolean isRun = true;
        while(isRun){
            try {
                String consoleLine = br.readLine();
                if (consoleLine == null) {
                    isRun = false;
                    multiOutputStream.removeOutputStream(os);
                    continue;
                }
                lineQ.put(consoleLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readLineByIs(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        boolean isRun = true;
        while(isRun){
            try {
                String consoleLine = br.readLine();
                if (consoleLine == null) {
                    isRun = false;
                    continue;
                }
                lineQ.put(consoleLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setPort(int port) {
        this.port = port;
    }
}

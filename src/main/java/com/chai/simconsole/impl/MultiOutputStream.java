package com.chai.simconsole.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaishipeng on 2017/4/19.
 */
public class MultiOutputStream extends OutputStream {

    private List<OutputStream> osList = new ArrayList<OutputStream>();

    public void addOutputStream(OutputStream os){
        osList.add(os);
    }

    public void removeOutputStream(OutputStream os){
        osList.remove(os);
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream outputStream : osList){
            outputStream.write(b);
        }
    }
}

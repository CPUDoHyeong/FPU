package com.bigcoach.fpu;

import java.io.IOException;
import java.io.OutputStream;

public class SerialWriter implements Runnable {
    OutputStream out;

    public SerialWriter(OutputStream out) {
        this.out = out;
    }

    public void run() {
        try {
        	//byte[] datas = new byte[100];
            //int c = System.in.read(datas);
//            String value = new String(datas, 0, c-2);
//            int send = Integer.parseInt(value);
//            this.out.write(datas);
            int c;
            while ((c = System.in.read()) != -1) {
                this.out.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}

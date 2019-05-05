package com.bigcoach.fpu;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

public class Serial {
	private SerialReader reader;
	DBController db;
	
	public SerialReader getReader() {
		return reader;
	}

	public void setReader(SerialReader reader) {
		this.reader = reader;
	}

	public SerialWriter getWriter() {
		return writer;
	}

	public void setWriter(SerialWriter writer) {
		this.writer = writer;
	}

	private SerialWriter writer;
	
    public Serial(DBController db) {
        super();
        this.db = db;
        
    }

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                reader = new SerialReader(in, db);
                writer = new SerialWriter(out);
                
                new Thread(reader).start();
                new Thread(writer).start();

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }
}
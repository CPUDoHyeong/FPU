package com.bigcoach.fpu;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame{
	JFrame mainFrame;
	JPanel backPn;
	BasePn basePn;
	SetupPn setupPn;
	DBController db;
	
	NoticePanel noticePn;
	
	CardLayout cards;
	
	Serial serial;
	CurrentTime currentTime;
	Scheduler scheduler;
	
	public Frame() {
		db = new DBController();
		serial = new Serial(db);
		
		noticePn = new NoticePanel();
		basePn = new BasePn(this, serial, noticePn);		// 등교 하교가 있는 패널
		setupPn = new SetupPn(this, serial, noticePn, db);	// 등록 삭제가 있는 패널
		
		mainFrame = new JFrame("Attendance");
		backPn = new JPanel();					// 기본 패널
		
		// 시간을 표시하는 스레드 시작
		currentTime = new CurrentTime(basePn);
		new Thread(currentTime).start();
		
		// 매일 정해진 시간에 실행되는 스레드 시작
		scheduler = new Scheduler(db);
		
		try {
            serial.connect("/dev/ttyUSB0");
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		cards = new CardLayout(0, 0);			// 카드 레이아웃 생성
		backPn.setLayout(cards);
		
		// set Frame
		mainFrame.setSize(800, 480);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);
		
		// add component to the backPn
		backPn.add(basePn, "basePn");
		backPn.add(setupPn, "setupPn");
		
		// show mainFrame
		mainFrame.add(backPn);
		mainFrame.setUndecorated(true);
		mainFrame.setVisible(true);
		
		cards.show(backPn, "basePn");
		
		backPn.add(noticePn);		
		
		backPn.setComponentZOrder(noticePn, 2);
		backPn.setComponentZOrder(setupPn, 1);
		backPn.setComponentZOrder(basePn, 1);
	}
}

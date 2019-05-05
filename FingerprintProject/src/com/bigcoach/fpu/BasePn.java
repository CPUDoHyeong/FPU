package com.bigcoach.fpu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BasePn extends JPanel implements ActionListener {
	private Frame frame;
	
	private SerialReader reader;
	private SerialWriter writer;
	private Serial serial;

	private ImageButton setupBtn;
	private ImageButton arriveBtn, departureBtn;
	
	private JLabel arriveLb, departureLb;
	public JLabel timeLb;
	
	private NoticePanel noticePn;
	public static int flag;
	
	public static String arriveTime;	// DB등교시간(시:분:초)
	public static String goToSchoolDate;// 하교 시 00시가 자났을 경우를 대비하여 비교하기위한 시간(년:월:일)
	public static String date;			// 오늘의 날
	public static String time;			// 현재의 시간(시:분:초)
	public static int dayOfWeek;		// 요일을 나타내는 숫자(1:일요일 ~ 7:토요일)
	

	// set button icon
	private ImageIcon baseIcon = new ImageIcon("img/baseSettings.png");
	private ImageIcon pressedIcon = new ImageIcon("img/pressedSettings.png");
	private ImageIcon baseSchool = new ImageIcon("img/baseClock.png");
	private ImageIcon pressedSchool = new ImageIcon("img/pressedClock.png");
	private ImageIcon baseHome = new ImageIcon("img/baseHome.png");
	private ImageIcon pressedHome = new ImageIcon("img/pressedHome.png");
	
	public BasePn(Frame frame, Serial serial, NoticePanel noticePn) {
		this.frame = frame;
		this.serial = serial;
		this.noticePn = noticePn;
		noticePn.getCancelBtn().addActionListener(this);
		
		this.reader = serial.getReader();
		this.writer = serial.getWriter();

		setLayout(null);
		setBackground(Final.BASIC_BACKGROUND_COLOR);
		
		// timeLb
		timeLb = new JLabel("0000-00-00 00:00:00");
		timeLb.setBounds(250, 10, 300, 50);
		timeLb.setVerticalAlignment(SwingConstants.CENTER);
		timeLb.setHorizontalAlignment(SwingConstants.CENTER);
		timeLb.setFont(new Font("Monospaced", Font.PLAIN, 24));
		timeLb.setForeground(Color.white);
		this.add(timeLb);		
				
		// schoolBtn
		arriveBtn = new ImageButton(baseSchool, pressedSchool);
		arriveBtn.setBounds(181, 151, 128, 128); // 145 181
		arriveBtn.addActionListener(this);
		this.add(arriveBtn);
		
		// arrivedLb
		arriveLb = new JLabel("등 교");
		arriveLb.setBounds(181, 279, 128, 50);
		arriveLb.setVerticalAlignment(SwingConstants.CENTER);
		arriveLb.setHorizontalAlignment(SwingConstants.CENTER);
		arriveLb.setFont(Final.font);
		arriveLb.setForeground(Color.white);
		this.add(arriveLb);
		
		// homeBtn
		departureBtn = new ImageButton(baseHome, pressedHome);
		departureBtn.setBounds(490, 151, 128, 128); // 490 526
		departureBtn.addActionListener(this);
		this.add(departureBtn);
		
		// departureLb
		departureLb = new JLabel("하 교");
		departureLb.setBounds(490, 279, 128, 50);
		departureLb.setVerticalAlignment(SwingConstants.CENTER);
		departureLb.setHorizontalAlignment(SwingConstants.CENTER);
		departureLb.setForeground(Color.white);
		departureLb.setFont(Final.font);
		this.add(departureLb);

		// setupBtn
		setupBtn = new ImageButton(baseIcon, pressedIcon);
		setupBtn.setBounds(Final.CHANGE[0], Final.CHANGE[1], Final.CHANGE[2], Final.CHANGE[3]);
		setupBtn.addActionListener(this);
		this.add(setupBtn);
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == setupBtn) {
			frame.cards.show(frame.backPn, "setupPn");
		} else if(e.getSource() == arriveBtn) {
			flag = Final.ARRIVE_MODE;
			write();
		} else if(e.getSource() == departureBtn) {
			flag = Final.DEPARTURE_MODE;
			write();
		} else if(e.getSource() == noticePn.getCancelBtn()) {
			// 취소 버튼 이벤트
			setEnable();
		}
	}
	
	public void write() {
		if(writer == null) {
			writer = serial.getWriter();
		}
		
		try {
			this.setComponentZOrder(noticePn, 0);
			writer.out.write(50);
			writer.out.write(10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		NoticePanel.changeCancleBtnText("취소");
		noticePn.setVisible(true);
		setDisable();
	}
	
	public void setEnable() {
		arriveBtn.setEnabled(true);
		departureBtn.setEnabled(true);
		setupBtn.setEnabled(true);
	}
	
	public void setDisable() {
		arriveBtn.setEnabled(false);
		departureBtn.setEnabled(false);
		setupBtn.setEnabled(false);
	}
	
}

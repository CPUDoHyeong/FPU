package com.bigcoach.fpu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class NoticePanel extends JPanel{
	
	private JPanel messagePn;
	public static RoundButton cancelBtn;
	public static JLabel view;
	
	public static void changeCancleBtnText(String str) {
		cancelBtn.setText(str);
	}
	
	public RoundButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(RoundButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	// 지문인식기의 알림을 사용자한테 보여주기위한 패널
	public NoticePanel() {
		this.setSize(800, 480);
		this.setBackground(new Color(0, 0, 0, 122));
		this.setLayout(null);
		
		messagePn = new JPanel();
		messagePn.setLayout(null);
		messagePn.setBounds(200, 110, 400, 260);
		messagePn.setBackground(Final.BASIC_BACKGROUND_COLOR);
		this.add(messagePn);
		
		// view label
		view = new JLabel();
		view.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		view.setForeground(Color.white);
		view.setBounds(0, 0, 400, 260);
		view.setText("Start");
		view.setVerticalAlignment(SwingConstants.CENTER);
		view.setHorizontalAlignment(SwingConstants.CENTER);
		messagePn.add(view);
		
		// cancel button
		cancelBtn = new RoundButton("취소");
		cancelBtn.setBounds(280, 200, 110, 50);
		cancelBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		messagePn.add(cancelBtn);
	}
}

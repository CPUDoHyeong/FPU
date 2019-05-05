package com.bigcoach.fpu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SetupPn extends JPanel implements ActionListener, ListSelectionListener {
	private int flag = 0;
	private DBController db;
	
	private Frame frame;
	
	private SerialReader reader;
	private SerialWriter writer;
	private Serial serial;
	
	private ImageButton enrollBtn, deleteBtn;
	private JLabel enrollLb, deleteLb;
	private ImageButton exitBtn;
	
	private JPanel dataPn;
	private JList jlist;
	private JScrollPane jsp;
	
	private NoticePanel noticePn;
	
	private DefaultListModel<String> setData = new DefaultListModel<>();

	// set button icon
	private ImageIcon baseIcon = new ImageIcon("img/baseExit.png");
	private ImageIcon pressedIcon = new ImageIcon("img/pressedExit.png");
	private ImageIcon baseEnroll = new ImageIcon("img/baseEnroll.png");
	private ImageIcon pressedEnroll = new ImageIcon("img/pressedEnroll.png");
	private ImageIcon baseDelete = new ImageIcon("img/baseDelete.png");
	private ImageIcon pressedDelete = new ImageIcon("img/pressedDelete.png");

	public SetupPn(Frame frame, Serial serial, NoticePanel noticePn, DBController db) {
		this.frame = frame;
		this.serial = serial;
		this.noticePn = noticePn;
		this.db = db;
		noticePn.getCancelBtn().addActionListener(this);
		
		this.serial = serial;
		this.reader = serial.getReader();
		this.writer = serial.getWriter();
		
		setLayout(null);
		// setBackground(new Color(245, 245, 245));
		setBackground(Final.BASIC_BACKGROUND_COLOR);
		
		// enroll
		enrollBtn = new ImageButton(baseEnroll, pressedEnroll);
		enrollBtn.setBounds(181, 151, 128, 128);
		enrollBtn.addActionListener(this);
		this.add(enrollBtn);
		
		// enrollLb
		enrollLb = new JLabel("지문 등록");
		enrollLb.setBounds(181, 279, 128, 50);
		enrollLb.setVerticalAlignment(SwingConstants.CENTER);  // 세로가운데정렬
		enrollLb.setHorizontalAlignment(SwingConstants.CENTER);// 가로가운데정렬
		enrollLb.setFont(Final.font);
		enrollLb.setForeground(Color.white);
		this.add(enrollLb);
		
		// delete
		deleteBtn = new ImageButton(baseDelete, pressedDelete);
		deleteBtn.setBounds(490, 151, 128, 128);
		deleteBtn.addActionListener(this);
		this.add(deleteBtn);
		
		// deleteLb
		deleteLb = new JLabel("지문 삭제");
		deleteLb.setBounds(490, 279, 128, 50);
		deleteLb.setVerticalAlignment(SwingConstants.CENTER);
		deleteLb.setHorizontalAlignment(SwingConstants.CENTER);
		deleteLb.setFont(Final.font);
		deleteLb.setForeground(Color.white);
		this.add(deleteLb);

		// exitBtn
		exitBtn = new ImageButton(baseIcon, pressedIcon);
		exitBtn.setBounds(Final.CHANGE[0], Final.CHANGE[1], Final.CHANGE[2], Final.CHANGE[3]);
		exitBtn.addActionListener(this);
		this.add(exitBtn);
	
		// listPn
		dataPn = new JPanel();
		dataPn.setLayout(null);
		dataPn.setBackground(Final.BASIC_BACKGROUND_COLOR);
		// dataPn.setBounds(400, 0, 400, 480);
		
		jlist = new JList();
		jsp = new JScrollPane(jlist);
		jlist.setFixedCellHeight(50);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// jlist.setBorder(BorderFactory.createLineBorder(Color.black, 1)); // set border
		jlist.setFont(new Font(Final.FONT, Font.BOLD, 20));
		jlist.addListSelectionListener(this);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) jlist.getCellRenderer(); // center
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		jsp.setBounds(35, 50, 305, 380);
		jsp.getViewport().setBorder(null);
		jsp.setBorder(null);
		jsp.getVerticalScrollBar().setPreferredSize(new Dimension(30, 0));
		dataPn.add(jsp);
		this.add(dataPn);
		dataPn.setVisible(false);
	}

	public void actionPerformed(ActionEvent e) {
		
		// 나가기 버튼 이벤트
		if(e.getSource() == exitBtn) {
			frame.cards.show(frame.backPn, "basePn");
			dataPn.setVisible(false);
			if(!deleteBtn.isVisible()) {
				deleteBtn.setVisible(true);	
			}
			
		} else if(e.getSource() == enrollBtn) {
			// 등록하기 버튼 이벤트
			this.setComponentZOrder(noticePn, 0);
			flag = Final.ENROLL_MODE;
			if(dataPn.isVisible()) {
				dataPn.setVisible(false);
				deleteBtn.setVisible(true);
				deleteLb.setVisible(true);
				exitBtn.setVisible(true);
			} else {
				// show dataPanel
				dataPn.setBounds(400, 0, 400, 480);
				
				db.setEnrollList(setData); // set JList
				checkJList();
				
				setScrollBarPostionReset();
				dataPn.setVisible(true);
				deleteBtn.setVisible(false);
				deleteLb.setVisible(false);
				exitBtn.setVisible(false);
			}
			
		} else if(e.getSource() == deleteBtn) {
			// 삭제하기 버튼 이벤트
			
			this.setComponentZOrder(noticePn, 0);
			flag = Final.DELETE_MODE;
			if(dataPn.isVisible()) {
				dataPn.setVisible(false);
				enrollBtn.setVisible(true);
				enrollLb.setVisible(true);
				exitBtn.setVisible(true);
			} else {
				// show dataPanel
				dataPn.setBounds(30, 0, 400, 480);
				db.setDeleteList(setData); // set JList
				checkJList();
				jlist.setEnabled(false); // 삭제 기능 막아둠
				setScrollBarPostionReset();
				dataPn.setVisible(true);
				enrollBtn.setVisible(false);
				enrollLb.setVisible(false);
				exitBtn.setVisible(false);
			}
			
		} else if(e.getSource() == noticePn.getCancelBtn()) {
			// 취소 버튼 이벤트
			// 999를 보냄으로써 취소한다
			byte[] cancleArr = setChar(Final.CANCLE_NUM);
			try {
				if(writer == null) {
					writer = serial.getWriter();
				}
				for(int i = 0; i < cancleArr.length; i++) {
					writer.out.write(cancleArr[i]);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} 	
			disableNoticePn();
		}
	}
	
	@Override
	// list selection listener
	public void valueChanged(ListSelectionEvent e) {
		// -1인 경우는 아무것도 선택되지 않은 경우
		// 선택되었을 때만 처리한다
		if(jlist.getSelectedIndex() != -1) {
			NoticePanel.changeCancleBtnText("취소");
			String selected = (String)jlist.getSelectedValue();
			if(selected != null) {
				String[] div = selected.split(".  ");
				int id = Integer.parseInt(div[0]);
				byte[] writeArr = setChar(id);
				
				for(int i = 0; i < writeArr.length; i++) {
					System.out.println(writeArr[i]);
				}
				
				
				if(flag == Final.ENROLL_MODE) {
					startEnrollEvent(writeArr);
				} else if(flag == Final.DELETE_MODE) {
					startDeleteEvent(writeArr);
				}
			}
			noticePn.setVisible(true);
			setDisable();
			
		}
	}
	
	// int가 주어지면 char값으로 변환해주고 배열을 만듬
	public static byte[] setChar(int input) {		
		String str = Integer.toString(input);
		char[] charArr = str.toCharArray();
		byte[] byteArr = new byte[charArr.length + 1];
		
		for(int i = 0; i < byteArr.length; i++) {
			if(i == byteArr.length - 1) {
				byteArr[i] = 10;
			} else {
				byteArr[i] = (byte)charArr[i];
			}
		}
		return byteArr;
	}
	
//	// 지문 등록 리스트 작성
//	public void setEnrollList() {
//		String query = "SELECT * FROM auth_user WHERE is_staff = 0 AND finger_print = 0";
//		// String query = "SELECT * FROM test;";
//		setJList(query);
//	}
//	// 지문 삭제 리스트 작성
//	public void setDeleteList() {
//		String query = "SELECT * FROM auth_user WHERE is_staff = 0 AND finger_print = 1";
//		// String query = "SELECT * FROM test";
//		setJList(query);
//	}
//		
//	// jlist list 셋팅
//	public void setJList(String query) {
//		setData.removeAllElements();
//		db.setJList(query, setData);
//		
//		if(setData.getSize() == 0) {
//			// 항목이 없다면 문구를 표시해주고 클릭 리스너를 막는다
//			setData.addElement("항목 없음");
//			jlist.setEnabled(false);
//		} else {
//			jlist.setEnabled(true);
//		}
//		jlist.setModel(setData);
//	}
	
	// jlist list 셋팅
	public void checkJList() {
		if(setData.getSize() == 0) {
			// 항목이 없다면 문구를 표시해주고 클릭 리스너를 막는다
			setData.addElement("항목 없음");
			jlist.setEnabled(false);
		} else {
			jlist.setEnabled(true);
		}
		jlist.setModel(setData);
	}
		
	public void setEnable() {
		enrollBtn.setEnabled(true);
		deleteBtn.setEnabled(true);
		jlist.setEnabled(true);
		jsp.setEnabled(true);
		jsp.getVerticalScrollBar().setEnabled(true);
	}
	
	public void setDisable() {
		enrollBtn.setEnabled(false);
		deleteBtn.setEnabled(false);
		jlist.setEnabled(false);
		jsp.getVerticalScrollBar().setEnabled(false);
	}
	
	public void disableNoticePn() {
		noticePn.setVisible(false);
		setEnable();
	}
	
	public void startEnrollEvent(byte[] writeArr) {
		if(writer == null) {
			writer = serial.getWriter();
		}
		
		try {
			// 시리얼 통신으로 id값 전송
			// 먼저1을 보냄으로써 지문등록을 활성화시킨다
			System.out.println(40);
			writer.out.write(49);
			writer.out.write(10);
			// 그다음에 사용자의 id를 보내서 지문을 등록하도록 한다
			for(int i = 0; i < writeArr.length; i++) {
				writer.out.write(writeArr[i]);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} 			
	}
	
	public void startDeleteEvent(byte[] writeArr) {
		if(writer == null) {
			writer = serial.getWriter();
		}
		
		try {
			// 시리얼 통신으로 id값 전송
			// 먼저3을 보냄으로써 지문삭제를 활성화시킨다
			writer.out.write(51);
			writer.out.write(10);
			// 그다음에 사용자의 id를 보내서 지문을 삭제하도록 한다
			for(int i = 0; i < writeArr.length; i++) {
				writer.out.write(writeArr[i]);
			}
			// 삭제요청
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
	
	public void setScrollBarPostionReset() {
		jsp.getVerticalScrollBar().setValue(0);
	}
	
}

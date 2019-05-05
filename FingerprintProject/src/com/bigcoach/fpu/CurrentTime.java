package com.bigcoach.fpu;

import java.util.Calendar;

/**
 * 시간을 체크하는 쓰레드
 */
public class CurrentTime implements Runnable {
	private int setSecond = 1;
	private BasePn basePn;
	public static String str;
	
	
	public CurrentTime(BasePn basePn) {
		this.basePn = basePn;
	}
	
	@Override
	public void run() {
		
		while(true) {
			Calendar cal = Calendar.getInstance();
			// 0000-00-00 00:00:00 format
			str = String.format("%tF %tT", cal, cal);
			// 시간을 프로그램에 표시한다
			basePn.timeLb.setText(str);
			
			// 시분초를 초기화한다 등교 하교 시 사용
			BasePn.time = str.substring(11);
			// System.out.println(BasePn.time);
	
			try {
				Thread.sleep(setSecond * 1000);	// 1초마다
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}

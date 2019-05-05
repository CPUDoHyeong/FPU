package com.bigcoach.fpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class SerialReader implements Runnable {
    private InputStream in;
    private BufferedReader bf;
    private DBController db;
 
    private String temp;
    private String[] divs;
    private int user_id;
    private String user_name;
	
	private int modeFlag = -1;

    public SerialReader(InputStream in, DBController db) {
        this.in = in;
        this.db = db;
        try {
			bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    public void setFindMode() {
    	// 등교
    	if(BasePn.flag == Final.ARRIVE_MODE) 
    	{
    		// 06시 전인지 후인지 판별
    		if(arrivedTimeCheck() == 1) {
    			// 6시가 되지 않은 경우
    			NoticePanel.view.setText("6시 이전에는 등교처리가 불가능 합니다.");
    			return;
    		}
    		
    		if(overlapCheck() != 0) {
    			// 이미 등록 되어 있는 경우
    			NoticePanel.view.setText(user_name + "님. 이미 등교 처리가 되었습니다.");
    		} else {
    			latedCheck();
        		NoticePanel.view.setText(user_name + "님. 등교 처리되었습니다.");
    		}
    	} 
    	// 하교
    	else if(BasePn.flag == Final.DEPARTURE_MODE) 
    	{
    		if(db.goToHome(user_id) == 1) {
    			NoticePanel.view.setText("등교 기록이 없습니다.");
    		} else {
    			NoticePanel.view.setText(user_name + "님. 하교 처리되었습니다.");
    		}
    		
    	}
    	NoticePanel.changeCancleBtnText("완료");
    }
    
    public int arrivedTimeCheck() {
    	int tmp = 0;
    	// 6시전에 등교를 하려고 하는 경우 예외처리한다.
		int result = BasePn.time.compareTo("06:00:00");
    	// System.out.println(result);
    	if(result >= 0) {
    		// time이 더 큰 경우. 즉 6시가 지난 경우
    	} else if(result < 0) {
    		// time이 작은 경우. 즉 6시가 지나지 않은 경우
    		// msg = "6시 이전에는 등교를 할 수 없습니다.";
    		tmp = 1;
    	}
    	
    	return tmp;
    }
    
    public int overlapCheck() {
    	return db.overlap(user_id);
    }
    
    public void latedCheck() {
    	boolean flag = false;
//    	System.out.println(BasePn.time);
//    	System.out.println(BasePn.arriveTime);
    	int result = BasePn.time.compareTo(BasePn.arriveTime);
    	// System.out.println(result);
    	if(result > 0) {
    		// time이 더 큰 경우.즉 지각한 경우
    		flag = true;
    	} else if(result <= 0) {
    		// time이 같거나 작은 경우. 즉 지각하지 않은 경우
    	}
    	
    	db.goToSchool(flag, user_id);
    }

    public void run() {
        try {
            while((temp = bf.readLine()) != null) {
            	System.out.println(temp);
            	divs = temp.split("#");
            	
            	if(divs.length > 2){
            		modeFlag = Integer.parseInt(divs[0]);
            		user_id = Integer.parseInt(divs[1]);
            		user_name = db.findName(user_id);
            		if(modeFlag == Final.ENROLL_MODE) {
                    	NoticePanel.view.setText(user_name + "님의 지문 등록 완료!!");
                    	db.changeFingerprintValue(user_id, modeFlag);
                    	NoticePanel.changeCancleBtnText("완료");
                    } else if(modeFlag == Final.FIND_MODE) {
                    	setFindMode();            
                    } else if(modeFlag == Final.DELETE_MODE) {
                    	NoticePanel.view.setText(user_name + "님의 지문 삭제 완료!!");
                    	db.changeFingerprintValue(user_id, modeFlag);
                    	NoticePanel.changeCancleBtnText("완료");
                    }
            	} else {
            		NoticePanel.view.setText(divs[0]);
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

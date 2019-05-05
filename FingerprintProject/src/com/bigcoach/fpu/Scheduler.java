package com.bigcoach.fpu;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 하루에 한번 실행되는 작업을 정의한다.
 * 매일 날이 바뀌는 00시에 db의 등교시간을 가져와 저장한다.
 */
public class Scheduler {
	private DayByDay dayByday = new DayByDay();
	private DateUpdateTask dateUpdateTask = new DateUpdateTask();
	private Timer timer = new Timer();
	private Calendar date = Calendar.getInstance();
	private DBController db;
	
	
	
	// Constructor
	public Scheduler(DBController db){
		this.db = db;
		
		// time setting
		// 최초 실행시간을 설정한다
		date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		date.set(Calendar.AM_PM, Calendar.AM);
		date.set(Calendar.HOUR, 00);
		date.set(Calendar.MINUTE, 1);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		
		// start task
		// 정해진 시간에 최초로 1회 실행하고 이후에는 설정한 주기마다 실행된다.
		// 첫번째 파라미터는 작업할 것 두번째 파라미터는 최초시작시간, 마지막 파라미터는 반복주기이다.
		// 최초 시작시간이 과거이면 반복주기의 처음이 최초 실행이라는 것에 주의한다.
		timer.schedule(dayByday, date.getTime(), 1000 * 60 * 60 * 24); // 1초 * 60초 * 60분 * 24시간 즉 1일
		
		// 4시마다 실행될 작업
		// time setting
		date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		date.set(Calendar.AM_PM, Calendar.AM);
		date.set(Calendar.HOUR, 04);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		
		// start task
		timer.schedule(dateUpdateTask, date.getTime(), 1000 * 60 * 60 * 24); // 1일마다
	}
	
	// 실행될 작업
	class DayByDay extends TimerTask {
		@Override
		public void run() {
			// 하루의 날짜를 지정(년월일)
			date = Calendar.getInstance();
			BasePn.date = String.format("%tF", date);
			// dayOfWeek는 요일을 나타내는 int값임(1:일요일 ~ 7:토요일)
			BasePn.dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
			
			/*
			 * DB에서 요일별 등교시간을 가지고 와서 프로그램에 저장
			 * 우선 지정해 놓은 시간이 있는지 체크 후
			 * 없으면 기본 셋팅 시간을 가져온다.
			 */
			String arriveTime = db.getArriveTime(BasePn.dayOfWeek);
			// 프로그램의 등교시간을 초기화
			BasePn.arriveTime = arriveTime;
			
			System.out.println(BasePn.date + "의 등교시간 : " + BasePn.arriveTime);
		}
	}
	
	// 4시마다 실행될 작업
	class DateUpdateTask extends TimerTask {
		@Override
		public void run() {
			
			// 그리고 하교기준날짜를 초기화한다.
			date = Calendar.getInstance();
			BasePn.goToSchoolDate = String.format("%tF", date);
			// db.updateNoDepartureRecord();
			
			// System.out.println("4시 작업 실행됨");
			System.out.println("하교 기준 날짜 : " + BasePn.goToSchoolDate);
		}
	}
	
		
}

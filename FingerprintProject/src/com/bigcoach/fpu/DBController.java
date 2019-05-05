package com.bigcoach.fpu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;

public class DBController {
	private Connection conn = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;
	
	private String tableName;
	private String colunmName;
	private String query;
	
	// connect
	public ResultSet connect(String query){
		conn = DBManager.getConnection();
		try {
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	// close connect
	public void closeConnection() {
		try {
			if(rs != null) rs.close();
			if(pstm != null) pstm.close();
			if(conn != null) conn.close();
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public int overlap(int student_id) {
		tableName = "data";
		int count = 0;
		query = "SELECT COUNT(student_id) AS cnt FROM " + tableName + " " +
				"WHERE student_id = ? AND date = ?";
		
		try {    		
    		conn = DBManager.getConnection();
    		pstm = conn.prepareStatement(query);
    		pstm.setString(1, Integer.toString(student_id));
    		pstm.setString(2, BasePn.date);
    		rs = pstm.executeQuery();
    		
    		if(rs.next()) {
				count = rs.getInt("cnt");
			}
    		
		} catch(SQLException sqle) {
			System.out.println("INSERT Error");
			sqle.printStackTrace();
		} finally {
			// close the connection
			closeConnection();
		}
		
		return count;
	}
	
	// 등교처리
	public void goToSchool(boolean flag, int student_id) {
		tableName = "data";
		if(flag) {
			// 지각했을 때 쿼리
			query = "INSERT INTO " + tableName + " (student_id, date, arrive, late) VALUES(?, ?, ?, 1)";
		} else {
			// 지각하지 않았을 때
			query = "INSERT INTO " + tableName + " (student_id, date, arrive) VALUES(?, ?, ?)";
		}
				
		try {
			
			// 이제 등교 및 지각여부 처리
			
			conn = DBManager.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, Integer.toString(student_id));
    		pstm.setString(2, BasePn.date);
    		pstm.setString(3, CurrentTime.str);
    		pstm.executeUpdate();
			
		} catch(SQLException sqle) {
			System.out.println("INSERT Error");
			sqle.printStackTrace();
		}  finally {
			// close the connection
			closeConnection();
		}
	}
	
	// 하교처리
	public int goToHome(int student_id) {
		// 조건1. 등교기록이 없으면 하교처리를 하지 못하도록
		// 조건2. 00시가 넘어서 하교 할 경우 전날의 등교기록에 처리할 것
		// 조건3. 4시가 지날 경우 자동으로 4시로 하교처리(스케쥴러로 처리)-Scheduler Class
		
		tableName = "data";
		colunmName = "off_work";
		int result = 0;
		
		query = "SELECT COUNT(*) AS count FROM " + tableName + " " +
				"WHERE date = '" + BasePn.goToSchoolDate + "' AND student_id = " + student_id;		
		
		int count = 0;
		
		try {
			conn = DBManager.getConnection();
			rs = connect(query);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			
			if(count == 0) {
				// 0일 경우는 등교 처리한 것이 없다는 것 
				result = 1;
				return result;
			}
			
			// 이후 하교 처리를 한다
			query = "UPDATE " + tableName + " SET " + colunmName + "= ? " +
					"WHERE student_id = ? AND date = ?";
			
			pstm = conn.prepareStatement(query);
    		pstm.setString(1, CurrentTime.str);
    		pstm.setString(2, Integer.toString(student_id));
    		pstm.setString(3, BasePn.goToSchoolDate);
    		pstm.executeUpdate();

    		
		} catch(SQLException sqle) {
			System.out.println("MySQL Error");
			sqle.printStackTrace();
		} finally {
			// close the connection
			closeConnection();
		}
		
		return result;
	}
	
	// 하교 기록이 없는 것을 전부 04:00:00로 처리
	public void updateNoDepartureRecord() {
		System.out.println("updateNoDepartureRecord");
		tableName = "data";
		colunmName = "off_work";
		
		query = "UPDATE " + tableName + " SET " + colunmName + "= '" + BasePn.date + " 04:00:00' " + 
				"WHERE date = '" + BasePn.goToSchoolDate + "' AND off_work = '0000-00-00 00:00:00'";
		
		System.out.println(query);
		try {    		
    		conn = DBManager.getConnection();
    		pstm = conn.prepareStatement(query);
    		pstm.executeUpdate();
    		
		} catch(SQLException sqle) {
			System.out.println("UPDATE Error");
			sqle.printStackTrace();
		} finally {
			// close the connection
			closeConnection();
		}
	}
	
	// 등록과 삭제에 따른 db값 변경
	public void changeFingerprintValue(int id, int flag) {
		tableName = "auth_user";
		// tableName = "test";
		colunmName = "finger_print";
		query = "";
		if(flag == Final.ENROLL_MODE) {
			query = "UPDATE " + tableName + " SET " + colunmName + "=1 WHERE id=?"; 
		} else if(flag == Final.DELETE_MODE) {
			query = "UPDATE " + tableName + " SET " + colunmName + "=0 WHERE id=?"; 
		}
		
		try {    		
    		conn = DBManager.getConnection();
    		pstm = conn.prepareStatement(query);
    		pstm.setInt(1, id);
    		pstm.executeUpdate();
    		
		} catch(SQLException sqle) {
			System.out.println("UPDATE Error");
			sqle.printStackTrace();
		} finally {
			// close the connection
			closeConnection();
		}
	}
	
	// id로 이름 찾아서 반환
	public String findName(int id) {
    	String name = "";
    	try {
    		tableName = "auth_user";
    		// tableName = "test";
    		colunmName = "username";
    		query = "SELECT " + colunmName + " FROM " + tableName + " WHERE id=" + id;
    		
			rs = connect(query);
			if(rs.next()) {
				name = rs.getString("username");
			}
			
		} catch(SQLException sqle) {
			System.out.println("SELECT Error");
			sqle.printStackTrace();
		} finally {
			// close the connection
			closeConnection();
		}
    	return name;
    }
	
	// 지문 등록 리스트 작성
	public void setEnrollList(DefaultListModel<String> setData) {
		query = "SELECT * FROM auth_user WHERE is_staff = 1 AND finger_print = 0";
		// query = "SELECT * FROM test WHERE finger_print = 0";
		setJList(query, setData);
	}
	
	// 지문 삭제 리스트 작성
	public void setDeleteList(DefaultListModel<String> setData) {
		query = "SELECT * FROM auth_user WHERE is_staff = 1 AND finger_print = 1";
		// query = "SELECT * FROM test WHERE finger_print = 1";
		setJList(query, setData);
	}
	
	// jlist의 항목을 셋팅한다
	public void setJList(String query, DefaultListModel<String> setData) {
		setData.removeAllElements();
		
		try {
			rs = connect(query);
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String addData = id + ".  " + username;
				setData.addElement(addData);
			}
		} catch(SQLException sqle) {
			System.out.println("SELECT Error");
			sqle.printStackTrace();
		} finally {
			closeConnection();
		}
	}
	
	// db에서 요일별 등교시간을 가져온다
	public String getArriveTime(int dayOfWeek) {
		String time = "";
		// query = "SELECT time FROM custom_setting WHERE date = '" + BasePn.date + "'";
		// query = "SELECT time FROM custom_setting WHERE date = '2019-04-08'";
		query = "SELECT custom_arrive_time FROM custom_setting WHERE custom_date = '" + BasePn.date + "'";
	
		try {
			rs = connect(query);
			if(rs.next()) {
				time = rs.getString("custom_arrive_time");
			}
			
			// 직접 설정한 시간이 없다면 기본 시간을 가지고온다
			if(time.equals("")) {
				dayOfWeek -= 1;
				query = "SELECT arrive_time FROM default_setting WHERE id = " + dayOfWeek;
				pstm = conn.prepareStatement(query);
				rs = pstm.executeQuery();
				if(rs.next()) {
					time = rs.getString("arrive_time");
				}
			}
			
		} catch(SQLException sqle) {
			System.out.println("SELECT Error");
			sqle.printStackTrace();
		} finally {
			// close the connection
			closeConnection();
		}
		
		return time;
	}
}

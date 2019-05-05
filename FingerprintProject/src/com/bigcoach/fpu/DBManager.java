package com.bigcoach.fpu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	// declare
	private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final static String DB_URL = "jdbc:mysql://210.101.230.3:3306/fpu?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	
//	private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//	private final static String DB_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	
	private final static String USER_NAME = "root";
	private final static String PASSWORD = "mysql!23";
	
	public static Connection conn;
			
	public static Connection getConnection() {
		Connection conn = null;
		try {
			//Class 클래스의 forName()함수를 이용해서 해당 클래스를 메모리로 로드 하는 것
			Class.forName(JDBC_DRIVER);
			// 접속
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			System.out.println("MySQL Connection");
			
		} catch(ClassNotFoundException e) {
			System.out.println("Faild to load DB Driver");
		} catch(SQLException e) {
			System.out.println("SQL Exception : " + e.getMessage());
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("DBManager Error : Unkonwn error");
			e.printStackTrace();
		}
		
		return conn;
	}
}

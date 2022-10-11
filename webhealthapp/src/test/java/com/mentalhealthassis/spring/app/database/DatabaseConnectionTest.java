package com.mentalhealthassis.spring.app.database;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseConnectionTest {

	
	private static String jdbcUrl;
	private static String user;
	private static String user_fail;
	private static String pass;
	

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		jdbcUrl = "jdbc:mysql://localhost:3306/mental_health_assist?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
		user = "admin";
		user_fail= "admi";		
		pass = "admin";		

	}

	
	@Test
	public void correctConnectionTest() {
		
		try {

			Connection myConn = 
			DriverManager.getConnection(jdbcUrl, user, pass);
			
			//testing the connection was made by no exception thrown
			assertNotNull(myConn);
			
			} catch (Exception e) {
					e.printStackTrace();
			}
	}
	
	
	//testing the connection was not made by exception thrown
	@Test(expected=SQLException.class)
	public void incorrectConnectionTest() throws SQLException {
			
			Connection myConn = 
			DriverManager.getConnection(jdbcUrl, user_fail, pass);

	}
	
	

}

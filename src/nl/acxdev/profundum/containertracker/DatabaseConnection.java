package nl.acxdev.profundum.containertracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import android.app.Activity;

public class DatabaseConnection extends Activity{
	Connection connection = null;
	boolean isConnected = true; 
	public DatabaseConnection() {
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
	 
		System.out.println("MySQL JDBC Driver Registered!");
		
	 
		try {
			connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/contra", "root", "");
			isConnected = true;
	 
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			isConnected = false;
			return;
		}
	 
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}
	
	public boolean getConnection(){
		return isConnected;
		
	}
}
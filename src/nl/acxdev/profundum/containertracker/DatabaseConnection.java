package nl.acxdev.profundum.containertracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DatabaseConnection extends AsyncTask<String, String, ResultSet> {
	Connection conn = null;
	Statement stmt = null;
	String conID;
	String shipID;
	String query;
	ResultSet rs;
	boolean wait = true;

	public DatabaseConnection(String query) {
		this.query = query;
		threadDB.start();
	}


	@Override
	protected ResultSet doInBackground(String... params) {

		

		while (wait) {
			if(!threadDB.isAlive()){
				return rs;
			}
		}
		
		return rs;
		
	}

	Thread threadDB = new Thread() {
		public void run() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Log.d("DB_class_test", "Driver registred");
			} catch (ClassNotFoundException e) {
				Log.d("DB_class_test", "Where is your MySQL JDBC Driver?");
				e.printStackTrace();
				return;
			}
			try {
				conn = DriverManager.getConnection("jdbc:mysql://145.24.222.149:8306/contra", "contrauser", "1234");
				Log.d("DB_driver_test", "Database is connected");

			} catch (SQLException e) {
				Log.d("DB_driver_test", "Connection Failed! Check output console");
				e.printStackTrace();
				return;
			}

			try {

				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
				
				wait = false;
				Log.d("database", "waiting done;");
				
			} catch (SQLException e) {
				Log.d("DB_conn_test", "Results doen't work");
				e.printStackTrace();
			}
			
		}
	};
}
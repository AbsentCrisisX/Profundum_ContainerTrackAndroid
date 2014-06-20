package nl.acxdev.profundum.containertracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mysql.jdbc.*;

public class DataOverview extends Activity {

	TextView tx1, tx2;
	Connection conn = null;
	Statement stmt = null;
	String conID = null;
	int shipID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_overview);

		tx1 = (TextView) findViewById(R.id.TextView01);
		tx2 = (TextView) findViewById(R.id.TextView02);

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
							conn = DriverManager.getConnection("jdbc:mysql://145.24.222.149:8306/contra","contrauser", "1234");
							Log.d("DB_driver_test", "Database is connected");

						} catch (SQLException e) {
							Log.d("DB_driver_test", "Connection Failed! Check output console");
							e.printStackTrace();
							return;
						}

						
						String query = "SELECT * FROM shipments JOIN shipmentscontent JOIN shipmentspackinggroups where smt_con_id = 'ID18366162' GROUP BY smt_con_id";
						try {
							stmt = conn.createStatement();
							ResultSet rs = stmt.executeQuery(query);
							ResultSetMetaData rsmd = rs.getMetaData();
							while (rs.next()) {	
								conID = rs.getString("smt_con_id");
								shipID = rs.getInt("smt_to_cit_id");
								Log.d("testDB_string", conID);
								Log.d("testDB_string", shipID + "");
							} 
							conn.close();
						} catch (SQLException e) {
							Log.d("DB_conn_test", "Results doen't work");
							e.printStackTrace();
						} 
						
						runOnUiThread(new Runnable() {
						     @Override
						     public void run() {

						Log.d("Result check", conID);
						tx1.setText(conID);
						tx2.setText("" + shipID);
						    }
						});
					}
				};
				
				threadDB.start();

	}

}

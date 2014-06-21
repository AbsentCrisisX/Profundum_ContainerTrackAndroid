package nl.acxdev.profundum.containertracker;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText usr;
	EditText pwd;
	Button sig;
	String usrS;
	String pwdS;
	SHA1 clas;
	String pwdE;
	
	Connection conn = null;
	Statement stmt = null;
	DatabaseConnection db;
	ResultSet rs;
	String query;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		usr = (EditText) findViewById(R.id.usr);
		pwd = (EditText) findViewById(R.id.password);
		sig = (Button) findViewById(R.id.signIn);
		clas = new SHA1();

		
		sig.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	            	        	
                usrS = (String) usr.getText().toString();
                pwdS = pwd.getText().toString();
                
                try {
					pwdE = clas.sha1(pwdS);
					Log.d("pwcheck", pwdE);
					
					query = "SELECT COUNT(*) FROM users WHERE use_username = '"+usrS+"' AND use_password = '"+pwdE+"'";
					
					Log.d("query", query);
					db = new DatabaseConnection(query);
					db.execute();
					try {
						rs = db.get();
						
						while(rs.next()){
							int count = rs.getInt("COUNT(*)");
							
							if(count == 1){
			                	Context context = getApplicationContext();
			                	CharSequence text = "Login succeeded!";
			                	int duration = Toast.LENGTH_SHORT;
			                	
			                	Toast.makeText(context, text, duration).show();
			                	
			                	Intent myIntent = new Intent(MainActivity.this, ScanView.class);
			                	MainActivity.this.startActivity(myIntent);
			                } else {
			                	Context context = getApplicationContext();
			                	CharSequence text = "Username or password is incorrect";
			                	int duration = Toast.LENGTH_LONG;
			                	
			                	Toast.makeText(context, text, duration).show();
			                }
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

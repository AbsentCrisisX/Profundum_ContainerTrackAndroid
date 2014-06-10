package nl.acxdev.profundum.containertracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText usr;
	EditText pwd;
	Button sig;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		usr = (EditText) findViewById(R.id.usr);
		pwd = (EditText) findViewById(R.id.password);
		sig = (Button) findViewById(R.id.signIn);
		
		sig.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click   
            	            	        	
                String usrS = (String) usr.getText().toString();
                String pwdS = (String) pwd.getText().toString();
                
                if(usrS.equals("admin") && pwdS.equals("admin")){
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
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

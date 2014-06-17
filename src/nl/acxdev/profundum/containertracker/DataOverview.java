package nl.acxdev.profundum.containertracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class DataOverview extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_overview);

		DatabaseConnection dbConn = new DatabaseConnection();
		
		TextView debuggin = (TextView) findViewById(R.id.debug);
		debuggin.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

/**
 * 
 */
package nl.acxdev.profundum.containertracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanView extends Activity {

	Button search;
	ImageView imagedb;
	TextView textDBcon;
	DatabaseConnection dbConn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		
		dbConn = new DatabaseConnection();
		
		search = (Button) findViewById(R.id.cont_search);
		imagedb = (ImageView) findViewById(R.id.imageViewDB);
		textDBcon = (TextView) findViewById(R.id.textViewDB);

		
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent detail = new Intent(ScanView.this, DataOverview.class);

				Toast.makeText(getApplicationContext(), "Found the container",
						Toast.LENGTH_SHORT).show();
				ScanView.this.startActivity(detail);

			}
		});
		
		
		if(dbConn.getConnection()){
			imagedb.setImageResource(R.drawable.green);
			textDBcon.setText("Connected"); 
		} 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		String url = "http://localhost";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
		return super.onMenuItemSelected(featureId, item);
	}


}

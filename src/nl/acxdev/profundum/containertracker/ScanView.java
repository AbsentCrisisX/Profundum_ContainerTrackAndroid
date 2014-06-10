/**
 * 
 */
package nl.acxdev.profundum.containertracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ScanView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		Button search = (Button) findViewById(R.id.cont_search);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent detail = new Intent(ScanView.this, DataOverview.class);

				Toast.makeText(getApplicationContext(), "Found the container",
						Toast.LENGTH_SHORT).show();
				ScanView.this.startActivity(detail);

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

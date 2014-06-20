/**
 * 
 */
package nl.acxdev.profundum.containertracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanView extends Activity {

	Button search;
	ImageView imagedb;
	TextView textDBcon;
	EditText container;
	DatabaseConnection dbConn;
	NFCForegroundUtil nfcForegroundUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		// dbConn = new DatabaseConnection();
		nfcForegroundUtil = new NFCForegroundUtil(this);
		search = (Button) findViewById(R.id.cont_search);
		imagedb = (ImageView) findViewById(R.id.imageViewDB);
		textDBcon = (TextView) findViewById(R.id.textViewDB);
		container = (EditText) findViewById(R.id.cont_id);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getContainerData(container.getText().toString(), false);

			}
		});

		

	}

	@Override
	public void onResume() {
		super.onResume();
		nfcForegroundUtil.enableForeground();

		if (!nfcForegroundUtil.getNfc().isEnabled()) {
			Toast.makeText(getApplicationContext(), "Please activate NFC and press Back to return to the application!", Toast.LENGTH_LONG)
					.show();
			startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	@Override
	public void onPause() {
		nfcForegroundUtil.disableForeground();
		super.onPause();
	}

	public void getContainerData(String id, boolean byScan) {
		Intent detail = new Intent(ScanView.this, DataOverview.class);
		if (byScan) {
			detail.putExtra("CHIP", id);
		} else {
			detail.putExtra("CONTAINER", id);
		}
		ScanView.this.startActivity(detail);

	}

	public void onNewIntent(Intent intent) {
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		/*
		 * StringBuilder sb = new StringBuilder();
		 * for(int i = 0; i < tag.getId().length; i++){
		 * sb.append(new Integer(tag.getId()[i]) + " ");
		 * }
		 */
		
		Log.d("scantag: ", bytesToHex(tag.getId()));
		getContainerData(bytesToHex(tag.getId()), true);

	}

	public static String bytesToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buf.append(byteToHex(data[i]).toUpperCase());
			buf.append(" ");
		}
		return (buf.toString());
	}

	public static String byteToHex(byte data) {
		StringBuffer buf = new StringBuffer();
		buf.append(toHexChar((data >>> 4) & 0x0F));
		buf.append(toHexChar(data & 0x0F));
		return buf.toString();
	}

	public static char toHexChar(int i) {
		if ((0 <= i) && (i <= 9)) {
			return (char) ('0' + i);
		} else {
			return (char) ('a' + (i - 10));
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
		String url = "https://www.google.com/maps/place/Haven+van+Rotterdam/@51.885,4.2867,17z/data=!3m1!4b1!4m2!3m1!1s0x47c44b942f269a33:0x183e4ce5a09eff9";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
		return super.onMenuItemSelected(featureId, item);
	}

}

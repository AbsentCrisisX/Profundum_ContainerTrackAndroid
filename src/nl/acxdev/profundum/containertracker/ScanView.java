/**
 * 
 */
package nl.acxdev.profundum.containertracker;

import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
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
	EditText container,pin;
	DatabaseConnection dbConn;
	NFCForegroundUtil nfcForegroundUtil = null;
	
	String whereStatement;
	ResultSet rs;
	String query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		nfcForegroundUtil = new NFCForegroundUtil(this);
		search = (Button) findViewById(R.id.cont_search);
		imagedb = (ImageView) findViewById(R.id.imageViewDB);
		textDBcon = (TextView) findViewById(R.id.textViewDB);
		container = (EditText) findViewById(R.id.cont_id);
		pin = (EditText) findViewById(R.id.cont_pin);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getContainerData(container.getText().toString(), pin.getText().toString(), false);

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

	public void getContainerData(String id, String pin, boolean byScan) {
		Intent detail = new Intent(ScanView.this, DataOverview.class);
		if (byScan) {
			detail.putExtra("CHIP", id);
			whereStatement = "con_chip = '" + id + "'";
		} else {
			detail.putExtra("CONTAINER", id);
			whereStatement = "smt_con_id = '" + id + "' AND c.con_code='" + pin + "'";
		}
		
		query = "SELECT smt_con_id, citiesfrom.cit_name AS city_from, citiesto.cit_name AS city_to, cont_name, pac_name , countriesfrom.cou_code AS cou_from_code, countriesto.cou_code AS cou_to_code, pos_longitude, pos_latitude" 
				+" FROM shipments"
				+" LEFT JOIN containers AS c ON smt_con_id = c.con_id"
				+" LEFT JOIN shipmentscontent ON smt_id=cco_smt_id" 
				+" LEFT JOIN content ON cco_content_id=cont_id"
				+" LEFT JOIN shipmentspackinggroups ON smt_id=cpg_smt_id" 
				+" LEFT JOIN packinggroups ON cpg_pac_id=pac_id"
				+" LEFT JOIN cities AS citiesfrom ON smt_from_cit_id = citiesfrom.cit_id"
				+" LEFT JOIN cities AS citiesto ON shipments.smt_to_cit_id = citiesto.cit_id"
				+" LEFT JOIN countries AS countriesfrom ON countriesfrom.cou_id = citiesfrom.cit_cou_id"
				+" LEFT JOIN countries AS countriesto ON countriesto.cou_id = citiesto.cit_cou_id"
				+" LEFT JOIN positions ON smt_id=pos_smt_id WHERE " + whereStatement
				+" GROUP BY smt_con_id ORDER BY pos_smt_id DESC LIMIT 1";
		
		Log.d("query", query);
		dbConn = new DatabaseConnection(query, true);
		dbConn.execute();
		try {
			rs = dbConn.get();
			
			Log.d("results", rs.toString());

			while (rs.next()) {
				detail.putExtra("CONID", rs.getString("smt_con_id"));
				detail.putExtra("CITYFROM", rs.getString("city_from")+"["+rs.getString("cou_from_code")+"]");
				detail.putExtra("CITYTO", rs.getString("city_to")+"["+rs.getString("cou_to_code")+"]");
				detail.putExtra("CONTENTS", rs.getString("cont_name"));
				detail.putExtra("DANGER", rs.getString("pac_name"));
				
				Context context = getApplicationContext();
            	CharSequence text = "Found the container!";
            	int duration = Toast.LENGTH_LONG;
            	
            	Toast.makeText(context, text, duration).show();
				ScanView.this.startActivity(detail);
			}
			Context context = getApplicationContext();
        	CharSequence text = "Couldn't find a container!";
        	int duration = Toast.LENGTH_LONG;
        	
        	Toast.makeText(context, text, duration).show();
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
		//ScanView.this.startActivity(detail);

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
		getContainerData(bytesToHex(tag.getId()), null, true);

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

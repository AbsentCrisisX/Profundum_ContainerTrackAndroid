package nl.acxdev.profundum.containertracker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DataOverview extends Activity implements LocationListener {

	//TextView tx1, tx2;
	TextView cId, cFrom, cTo, cCont, cDang;
	Connection conn = null;
	Statement stmt = null;
	
	String conID = null;
	String coFrom = null;
	String coTo = null;
	String coCont = null;
	String coDang = null;
	
	DatabaseConnection db;
	DatabaseConnection db2;
	ResultSet rs;
	String query;
	
	LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_overview);

		String containerid = getIntent().getExtras().getString("CONTAINER");
		String chipid = getIntent().getExtras().getString("CHIP");
		String whereStatement = "";
		if (containerid == null) {
			chipid = chipid.trim();
			whereStatement = "con_chip = '" + chipid + "'";
		} else {
			whereStatement = "smt_con_id = '" + containerid + "'";
		}
		
		cId = (TextView)findViewById(R.id.cId);
		cFrom = (TextView)findViewById(R.id.cFrom);
		cTo = (TextView)findViewById(R.id.cTo);
		cCont = (TextView)findViewById(R.id.conCont);
		cDang = (TextView)findViewById(R.id.conDang);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);

		query = "SELECT smt_con_id, citiesfrom.cit_name AS city_from, citiesto.cit_name AS city_to, cont_name, pac_name , countriesfrom.cou_code AS cou_from_code, countriesto.cou_code AS cou_to_code, pos_longitude, pos_latitude" 
				+" FROM shipments"
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
		db = new DatabaseConnection(query, true);
		db.execute();
		try {
			rs = db.get();

			while (rs.next()) {
				conID = rs.getString("smt_con_id");
				coFrom = rs.getString("city_from")+"["+rs.getString("cou_from_code")+"]";
				coTo = rs.getString("city_to")+"["+rs.getString("cou_to_code")+"]";
				coCont = rs.getString("cont_name");
				coDang = rs.getString("pac_name");

				cId.setText(conID);
				cFrom.setText(coFrom);
				cTo.setText(coTo);
				cCont.setText(coCont);
				Log.d("contents", rs.getString("cont_name"));
				cDang.setText(coDang);
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

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		//Log.d("Location", location.getLongitude()+", "+location.getLatitude());
		String containerid = getIntent().getExtras().getString("CONTAINER");
		String chipid = getIntent().getExtras().getString("CHIP");
		Integer pos_smt_id = 0;
		String whereStatement = "";
		if (containerid == null) {
			chipid = chipid.trim();
			whereStatement = "con_chip = '" + chipid + "'";
		} else {
			whereStatement = "smt_con_id = '" + containerid + "'";
		}
		
		query = "SELECT pos_smt_id" 
				+" FROM shipments"
				+" LEFT JOIN shipmentscontent ON smt_id=cco_smt_id" 
				+" LEFT JOIN content ON cco_content_id=cont_id"
				+" LEFT JOIN shipmentspackinggroups ON smt_id=cpg_smt_id" 
				+" LEFT JOIN packinggroups ON cpg_pac_id=pac_id"
				+" LEFT JOIN cities AS citiesfrom ON smt_from_cit_id = citiesfrom.cit_id"
				+" LEFT JOIN cities AS citiesto ON shipments.smt_to_cit_id = citiesto.cit_id"
				+" LEFT JOIN countries AS countriesfrom ON countriesfrom.cou_id = citiesfrom.cit_cou_id"
				+" LEFT JOIN countries AS countriesto ON countriesto.cou_id = citiesto.cit_cou_id"
				+" LEFT JOIN positions ON smt_id=pos_smt_id WHERE " + whereStatement
				+" GROUP BY smt_con_id"
				+" ORDER BY smt_id DESC, pos_smt_id DESC LIMIT 1";

		Log.d("query", query);
		db = new DatabaseConnection(query, true);
		db.execute();
		
		try {
			rs = db.get();

			while (rs.next()) {
				pos_smt_id = rs.getInt("pos_smt_id");
				
				String query2 = "INSERT INTO positions (pos_id, pos_smt_id, pos_latitude, pos_longitude, pos_datetime) VALUES (null, "+pos_smt_id+","+location.getLongitude()+","+location.getLatitude()+", NOW())";
				Log.d("query2", query2);
				
				db2 = new DatabaseConnection(query2, false);
				db2.execute();
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
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}

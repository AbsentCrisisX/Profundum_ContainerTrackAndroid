package nl.acxdev.profundum.containertracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DataOverview extends Activity {

	TextView tx1, tx2;
	Connection conn = null;
	Statement stmt = null;
	String conID = null;
	String shipID;
	DatabaseConnection db;
	ResultSet rs;
	String query;

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

		tx1 = (TextView) findViewById(R.id.TextView01);
		tx2 = (TextView) findViewById(R.id.TextView02);
		query = "SELECT smt_con_id, citiesfrom.cit_name AS city_from, citiesto.cit_name AS city_to, cont_name, pac_name , countriesfrom.cou_code AS cou_from_code, countriesto.cou_code AS cou_to_code, pos_longitude, pos_latitude"
				+ " FROM shipments "
				+ "NATURAL JOIN shipmentscontent "
				+ "NATURAL JOIN content "
				+ "NATURAL JOIN shipmentspackinggroups "
				+ "NATURAL JOIN packinggroups "
				+ "LEFT JOIN cities AS citiesfrom ON smt_from_cit_id = citiesfrom.cit_id "
				+ "LEFT JOIN cities AS citiesto ON smt_from_cit_id = citiesto.cit_id "
				+ "LEFT JOIN countries AS countriesfrom ON countriesfrom.cou_id = citiesfrom.cit_cou_id "
				+ "LEFT JOIN countries AS countriesto ON countriesto.cou_id = citiesto.cit_cou_id "
				+ "LEFT JOIN positions ON smt_id=pos_smt_id "
				+ "LEFT JOIN containers ON smt_con_id=con_id "
				+ "WHERE "
				+ whereStatement + " GROUP BY smt_con_id ORDER BY smt_id DESC LIMIT 1;";

		Log.d("query", query);
		db = new DatabaseConnection(query);
		db.execute();
		try {
			rs = db.get();

			while (rs.next()) {
				conID = rs.getString("smt_con_id");
				shipID = rs.getString("smt_con_id");

				tx1.setText(conID);
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

}

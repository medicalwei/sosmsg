package idv.medicalwei.sosmsg;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class SOSMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7815193602547553293L;
	
	public String id;
	public String name;
	public String message;
	public int status;
	public float lat, lon;
	public int hop;
	public long timestamp;
	public String appid;
	
	public void _SOSMessage(String appid, String name, String message, int status, float lat, float lon,
			int hop, long timestamp) {
		this.appid = appid;
		this.name = name;
		this.message = message;
		this.status = status;
		this.lat = lat;
		this.lon = lon;
		this.hop = hop;
		this.timestamp = timestamp;
	}
	
	public void _SOSMessage(String appid, String name, String message, int status, float lat, float lon,
			int hop) {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		_SOSMessage(appid, name, message, status, lat, lon, hop, now.getTime());
	}
	
	public SOSMessage(String appid, String name, String message, int status, float lat, float lon, int hop) {
		_SOSMessage(appid, name, message, status, lat, lon, hop);
	}

	public SOSMessage(String appid, String name, String message, int status, float lat, float lon) {
		this(appid, name, message, status, lat, lon, 0);
	}

	public SOSMessage(String appid, String name, String message, int status) {
		this(appid, name, message, status, Float.NaN, Float.NaN, 0);
	}
	
	public SOSMessage(String appid, View view){
        String name = ((EditText) view.findViewById(R.id.userName)).getText().toString();
        String message = ((EditText) view.findViewById(R.id.message)).getText().toString();
        int status = ((Spinner) view.findViewById(R.id.status)).getSelectedItemPosition();
        _SOSMessage(appid, name, message, status, Float.NaN, Float.NaN, 0);
	}
	
	public SOSMessage(Cursor c){
		appid = c.getString(c.getColumnIndex("appid"));
        name = c.getString(c.getColumnIndex("name"));
        status = c.getInt(c.getColumnIndex("status"));
        message = c.getString(c.getColumnIndex("message"));
        lat = c.getFloat(c.getColumnIndex("lat"));
        lon = c.getFloat(c.getColumnIndex("lon"));
        if(lat == 0 && lon == 0){
        	lat = lon = Float.NaN;
        }
        hop = c.getInt(c.getColumnIndex("hop"));
	}
	
	public ContentValues toValues(){
        ContentValues values = new ContentValues();
        values.put("appid", this.appid);
        values.put("name", this.name);
        values.put("message", this.message);
        values.put("status", this.status);
        if(this.lat != Float.NaN){
        	values.put("lat", this.lat);
        	values.put("lon", this.lon);
        }
        values.put("hop", this.hop);
        values.put("timestamp", this.timestamp);

		return values;
	}
}

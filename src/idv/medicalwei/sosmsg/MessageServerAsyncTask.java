package idv.medicalwei.sosmsg;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class MessageServerAsyncTask extends AsyncTask<Void, Void, Void> {

    private TextView statusText;
	private SQLiteDatabase db;
	private String tableName;
	private MainActivity activity;

    public MessageServerAsyncTask(MainActivity activity, View statusText) {
    	this.activity = activity;
        this.statusText = (TextView) statusText;
        this.tableName = DBHelper.tableName; 
        this.db = new DBHelper(activity).getWritableDatabase();
    }

    protected Void doInBackground(Void... params) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            Socket client = serverSocket.accept();
            ObjectInputStream is = new ObjectInputStream(client.getInputStream());
            int count = (Integer) is.readObject();
            
            for(;count>0;count--){
            	SOSMessage message = (SOSMessage) is.readObject();
            	message.hop += 1;
            	ContentValues values = message.toValues();
            	String[] whereparams = {values.getAsString("appid"), values.getAsLong("timestamp").toString(),
            			values.getAsInteger("hop").toString()};
            	if (db.update(tableName, values, "pid=? and timestamp<? and hop<?", whereparams) <= 0){
            		db.insert(tableName, null, values);
            	}
            }
            
            serverSocket.close();
            activity.updateMessage();

        } catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
    protected void onPostExecute(String result) {
        if (result != null) {
            statusText.setText("File copied - " + result);
        }
    }
}

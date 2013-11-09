package idv.medicalwei.sosmsg;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;

public class RelatedFragment extends ListFragment {
    private ArrayList<String> results = new ArrayList<String>();
    private String tableName = DBHelper.tableName;
    private SQLiteDatabase newDB;
    /** Called when the activity is first created. */
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	refreshResultList();
    }
    
    public void refreshResultList(){
        openAndQueryDatabase();
        displayResultList();
    }
    
    private void displayResultList() {
        setListAdapter(new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1, results));
        getListView().setTextFilterEnabled(true);
    }
    
    private void openAndQueryDatabase() {
        try {
            DBHelper dbHelper = new DBHelper(this.getActivity());
            newDB = dbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT name, status, message, timestamp FROM " +
                    tableName + "SORT BY status, timestamp ", null);
            String[] statusStrings = getResources().getStringArray(R.array.status_array);
            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String name = c.getString(c.getColumnIndex("name"));
                        String status = statusStrings[c.getInt(c.getColumnIndex("status"))];
                        String message = c.getString(c.getColumnIndex("message"));
                        results.add("[" + status + "] " + name + " " + message);
                    }while (c.moveToNext());
                }
            }          
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {
            if (newDB != null)
                newDB.execSQL("DELETE FROM " + tableName);
                newDB.close();
        }
 
    }
}
package idv.medicalwei.sosmsg;
import idv.medicalwei.sosmsg.R;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public class MyselfFragment extends Fragment {
        private String tableName = DBHelper.tableName;
        private SQLiteDatabase newDB;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public MyselfFragment() {
        	
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_myself, container, false);
            return rootView;
        }
        
        public void updateMessage(){
            try {
                DBHelper dbHelper = new DBHelper((MainActivity) this.getActivity());
                newDB = dbHelper.getWritableDatabase();

                SOSMessage message = new SOSMessage(DBHelper.appid, getView());
                ContentValues values = message.toValues();
                if (newDB.update(tableName, values, "appid=\'" + values.getAsString("appid") + "\'", null) <= 0){
                	newDB.insert(tableName, null, values);
                }
                ((TextView) getActivity().findViewById(R.id.app_status)).setText(R.string.string_message_updated);
            } catch (SQLiteException se ) {
                Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            } finally {
                newDB.close();
            }
        }
    }

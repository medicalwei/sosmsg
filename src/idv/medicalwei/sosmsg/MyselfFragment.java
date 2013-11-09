package idv.medicalwei.sosmsg;
import idv.medicalwei.sosmsg.R;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

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
                DBHelper dbHelper = new DBHelper(this.getActivity());
                newDB = dbHelper.getWritableDatabase();

            	String name = DatabaseUtils.sqlEscapeString(getView().findViewById(R.id.userName).getContext().toString());
            	String message = DatabaseUtils.sqlEscapeString(getView().findViewById(R.id.message).getContext().toString());
            	Integer status = ((Spinner) getView().findViewById(R.id.status)).getSelectedItemPosition();

                newDB.rawQuery("INSERT INTO " +
                        tableName + " (name, message, status) VALUES (" + 
                		name + ", " + message + "," + status + ");", null);
            } catch (SQLiteException se ) {
                Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            } finally {
                if (newDB != null)
                    newDB.execSQL("DELETE FROM " + tableName);
                    newDB.close();
            }
     

        }
    }

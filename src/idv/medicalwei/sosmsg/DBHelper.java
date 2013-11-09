package idv.medicalwei.sosmsg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public SQLiteDatabase DB;
    public String DBPath;
    public static String DBName = "sosmsg";
    public static final int version = '3';
    public static Context currentContext;
    public static String tableName = "messages";
    private boolean dbExists;

	public DBHelper(Context context) {
		super(context, DBName, null, version);
        currentContext = context;
        DBPath = context.getFilesDir().toString();
        dbExists = checkDbExists();
        createDatabase();
    }
	 
    private void createDatabase() {
        if (dbExists) {
            // do nothing
        } else {
            DB = currentContext.openOrCreateDatabase(DBName, 0, null);
            DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                    tableName +
                    " (name VARCHAR, status INTEGER, message TEXT" +
                    " lat REAL, lon REAL, timestamp DATETIME);");
        }
    }
    
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	} 
	
    private boolean checkDbExists() {
        SQLiteDatabase checkDB = null;
 
        try {
            String myPath = DBPath + "/" + DBName;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }
 
        if (checkDB != null) {
            checkDB.close();
        }
 
        return checkDB != null;
    }

}

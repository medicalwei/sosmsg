package idv.medicalwei.sosmsg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "sosmsg";
    private static final int DATABASE_VERSION = 6; 
    public static Context currentContext;
    public static String tableName = "messages";
    public static String appid;

	public DBHelper(MainActivity activity) {
		super(activity, DATABASE_NAME, null, DATABASE_VERSION);
		appid = activity.appid;
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                " (appid STRING PRIMARY KEY, name VARCHAR, status INTEGER, message TEXT," +
                " lat REAL, lon REAL, timestamp LONG, hop INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS " + tableName);
		onCreate(db);
	} 
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS " + tableName);
		onCreate(db);
	} 

}

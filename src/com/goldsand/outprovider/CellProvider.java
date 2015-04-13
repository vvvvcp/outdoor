package com.goldsand.outprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class CellProvider extends ContentProvider{
	static final String PROVIDER_NAME = "com.goldsand.outdoor.cellinfo";
	static final String URL = "content://" + PROVIDER_NAME + "/detail";

    static final String _ID = "_id";
	static final String LAC = "LAC";
	static final String SID = "SID";
	static final String RSSI = "RSSI";
//	static final HashMap<String, String> STUDENT_PROJECTION_MAP;

	static final int CELL_ID = 1;
	static final int CELL_INFO = 2;
	static final UriMatcher uriMatcher;
	private DatabaseHelper dbHelper;

	static{
	    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "cellid", CELL_ID);
		uriMatcher.addURI(PROVIDER_NAME, "cellinfo", CELL_INFO);
	}
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "cellinfo";
	static final String CELL_ID_TABLE = "cellid";
	static final String CELL_INFO_TABLE = "cellinfo";
    static final int DATABASE_VERSION = 1;

	static final String CREATE_CELLID_TABLE =
	  " CREATE TABLE " + CELL_ID_TABLE +
	  " (_id INTER PRIMARY KEY AUTOINCRMENT,"+
	  " lac TEXT NOT NULL, "+
	  "  TEXT NOT NUll, "+
	  " rssi TEXT NOT NULL);";

    static final String CREATE_CELLINFO_TABLE =
	  "CREATE TABLE " + CELL_INFO_TABLE +
	  " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
	  " lac TEXT, "+
	  " sid TEXT, "+
	  " latitude TEXT, "+
	  " longitude TEXT);";
    private static class DatabaseHelper extends SQLiteOpenHelper {
	  DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }
	  @Override
	  public void onCreate(SQLiteDatabase db) {
	    //db.execSQL(CREATE_CELLID_TABLE);
		db.execSQL(CREATE_CELLINFO_TABLE);
	  }
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   // db.execSQL("DROP TABLE IF EXISTS "+ CELL_ID_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ CELL_INFO_TABLE);
		onCreate(db);
	  }
	}
    @Override
	public boolean onCreate() {
	    Context context = getContext();
		dbHelper = new DatabaseHelper(context);
		//db = dbHelper.getWritableDatabase();
		return (dbHelper == null)? false: true;
	}

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
       // SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		//qb.setTables();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        switch(uriMatcher.match(uri)) {
        case CELL_ID:
            return db.query(CELL_INFO_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        }



        return null;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		switch(uriMatcher.match(uri)) {
		case CELL_ID: {
            long rowid = db.insert(CELL_INFO_TABLE, "", values);
            Uri insertUri = ContentUris.withAppendedId(uri, rowid);// 得到代表新增记录的Uri  
            this.getContext().getContentResolver().notifyChange(uri, null);  
            return insertUri;
		}		    
		default:  
            throw new IllegalArgumentException("Unkwon Uri:" + uri.toString()); 
		}
        
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int count;
        switch(uriMatcher.match(uri)) {
        case CELL_INFO: {
            
            count = db.update(CELL_INFO_TABLE, values, selection, selectionArgs);
           return count;
        }
        default:  
            throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());  
        
        }
    }


}

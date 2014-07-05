package com.softnuke.noter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper{
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "notesmanager";
 
    // Contacts table name
    private static final String TABLE_NOTES = "notes";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATA = "data";
    private static final String KEY_COLOR = "color";	//default light orange #EB8736
    private static final String KEY_TIME = "timestamp";
	
	public DbHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT,"
                + KEY_DATA + " TEXT,"                
				+ KEY_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
				+ KEY_COLOR + " VARCHAR DEFAULT '#EB8736' " + ")";
		
        db.execSQL(CREATE_NOTES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		/*if (oldVersion < 2) {
            final String ALTER_TBL = 
                "ALTER TABLE " + TABLE_NOTES+
                " ADD COLUMN "+ KEY_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP";
            db.execSQL(ALTER_TBL);
        }*/
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
 
        // Create tables again
        onCreate(db);
		
	}
	
	/**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
	
	// Adding new contact
	public void addNote(Notes note) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    
	    //values.put(KEY_ID, note.getID());	// Note id
	    values.put(KEY_NAME, note.getName()); // Note Name
	    values.put(KEY_DATA, note.getData()); // Note Data
	    values.put(KEY_COLOR, note.getColor()); // Note Color
	    
	    if(note.getTime()!=null)
	    	values.put(KEY_TIME, note.getTime()); // Note time
	 
	    // Inserting Row
	    db.insert(TABLE_NOTES, null, values);
	    
	    db.close(); // Closing database connection	    
	    
	}

	// Getting single note
	public Notes getNote(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = null;
	    Notes note = null;
	    
	    // tablename, columns, where clause by escaping element, null for remaning parameters like Groupby,etc.
	    cursor = db.query(TABLE_NOTES, new String[] { KEY_ID, KEY_NAME, KEY_DATA, KEY_TIME, KEY_COLOR }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    
	    if (cursor != null && cursor.moveToFirst()){	    	
	 
		    note = new Notes(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
		    		cursor.getString(2), cursor.getString(3), cursor.getString(4));
		    
		    cursor.close();
	    } 
	    
		// return note
		return note;
	    
	}
	
	// Get last added note
	public Notes getLastAddedNote() {
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = null;
	    Notes note = null;   
	   
	    
	    // tablename, columns, where clause by escaping element, null for remaning parameters like Groupby,etc.
	    cursor = db.query(TABLE_NOTES, new String[] { KEY_ID, KEY_NAME, KEY_DATA, KEY_TIME, KEY_COLOR }, null,
	            null, null, null, KEY_TIME+ " DESC", "1");
	    
	    if (cursor != null && cursor.moveToFirst()){	    	
	 
		    note = new Notes(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
		    		cursor.getString(2), cursor.getString(3), cursor.getString(4));
		    
		    cursor.close();
	    } 
	    
		// return note
		return note;
	    
	} 
	
	//get NoteId, under construction
	public int getId(Notes note){
		return 0;
	}
	
	// Fetching all notes in list form
	public List<Notes> getAllNotes() {
	    List<Notes> noteList = new ArrayList<Notes>();
	    
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " ORDER BY "+KEY_TIME+ " DESC";
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Notes note = new Notes();
	            note.setID(Integer.parseInt(cursor.getString(0)));
	            note.setName(cursor.getString(1));
	            note.setData(cursor.getString(2));
	            note.setTime(cursor.getString(3));
	            note.setColor(cursor.getString(4));
	            
	            // Adding note to list
	            noteList.add(note);
	        } while (cursor.moveToNext());
	    }
	    
	    cursor.close();
	    
	    // return note list
	    return noteList;
	}

	// Getting note Count
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
    
    // Getting note color
    public String getNoteColor(int id) {
        String countQuery = "SELECT "+ KEY_COLOR +" FROM " + TABLE_NOTES + " WHERE "+ KEY_ID +" = "+ id;
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return color
        return cursor.getString(0);
    }
    
    // Updating single contact
    public int updateNote(Notes note) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, note.getName());
        values.put(KEY_DATA, note.getData());
        values.put(KEY_COLOR, note.getColor());
        
        if(note.getTime()!=null)
	    	values.put(KEY_TIME, note.getTime()); // Note time
     
        // updating row
        int res = db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(note.getID()) });
        
        db.close();
        
        return res; 
    }
    
    // Deleting single contact
    public int deleteNote(Notes note) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[] { String.valueOf(note.getID()) });
        db.close();
        
        return res;
        
        
    }
}



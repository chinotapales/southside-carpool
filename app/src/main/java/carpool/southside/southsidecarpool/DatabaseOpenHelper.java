package carpool.southside.southsidecarpool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
    public static final String SCHEMA = "southside";
    public DatabaseOpenHelper(Context context){
        super(context, SCHEMA, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Person.TABLE_NAME + " ("
                + Person.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Person.COL_NAME + " TEXT, "
                + Person.COL_NUMBER + " TEXT, "
                + Person.COL_COLLEGE + " TEXT, "
                + Person.COL_PROVIDER_FAVORITED + " INTEGER, "
                + Person.COL_RIDER_FAVORITED + " INTEGER);";
        db.execSQL(sql);
        sql = "CREATE TABLE " + Shift.TABLE_NAME + " ("
                + Shift.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Shift.COL_DAY + " TEXT, "
                + Shift.COL_TYPE + " TEXT, "
                + Shift.COL_TIME + " TEXT, "
                + Shift.COL_PROVIDER + " TEXT);";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String sql = "DROP TABLE IF EXISTS " + Person.TABLE_NAME;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + Shift.TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);

    }
    public void deleteAllPeople(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Person.TABLE_NAME);
    }
    public void deleteAllShifts(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Shift.TABLE_NAME);
    }
    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Person.TABLE_NAME, null, null);
        db.delete(Shift.TABLE_NAME, null, null);
    }
    public long insertPerson(Person p){
        SQLiteDatabase db =  getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Person.COL_NAME, p.getPersonName());
        contentValues.put(Person.COL_NUMBER, p.getPersonNumber());
        contentValues.put(Person.COL_COLLEGE, p.getPersonCollege());
        contentValues.put(Person.COL_PROVIDER_FAVORITED, p.getIsFavorited());
        contentValues.put(Person.COL_RIDER_FAVORITED, p.getIsRiderFavorited());
        long id = db.insert(Person.TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }
    public long insertShift(Shift s){
        SQLiteDatabase db =  getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Shift.COL_DAY, s.getShiftDay());
        contentValues.put(Shift.COL_TYPE, s.getShiftType());
        contentValues.put(Shift.COL_TIME, s.getShiftTime());
        contentValues.put(Shift.COL_PROVIDER, s.getShiftProvider());
        long id = db.insert(Person.TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }
    public Person getPerson(int id){
        Person p = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.query(Person.TABLE_NAME,
                null,
                Person.COL_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor.moveToFirst()){
            p = new Person();
            p.setPersonName(cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
            p.setPersonNumber(cursor.getString(cursor.getColumnIndex(Person.COL_NUMBER)));
            p.setPersonCollege(cursor.getString(cursor.getColumnIndex(Person.COL_COLLEGE)));
            p.setIsFavorited(cursor.getInt(cursor.getColumnIndex(Person.COL_PROVIDER_FAVORITED)));
            p.setIsRiderFavorited(cursor.getInt(cursor.getColumnIndex(Person.COL_RIDER_FAVORITED)));
        }
        cursor.close();
        return p;
    }
    public Person getShiftPerson(String name){
        Person p = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.query(Person.TABLE_NAME,
                null,
                Person.COL_NAME + " =? ",
                new String[]{String.valueOf(name)},
                null, null, null);
        if(cursor.moveToFirst()){
            p = new Person();
            p.setPersonName(cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
            p.setPersonNumber(cursor.getString(cursor.getColumnIndex(Person.COL_NUMBER)));
            p.setPersonCollege(cursor.getString(cursor.getColumnIndex(Person.COL_COLLEGE)));
            p.setIsFavorited(cursor.getInt(cursor.getColumnIndex(Person.COL_PROVIDER_FAVORITED)));
            p.setIsRiderFavorited(cursor.getInt(cursor.getColumnIndex(Person.COL_RIDER_FAVORITED)));
        }
        cursor.close();
        return p;
    }
    public Shift getShift(int id){
        Shift s = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.query(Shift.TABLE_NAME,
                null,
                Shift.COL_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor.moveToFirst()){
            s = new Shift();
            s.setShiftDay(cursor.getString(cursor.getColumnIndex(Shift.COL_DAY)));
            s.setShiftType(cursor.getString(cursor.getColumnIndex(Shift.COL_TYPE)));
            s.setShiftTime(cursor.getString(cursor.getColumnIndex(Shift.COL_TIME)));
            s.setShiftProvider(cursor.getString(cursor.getColumnIndex(Shift.COL_PROVIDER)));
            s.setShiftPerson(getShiftPerson(cursor.getString(cursor.getColumnIndex(Shift.COL_PROVIDER))));
        }
        cursor.close();
        return s;
    }
}

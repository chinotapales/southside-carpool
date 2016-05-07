package carpool.southside.southsidecarpool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

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
    public void insertDummyData(){
        SQLiteDatabase db =  getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Person.COL_NAME, "Chino Tapales");
        contentValues.put(Person.COL_NUMBER, "09175524466");
        contentValues.put(Person.COL_COLLEGE, "DLSU");
        contentValues.put(Person.COL_PROVIDER_FAVORITED, 0);
        contentValues.put(Person.COL_RIDER_FAVORITED, 0);
        long id = db.insert(Person.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Person.COL_NAME, "Erika Mison");
        contentValues.put(Person.COL_NUMBER, "09177945907");
        contentValues.put(Person.COL_COLLEGE, "CSB");
        contentValues.put(Person.COL_PROVIDER_FAVORITED, 0);
        contentValues.put(Person.COL_RIDER_FAVORITED, 0);
        id = db.insert(Person.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Person.COL_NAME, "Briana Buencamino");
        contentValues.put(Person.COL_NUMBER, "09175055520");
        contentValues.put(Person.COL_COLLEGE, "CSB");
        contentValues.put(Person.COL_PROVIDER_FAVORITED, 0);
        contentValues.put(Person.COL_RIDER_FAVORITED, 0);
        id = db.insert(Person.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Shift.COL_DAY, "Monday");
        contentValues.put(Shift.COL_TYPE, "ToSchool");
        contentValues.put(Shift.COL_TIME, "7:30 AM");
        contentValues.put(Shift.COL_PROVIDER, "Chino Tapales");
        id = db.insert(Shift.TABLE_NAME, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(Shift.COL_DAY, "Monday");
        contentValues.put(Shift.COL_TYPE, "BackHome");
        contentValues.put(Shift.COL_TIME, "9:00 AM");
        contentValues.put(Shift.COL_PROVIDER, "Briana Buencamino");
        id = db.insert(Shift.TABLE_NAME, null, contentValues);

        db.close();
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
        SQLiteDatabase db = getWritableDatabase();
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
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Shift.COL_DAY, s.getShiftDay());
        contentValues.put(Shift.COL_TYPE, s.getShiftType());
        contentValues.put(Shift.COL_TIME, s.getShiftTime());
        contentValues.put(Shift.COL_PROVIDER, s.getShiftProvider());
        long id = db.insert(Shift.TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }
    public int updatePerson(Person p){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Person.COL_NAME, p.getPersonName());
        contentValues.put(Person.COL_NUMBER, p.getPersonNumber());
        contentValues.put(Person.COL_COLLEGE, p.getPersonCollege());
        contentValues.put(Person.COL_PROVIDER_FAVORITED, p.getIsFavorited());
        contentValues.put(Person.COL_RIDER_FAVORITED, p.getIsRiderFavorited());
        return getWritableDatabase().update(Person.TABLE_NAME,
                contentValues,
                Person.COL_ID + " =? ",
                new String[]{String.valueOf(p.getPersonID())});
    }
    public int updateFavoriteProvider(int id, int favorited){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Person.COL_PROVIDER_FAVORITED, favorited);
        return getWritableDatabase().update(Person.TABLE_NAME,
                contentValues,
                Person.COL_ID + " =? ",
                new String[]{String.valueOf(id)});
    }
    public int updateFavoriteRider(int id, int favorited){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Person.COL_RIDER_FAVORITED, favorited);
        return getWritableDatabase().update(Person.TABLE_NAME,
                contentValues,
                Person.COL_ID + " =? ",
                new String[]{String.valueOf(id)});
    }
    public int updateFavoriteProviderByName(String name, int favorited){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Person.COL_PROVIDER_FAVORITED, favorited);
        return getWritableDatabase().update(Person.TABLE_NAME,
                contentValues,
                Person.COL_NAME + " =? ",
                new String[]{String.valueOf(name)});
    }
    public int updateFavoriteRiderByName(String name, int favorited){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Person.COL_RIDER_FAVORITED, favorited);
        return getWritableDatabase().update(Person.TABLE_NAME,
                contentValues,
                Person.COL_NAME + " =? ",
                new String[]{String.valueOf(name)});
    }
    public boolean getIsFavorited(int id){
        int isFavorited = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor.moveToFirst()){
            isFavorited = cursor.getInt(cursor.getColumnIndex(Person.COL_PROVIDER_FAVORITED));
        }
        cursor.close();
        if(isFavorited == 0){
            return false;
        }
        else return true;
    }
    public boolean getIsRiderFavorited(int id){
        int isFavorited = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor.moveToFirst()){
            isFavorited = cursor.getInt(cursor.getColumnIndex(Person.COL_RIDER_FAVORITED));
        }
        cursor.close();
        if(isFavorited == 0){
            return false;
        }
        else return true;
    }
    public boolean getIsFavoritedByName(String name){
        int isFavorited = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_NAME + " =? ",
                new String[]{String.valueOf(name)},
                null, null, null);
        if(cursor.moveToFirst()){
            isFavorited = cursor.getInt(cursor.getColumnIndex(Person.COL_PROVIDER_FAVORITED));
        }
        cursor.close();
        if(isFavorited == 0){
            return false;
        }
        else return true;
    }
    public boolean getIsRiderFavoritedByName(String name){
        int isFavorited = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_NAME + " =? ",
                new String[]{String.valueOf(name)},
                null, null, null);
        if(cursor.moveToFirst()){
            isFavorited = cursor.getInt(cursor.getColumnIndex(Person.COL_RIDER_FAVORITED));
        }
        cursor.close();
        if(isFavorited == 0){
            return false;
        }
        else return true;
    }
    public String getNumberFromName(String name){
        String nameReturn = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_NAME + " =? ",
                new String[]{String.valueOf(name)},
                null, null, null);
        if(cursor.moveToFirst()){
            nameReturn = cursor.getString(cursor.getColumnIndex(Person.COL_NUMBER));
        }
        return nameReturn;
    }
    public Person getPerson(int id){
        Person p = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
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
    public Shift getShift(int id){
        Shift s = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Shift.TABLE_NAME,
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
        }
        cursor.close();
        return s;
    }
    public Cursor getAllPeople(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }
    public Cursor getAllPeopleByCollege(String college){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_COLLEGE + " =? ",
                new String[]{college},
                null, null, null);
        return cursor;
    }
    public Cursor getAllPeopleByFavProviders(){
        int pos = 1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_PROVIDER_FAVORITED + " =? ",
                new String[]{String.valueOf(pos)},
                null, null, null);
        return cursor;
    }
    public Cursor getAllPeopleByFavRiders(){
        int pos = 1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_RIDER_FAVORITED + " =? ",
                new String[]{String.valueOf(pos)},
                null, null, null);
        return cursor;
    }
    public Cursor getAllShifts(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Shift.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }
    public Cursor getAllShiftsByType(String type){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Shift.TABLE_NAME,
                null,
                Shift.COL_TYPE + " =? ",
                new String[]{type},
                null, null, null);
        return cursor;
    }
    public ArrayList<Object> getArrayListShifts(String type){
        ArrayList<Object> shifts = new ArrayList<>();
        Shift s = null;
        Cursor cursor = getAllShiftsByType(type);
        if(cursor != null) {
            while(cursor.moveToNext()){
                s = new Shift();
                s.setShiftDay(cursor.getString(cursor.getColumnIndex(Shift.COL_DAY)));
                s.setShiftType(cursor.getString(cursor.getColumnIndex(Shift.COL_TYPE)));
                s.setShiftTime(cursor.getString(cursor.getColumnIndex(Shift.COL_TIME)));
                s.setShiftProvider(cursor.getString(cursor.getColumnIndex(Shift.COL_PROVIDER)));
                shifts.add(s);
            }
            cursor.close();
        }
        return shifts;
    }
}

package carpool.southside.southsidecarpool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
    public static final String SCHEMA = "southside";
    public static final String PASSCODE_TABLE = "Passcode";
    public static final String PASSCODE_ID = "passID";
    public static final String PASSCODE = "code";
    public DatabaseOpenHelper(Context context){
        super(context, SCHEMA, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE " + PASSCODE_TABLE + " ("
                + PASSCODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PASSCODE + " TEXT);";
        db.execSQL(sql);
        sql = "CREATE TABLE " + Person.TABLE_NAME + " ("
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
        sql = "CREATE TABLE " + Announcement.TABLE_NAME + " ("
                + Announcement.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Announcement.COL_NAME + " TEXT, "
                + Announcement.COL_DATE + " TEXT, "
                + Announcement.COL_MESSAGE + " TEXT);";
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
    public void initPasscode(){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PASSCODE, "124578");
        long id = db.insert(PASSCODE_TABLE, null, contentValues);
        db.close();
    }
    public boolean isPasscodeCorrect(String passCode){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PASSCODE_TABLE,
                null,
                PASSCODE + " =? ",
                new String[]{String.valueOf(passCode)},
                null, null, null);
        if(cursor.moveToFirst()){
             if(cursor.getString(cursor.getColumnIndex(PASSCODE)).equals(passCode)){
                 return true;
             }
        }
        cursor.close();
        return false;
    }
    public void deleteAllPeople(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Person.TABLE_NAME);
    }
    public void deleteAllShifts(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Shift.TABLE_NAME);
    }

    public void deleteAllAnnouncements(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Announcement.TABLE_NAME);
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
    public long insertAnnouncement(Announcement a){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Announcement.COL_NAME, a.getAnnouncementName());
        contentValues.put(Announcement.COL_DATE, a.getAnnouncementDate());
        contentValues.put(Announcement.COL_MESSAGE, a.getAnnouncementMessage());
        long id = db.insert(Announcement.TABLE_NAME, null, contentValues);
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
    public int updateShift(Shift s){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Shift.COL_DAY, s.getShiftDay());
        contentValues.put(Shift.COL_TYPE, s.getShiftType());
        contentValues.put(Shift.COL_TIME, s.getShiftTime());
        contentValues.put(Shift.COL_PROVIDER, s.getShiftProvider());
        return getWritableDatabase().update(Shift.TABLE_NAME,
                contentValues,
                Shift.COL_ID + " =? ",
                new String[]{String.valueOf(s.getShiftID())});
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
        Cursor cursor = db.query(Person.TABLE_NAME, null, null, null, null, null, Person.COL_NAME + " ASC");
        return cursor;
    }
    public Cursor getAllPeopleByCollege(String college){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_COLLEGE + " =? ",
                new String[]{college},
                null, null, Person.COL_NAME + " ASC");
        return cursor;
    }
    public Cursor getAllPeopleByFavProviders(){
        int pos = 1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_PROVIDER_FAVORITED + " =? ",
                new String[]{String.valueOf(pos)},
                null, null, Person.COL_NAME + " ASC");
        return cursor;
    }
    public Cursor getAllPeopleByFavRiders(){
        int pos = 1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Person.TABLE_NAME,
                null,
                Person.COL_RIDER_FAVORITED + " =? ",
                new String[]{String.valueOf(pos)},
                null, null, Person.COL_NAME + " ASC");
        return cursor;
    }
    public ArrayList<ParentObject> getAssignedTimesByDayAndType(final String day, String type){
        ArrayList<ParentObject> times = new ArrayList<>();
        ArrayList<String> aTimes = new ArrayList<>();
        Set<String> aTimesSet = new HashSet<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Shift.TABLE_NAME,
                null,
                Shift.COL_DAY + " =? AND " + Shift.COL_TYPE + " =? ",
                new String[]{day, type},
                null, null, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                if(!cursor.getString(cursor.getColumnIndex(Shift.COL_TIME)).equals("")){
                    aTimes.add(cursor.getString(cursor.getColumnIndex(Shift.COL_TIME)));
                }
            }
            cursor.close();
        }
        aTimesSet.addAll(aTimes);
        aTimes.clear();
        aTimes.addAll(aTimesSet);
        Collections.sort(aTimes, new Comparator<String>(){
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            @Override
            public int compare(String one, String two) {
                try{
                    return dateFormat.parse(one).compareTo(dateFormat.parse(two));
                }
                catch(ParseException e){
                    throw new IllegalArgumentException(e);
                }
            }
        });
        for(int i = 0; i < aTimes.size(); i++){
            AssignedTime a = new AssignedTime();
            a.setShiftTime(aTimes.get(i));
            times.add(a);
        }
        return times;
    }
    public ArrayList<Object> getArrayListShifts(String day, String type){
        ArrayList<Object> shifts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Shift.TABLE_NAME,
                null,
                Shift.COL_DAY + " =? AND " + Shift.COL_TYPE + " =? ",
                new String[]{day, type},
                null, null, Shift.COL_PROVIDER + " ASC");
        if(cursor != null) {
            while(cursor.moveToNext()){
                Shift s = new Shift();
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
    public ArrayList<Announcement> getArrayListAnnouncements(){
        ArrayList<Announcement> announcements = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Announcement.TABLE_NAME,
                null, null, null, null, null, Announcement.COL_ID + " DESC");
        if(cursor != null) {
            while(cursor.moveToNext()){
                Announcement a = new Announcement();
                a.setAnnouncementName(cursor.getString(cursor.getColumnIndex(Announcement.COL_NAME)));
                a.setAnnouncementDate(cursor.getString(cursor.getColumnIndex(Announcement.COL_DATE)));
                a.setAnnouncementMessage(cursor.getString(cursor.getColumnIndex(Announcement.COL_MESSAGE)));
                announcements.add(a);
            }
            cursor.close();
        }
        return announcements;
    }
    public ArrayList<String> getArrayListFavoriteRiders(){
        ArrayList<String> riders = new ArrayList<>();
        Cursor cursor = getAllPeopleByFavRiders();
        if(cursor != null){
            while(cursor.moveToNext()){
                    riders.add(cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
            }
        }
        return riders;
    }
    public ArrayList<String> getArrayListFavoriteProviders(){
        ArrayList<String> providers = new ArrayList<>();
        Cursor cursor = getAllPeopleByFavProviders();
        if(cursor != null){
            while(cursor.moveToNext()){
                providers.add(cursor.getString(cursor.getColumnIndex(Person.COL_NAME)));
            }
        }
        return providers;
    }
    public void updateFavorites(ArrayList<String> riders, ArrayList<String> providers){
        for(int i=0; i<riders.size(); i++){
            updateFavoriteRiderByName(riders.get(i),1);
            Log.i("DB", "setting favorited rider: "+riders.get(i));
        }
        for(int i=0; i<providers.size(); i++){
            updateFavoriteProviderByName(providers.get(i),1);
            Log.i("DB", "setting favorited provider: "+providers.get(i));
        }
    }
}

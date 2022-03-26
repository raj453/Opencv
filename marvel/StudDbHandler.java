package cultoftheunicorn.marvel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class StudDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RtfdDB";
    private static final String TABLE_NAME = "tbl_student";
    private static final String KEY_ROLL = "stud_roll";
    private static final String KEY_NAME = "stud_name";
    private static final String KEY_CLASS= "stud_class";
    private static final String KEY_MOB= "stud_mob";
    private static final String KEY_PHOTO= "stud_photo";
    private static final String[] COLUMNS = {KEY_ROLL,KEY_NAME, KEY_CLASS,KEY_PHOTO};


    private static final String TABLE_NAME1 = "tbl_student_att";
    private static final String KEY_DTTIME= "att_dttime";
    private static final String KEY_SUBJ= "stud_subj";
    private static final String KEY_STATUS= "att_status";

    private static final String[] COLUMNS1 = {KEY_DTTIME,KEY_ROLL,KEY_CLASS,KEY_SUBJ,KEY_STATUS};


    public StudDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      try {
           String CREATION_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+" stud_sr INTEGER PRIMARY KEY AUTOINCREMENT, "
                   +"stud_roll TEXT,"+" stud_name TEXT,"+" stud_class TEXT,"+" stud_mob TEXT,"+" stud_photo TEXT)";
            db.execSQL(CREATION_TABLE);


          String CREATION_TABLE1 = "CREATE TABLE "+TABLE_NAME1+" ("+" att_sr INTEGER PRIMARY KEY AUTOINCREMENT, "
                  +"att_dttime TEXT,"+" stud_roll TEXT,"+" stud_class TEXT,"+" stud_subj,"+" att_status)";

           db.execSQL(CREATION_TABLE1);

          Log.d("log"," CREATION_TABLE");

        }catch (SQLiteException ex) {
            Log.d("log", ex.getMessage());
        }
        catch (Exception ex) {
            Log.d("log", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(Student stud) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "stud_roll = ? and stud_class = ?", new String[] { stud.getRoll(),stud.getClas()});
        db.close();
    }

    public Student getLastRoll(String query,String val) {
        SQLiteDatabase db;
        Student stud = new Student();
        try {
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null); // h. limit
            if (cursor != null)
                cursor.moveToFirst();
            try {
                if(val.equals("roll"))
                   stud.setRoll(cursor.getString(0));
                if(val.equals("name"))
                    stud.setName(cursor.getString(0));
                if(val.equals("clas"))
                    stud.setClas(cursor.getString(0));
                if(val.equals("mob"))
                    stud.setMob(cursor.getString(0));
                if(val.equals("photo"))
                    stud.setPhoto(cursor.getString(0));
            } catch (CursorIndexOutOfBoundsException e) {
                Log.e("Database Error", e.getMessage());
                if(val.equals("roll"))
                    stud.setRoll("");
                if(val.equals("name"))
                    stud.setName("");
                if(val.equals("clas"))
                    stud.setClas("");
                if(val.equals("mob"))
                    stud.setMob("");
                if(val.equals("photo"))
                    stud.setPhoto("");
            }
        }
        catch (SQLiteException e){
            Log.e("Database Error",e.getMessage());

        }
        return stud;
    }
    public Student getStud(String sroll,String sclass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " stud_roll = ? and stud_class = ?", // c. selections
                new String[] { sroll,sclass }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Student stud = new Student();
        stud.setRoll(cursor.getString(0));
        stud.setName(cursor.getString(1));
        stud.setClas(cursor.getString(2));
        stud.setMob(cursor.getString(3));
        stud.setPhoto(cursor.getString(4));

        return stud;
    }

    public Student getSingleRow(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null)
            cursor.moveToFirst();
        Student stud = new Student();
        try {
            stud.setRoll(cursor.getString(0));
            stud.setName(cursor.getString(1));
            stud.setClas(cursor.getString(2));
            stud.setMob(cursor.getString(3));
            stud.setPhoto(cursor.getString(4));
        }
        catch (CursorIndexOutOfBoundsException e){
            Log.e("Database Error",e.getMessage());
            stud.setRoll("");
            stud.setName("");
            stud.setClas("");
            stud.setMob("");
            stud.setPhoto("");
        }

        return stud;
    }

    public List<Student> allStudents() {

        List<Student> studs = new LinkedList<Student>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Student stud = null;

        if (cursor.moveToFirst()) {
            do {
                stud = new Student();
                stud.setRoll(cursor.getString(0));
                stud.setName(cursor.getString(1));
                stud.setClas(cursor.getString(2));
                stud.setMob(cursor.getString(3));
                stud.setPhoto(cursor.getString(4));
                studs.add(stud);
            } while (cursor.moveToNext());
        }

        return studs;
    }

    public String addStudent(Student stud) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long i=1234_5678_9012_3456L;
        try{
        ContentValues values = new ContentValues();
        values.put(KEY_ROLL, stud.getRoll());
        values.put(KEY_NAME, stud.getName());
        values.put(KEY_CLASS, stud.getClas());
        values.put(KEY_MOB, stud.getMob());
        values.put(KEY_PHOTO, stud.getPhoto());
        // insert
        i = db.insert(TABLE_NAME, null, values);
        db.close();
    }catch (SQLiteException e){Log.e("err",e.getMessage());}
        return  i.toString();
    }

    public int updateStudent(Student stud) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, stud.getName());
        values.put(KEY_MOB, stud.getMob());
        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "stud_roll = ? and stud_class = ?", // selections
                new String[] {stud.getRoll(),stud.getClas()});
        db.close();
        return i;
    }


    public void deleteOneAtt(Student stud) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME1, "stud_roll = ? and stud_class = ? and stud_subj = ?", new String[] { stud.getRoll(),stud.getClas()});
        db.close();
    }

    public StudentAtt getLastRollAtt(String query,String val) {
        SQLiteDatabase db;
        StudentAtt studatt = new StudentAtt();
        try {
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null); // h. limit
            if (cursor != null)
                cursor.moveToFirst();
            try {
                if(val.equals("dttime"))
                    studatt.setDt(cursor.getString(0));
                if(val.equals("roll"))
                    studatt.setRoll(cursor.getString(0));
                if(val.equals("clas"))
                    studatt.setClas(cursor.getString(0));
                if(val.equals("subj"))
                    studatt.setSubj(cursor.getString(0));
                if(val.equals("status"))
                    studatt.setStatus(cursor.getString(0));
            } catch (CursorIndexOutOfBoundsException e) {
                Log.e("Database Error", e.getMessage());
                if(val.equals("roll"))
                    studatt.setRoll("");
                if(val.equals("dttime"))
                    studatt.setDt("");
                if(val.equals("clas"))
                    studatt.setClas("");
                if(val.equals("subj"))
                    studatt.setSubj("");
                if(val.equals("status"))
                    studatt.setStatus("");
            }
        }
        catch (SQLiteException e){
            Log.e("Database Error",e.getMessage());

        }
        return studatt;
    }
    public StudentAtt getStudAtt(String sroll,String sclass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME1, // a. table
                COLUMNS1, // b. column names
                " stud_roll = ? and stud_class = ? and stud_subj= ?", // c. selections
                new String[] { sroll,sclass }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        StudentAtt studatt = new StudentAtt();
        studatt.setRoll(cursor.getString(0));
        studatt.setDt(cursor.getString(1));
        studatt.setClas(cursor.getString(2));
        studatt.setSubj(cursor.getString(3));
        studatt.setStatus(cursor.getString(4));
        return studatt;
    }

    public StudentAtt getSingleRowAtt(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null)
            cursor.moveToFirst();
        StudentAtt studatt = new StudentAtt();
        try {
            studatt.setDt(cursor.getString(0));
            studatt.setRoll(cursor.getString(1));
            studatt.setClas(cursor.getString(2));
            studatt.setSubj(cursor.getString(3));
            studatt.setStatus(cursor.getString(4));
        }
        catch (CursorIndexOutOfBoundsException e){
            Log.e("Database Error",e.getMessage());
            studatt.setRoll("");
            studatt.setDt("");
            studatt.setClas("");
            studatt.setSubj("");
            studatt.setStatus("");
        }

        return studatt;
    }
    public Cursor getAllData(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }
    public List<StudentAtt> allStudentsAtt(String query) {

        List<StudentAtt> studatts = new LinkedList<StudentAtt>();
        //  String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        StudentAtt studatt = null;

        if (cursor.moveToFirst()) {
            do {
                studatt = new StudentAtt();
                studatt.setDt(cursor.getString(0));
                studatt.setRoll(cursor.getString(1));
                studatt.setClas(cursor.getString(2));
                studatt.setSubj(cursor.getString(3));
                studatt.setStatus(cursor.getString(4));
                studatts.add(studatt);
            } while (cursor.moveToNext());
        }

        return studatts;
    }

    public String addStudentAtt(StudentAtt studatt) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long i=1234_5678_9012_3456L;
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_ROLL, studatt.getRoll());
            values.put(KEY_DTTIME, studatt.getDt());
            values.put(KEY_CLASS, studatt.getClas());
            values.put(KEY_SUBJ, studatt.getSubj());
            values.put(KEY_STATUS, studatt.getStatus());
            // insert
            i = db.insert(TABLE_NAME1, null, values);
            db.close();
        }catch (SQLiteException e){Log.e("err",e.getMessage());}
        return  i.toString();
    }

    public int updateStudentAtt(StudentAtt studatt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DTTIME, studatt.getDt());
        values.put(KEY_STATUS, studatt.getStatus());
        // values.put(KEY_MOB, stud.getMob());
        int i = db.update(TABLE_NAME1, // table
                values, // column/value
                "stud_roll = ? and stud_class = ? and stud_subj= ?", // selections
                new String[] {studatt.getRoll(),studatt.getClas(),studatt.getSubj()});
        db.close();
        return i;
    }


}
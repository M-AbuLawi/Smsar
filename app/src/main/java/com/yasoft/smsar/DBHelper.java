package com.yasoft.smsar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.models.Smsar;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Smsar.db";

    //CREATE TABLE StartInterface
    public static final String SMSAR_TABLE_NAME = "Smsars";
    public static final String SMSAR_COLUMN_USERNAME = "username";
    public static final String SMSAR_COLUMN_NAME = "name";
    public static final String SMSAR_COLUMN_EMAIL = "email";
    public static final String SMSAR_COLUMN_PASSWORD= "password";
    public static final String SMSAR_COLUMN_PHONE = "phone";

    // CREATE TABLE Property
    public static final String PROPERTY_TABLE_NAME = "Property";
    public static final String  PROPERTY_COLUMN__PROPERTYID = "propertyID";
    public static final String  PROPERTY_COLUMN_SMSARUSERNAME = "smsarUsername";
    public static final String  PROPERTY_COLUMN__CITY = "city";
    public static final String  PROPERTY_COLUMN__DESCRIPTION = "description";
    public static final String  PROPERTY_COLUMN__PRICE = "price";
    public static final String  PROPERTY_COLUMN__NOROOMS = "noRooms";
    public static final String  PROPERTY_COLUMN__NOBATHROOMS = "noBathrooms";
    public static final String  PROPERTY_COLUMN__PARKING = "parking";
    public static final String  PROPERTY_COLUMN__ADDRESS = "address";
    public static final String  PROPERTY_COLUMN__DATE= "date";
    public static final String  PROPERTY_COLUMN__AREA = "area";

    // CREATE TABLE Images
    public static final String IMAGES_TABLE_NAME = "Images";
    public static final String  IMAGES_COLUMN__PROPERTYID = "propertyID";
    public static final String  IMAGES_COLUMN_IMAGEID= "imageID";
    public static final String  IMAGES_COLUMN__IMAGE = "image";

    private HashMap hp;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 19);
    }

    @Override
    public void onCreate(SQLiteDatabase db) throws SQLException{
        // TODO Auto-generated method stub
        db.execSQL(
                "create table Smsars " +
                        "(username text primary key unique, name text,email text, password text," +
                        "phone text)"
        );
        db.execSQL(
                "create table Property " +
                        "(propertyID INTEGER primary key AUTOINCREMENT unique, smsarUsername TEXT " +
                        ",city TEXT, picture BLOB,description TEXT,price float ,noRooms INTEGER ," +
                            "noBathrooms INTEGER ,parking BOOLEAN ,address TEXT , date TEXT ," +
                        "area TEXT, " +
                        "FOREIGN KEY(`smsarUsername`) REFERENCES `Smsar`(`username`))"
        );
        db.execSQL(
                "create table Images " +
                        "(imageID INTEGER primary key AUTOINCREMENT unique, propertyID INTEGER " +
                        ",image BLOB,"  +
                        "FOREIGN KEY(`propertyID`) REFERENCES `Property`(`propertyID`))"
        );

        db.execSQL(
               "create view propertyDataView AS " +
                       "select username ,name , phone, city " +
                       "from Smsars "+
                       "inner join Property ON Smsars.username=Property.smsarUsername"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)throws SQLException {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Smsars");
        db.execSQL("DROP TABLE IF EXISTS Property");
        db.execSQL("DROP TABLE IF EXISTS Images");
        db.execSQL("DROP View IF EXISTS propertyDataView");
        onCreate(db);
    }

    public boolean insertProperty (String smsarusername, String City, String Description,
                                   float Price, int numRoom, int numBaths, boolean parking,String address,
                                   String date,String area)throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("city", City);
        contentValues.put("smsarUsername", smsarusername);
        contentValues.put("description", Description);
        contentValues.put("price", Price);
        contentValues.put("noRooms", numRoom);
        contentValues.put("noBathrooms", numBaths);
        contentValues.put("parking", parking);
        contentValues.put("address", address);
        contentValues.put("date",date);
        contentValues.put("area",area);
        db.insert("Property", null, contentValues);
        return true;
    }
    public boolean insertImage (int Property_ID)throws SQLException {

        return true;
    }
    public boolean insertSmsar (String username,String name, String email, String password, String phone)throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        db.insert("Smsars", null, contentValues);
        return true;
    }

    public Cursor getDataCity(String citY)throws SQLException {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from Property where city='"+citY+"'", null);
            return res;

          }
    public Cursor getData(String userName)throws SQLException {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Smsars where username='"+userName+"'", null);
        res.moveToFirst();
        return res;

    }

    public Cursor getProperty(int id)throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Property where propertyID='"+id+"'", null);
        res.moveToFirst();
        return res;

    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SMSAR_TABLE_NAME);
        return numRows;
    }

    public boolean updateSmsar (String username,String name, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        db.update("Smsars", contentValues, "username = ? ", new String[] { username } );
        return true;
    }


    public void deleteSmsar (String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Smsars",
                "username = ? ",
                new String[] { (username) });
        db.delete("Property",
                "smsarUsername = ? ",
                new String[] { (username) });

    }

    public boolean deletePropperty (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

       //  db.execSQL("delete from Property where propertyID='id'",null);
        return db.delete(PROPERTY_TABLE_NAME, PROPERTY_COLUMN__PROPERTYID + "=" + id, null) > 0;
    }



    //For ArrayList and Adapters
    public ArrayList<Property> getAllProperty(String Username){
        ArrayList<Property> array_list = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from Property where smsarUsername='"+Username+"'",null);
        res.moveToFirst();

        while(res.moveToNext()){
            int id =res.getInt(res.getColumnIndex(PROPERTY_COLUMN__PROPERTYID));
            String city=res.getString(res.getColumnIndex(PROPERTY_COLUMN__CITY));
            String desc=res.getString(res.getColumnIndex(PROPERTY_COLUMN__DESCRIPTION));
            String price=res.getString(res.getColumnIndex(PROPERTY_COLUMN__PRICE));
           // String username=res.getString(res.getColumnIndex(PROPERTY_COLUMN_SMSARUSERNAME));

        }

        return array_list;

    }

    //For Cursor;
/*    public String getProperty(int id){

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from Property where propertyID= 30 ",null);
        res.moveToFirst();
            return res.getString(res.getColumnIndex(PROPERTY_COLUMN__DESCRIPTION));

    }*/
    //For Cursor;

    public int getPropertyID(String Username){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select propertyID from Property where smsarUsername='"+Username+"'",null);
        res.moveToFirst();
       int id= res.getColumnIndex(PROPERTY_COLUMN__PROPERTYID);

        return id;

    }

    public ArrayList<Property> getAllProperty(){
        ArrayList<Property> array_list = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from Property ",null);
        res.moveToFirst();

        while(res.moveToNext()){
            int id =res.getInt(res.getColumnIndex(PROPERTY_COLUMN__PROPERTYID));
            String city=res.getString(res.getColumnIndex(PROPERTY_COLUMN__CITY));
            String desc=res.getString(res.getColumnIndex(PROPERTY_COLUMN__DESCRIPTION));
            String price=res.getString(res.getColumnIndex(PROPERTY_COLUMN__PRICE));
            String username=res.getString(res.getColumnIndex(PROPERTY_COLUMN_SMSARUSERNAME));
        //    Property property=new Property(id,username,city,desc,price);
           // array_list.add(property);

        }

        return array_list;

    }
    public ArrayList<Property> getAllProperty(int rooms,int baths,boolean parking){
        ArrayList<Property> array_list = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from Property WHERE noRooms = '"+rooms+"' AND noBathrooms = '"+baths+"' AND parking = '"+parking+"'  ",null);
        res.moveToFirst();

        while(res.moveToNext()){
            int id =res.getInt(res.getColumnIndex(PROPERTY_COLUMN__PROPERTYID));
            String city=res.getString(res.getColumnIndex(PROPERTY_COLUMN__CITY));
            String desc=res.getString(res.getColumnIndex(PROPERTY_COLUMN__DESCRIPTION));
            String price=res.getString(res.getColumnIndex(PROPERTY_COLUMN__PRICE));
            String username=res.getString(res.getColumnIndex(PROPERTY_COLUMN_SMSARUSERNAME));
        //    Property property=new Property(id,username,city,desc,price);
        //    array_list.add(property);

        }

        return array_list;

    }

    public Cursor getAllData(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Smsars", null);

        return res;
    }

    public ArrayList<String> propertyView(String Username){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from propertyDataView where username='"+Username+"'",null);

        res.moveToFirst();

        while(res.moveToNext()){
            array_list.add(res.getString(res.getColumnIndex(PROPERTY_COLUMN__CITY)));
            array_list.add(res.getString(res.getColumnIndex(PROPERTY_COLUMN__DESCRIPTION)));
            array_list.add(res.getString(res.getColumnIndex(PROPERTY_COLUMN__PRICE)));

        }

        return array_list;
    }
    public ArrayList<Smsar> getAllSmsar(String userName) {
        ArrayList<Smsar> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Smsars where username='" + userName + "'", null );
        res.moveToFirst();

        while(res.moveToNext()){
            String username=res.getString(res.getColumnIndex(SMSAR_COLUMN_USERNAME));
            String name=res.getString(res.getColumnIndex(SMSAR_COLUMN_NAME));
            String pn=res.getString(res.getColumnIndex(SMSAR_COLUMN_PHONE));
            Smsar smsarModel=new Smsar(username,name,pn);
            array_list.add(smsarModel);

        }

        return array_list;
    }
    public ArrayList<Smsar> getAllSmsar() {
        ArrayList<Smsar> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Smsars ", null );
        res.moveToFirst();

        while(res.moveToNext()){
            String username=res.getString(res.getColumnIndex(SMSAR_COLUMN_USERNAME));
            String name=res.getString(res.getColumnIndex(SMSAR_COLUMN_NAME));
            String pn=res.getString(res.getColumnIndex(SMSAR_COLUMN_PHONE));
            Smsar smsarModel=new Smsar(username,name,pn);
            array_list.add(smsarModel);

        }

        return array_list;
    }

    public String getPhone(String userName) {

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =db.rawQuery( "select * from Smsars where username='" + userName + "'", null );
            res.moveToFirst();
       // return res;
            return res.getString(res.getColumnIndex(SMSAR_COLUMN_PHONE));
    }
}

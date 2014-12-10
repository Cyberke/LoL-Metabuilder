package be.howest.lolmetabuilder.DAL;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

/**
 * Created by Milan on 10/12/2014.
 */
public class DatabaseAccess {
    public DatabaseAccess () {}

    public static void checkDB()
    {
        boolean newDB = false;

        try{
            //Probeer DB te openen
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/databases/loldb", null,0);

            //Haal versienummer op, controleer of de db verouderd is
            Cursor resultSet = db.rawQuery("SELECT * FROM Version",null);
            resultSet.moveToFirst();
            String dbversion = resultSet.getString(0);



            //Als DB verouderd is -> leegmaken en newDB = true


            db.close();
        } catch(SQLiteException e) {
            //Als DB niet geopened kan worden, nieuw maken
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("loldb", null);

            /* Queries voor tables aan te maken
            db.execSQL("CREATE TABLE IF NOT EXISTS LIST(wlist varchar);");
            db.execSQL("INSERT INTO LIST VALUES('খবর');");
            db.execSQL("INSERT INTO LIST VALUES('কবর');");
            */
            db.close();
            newDB = true;
        }

        if(newDB)
        {
            //Data ophalen van API en inserten in DB
        }
    }

}

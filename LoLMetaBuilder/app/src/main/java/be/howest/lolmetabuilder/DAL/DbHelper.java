package be.howest.lolmetabuilder.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Milan on 20/01/2015.
 */
public class DbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "loldb";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        try
        {
            database.execSQL("CREATE TABLE Spell (id integer , name text , description text , tooltip text , cost text , cooldown text , range text , image text )");
            database.execSQL("CREATE TABLE Tip_Champion ( id integer primary key autoincrement , champID integer , tipID integer )");
            database.execSQL("CREATE TABLE SpellEffect ( id integer , value text )");
            database.execSQL("CREATE TABLE Tip ( id integer , content text , isAlly boolean , champID integer )");
            database.execSQL("CREATE TABLE Effect ( id integer , name text , value real )");
            database.execSQL("CREATE TABLE Tag ( id integer , name text )");
            database.execSQL("CREATE TABLE Stat ( id integer , name text , value real )");
            database.execSQL("CREATE TABLE Champion ( id integer , name text , title text , lore text , attack integer , defense integer , magic integer , difficulty integer , passiveName text , passiveDesc text , image text , priceRP integer, priceIP integer)");
            database.execSQL("CREATE TABLE Item ( id integer , totalGold integer , baseGold integer , depth integer , specialRecipe integer , map integer , stacks integer , purchasable integer , consumed boolean , name text , description text , [group] text , image text )");
            database.execSQL("CREATE TABLE Rune ( id integer , name text , description text , tier integer , type text , image text )");
            database.execSQL("CREATE TABLE Skin ( id integer , name text , image text )");
            database.execSQL("CREATE TABLE MasteryTree ( id integer , name text )");
            database.execSQL("CREATE TABLE Leaf ( id integer , ranks integer , prereq integer , name text , description text )");
            database.execSQL("CREATE TABLE Champion_Spell ( id integer primary key autoincrement , champID integer , spellID integer )");
            database.execSQL("CREATE TABLE Tag_Champion ( id integer primary key autoincrement , champID integer , tagID integer )");
            database.execSQL("CREATE TABLE Stat_Champion ( id integer primary key autoincrement , champID integer , statID integer )");
            database.execSQL("CREATE TABLE SpellEffect_Spell ( id integer primary key autoincrement , spellID integer , spelleffectID integer )");
            database.execSQL("CREATE TABLE Stat_Item ( id integer primary key autoincrement , itemID integer , statID integer )");
            database.execSQL("CREATE TABLE Tag_Item ( id integer primary key autoincrement , itemID integer , tagID integer )");
            database.execSQL("CREATE TABLE Effect_Item ( id integer primary key autoincrement , itemID integer , effectID integer )");
            database.execSQL("CREATE TABLE Item_Upgrade ( id integer primary key autoincrement, itemID integer , otherItem integer , required boolean )");
            database.execSQL("CREATE TABLE Stat_Rune ( id integer primary key autoincrement , runeID integer , statID integer )");
            database.execSQL("CREATE TABLE Tag_Rune ( id integer primary key autoincrement , runeID integer , tagID integer )");
            database.execSQL("CREATE TABLE MasteryTree_Leaf ( id integer primary key autoincrement , masterytreeID integer , leafID integer )");
        }
        catch(Exception e)
        {
            Log.d("Debug:", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion)
    {
        try
        {
            database.execSQL("DROP TABLE IF EXISTS Spell");
            database.execSQL("DROP TABLE IF EXISTS Tip_Champion");
            database.execSQL("DROP TABLE IF EXISTS SpellEffect");
            database.execSQL("DROP TABLE IF EXISTS Tip");
            database.execSQL("DROP TABLE IF EXISTS Effect");
            database.execSQL("DROP TABLE IF EXISTS Tag");
            database.execSQL("DROP TABLE IF EXISTS Stat");
            database.execSQL("DROP TABLE IF EXISTS Champion");
            database.execSQL("DROP TABLE IF EXISTS Item");
            database.execSQL("DROP TABLE IF EXISTS Rune");
            database.execSQL("DROP TABLE IF EXISTS Skin");
            database.execSQL("DROP TABLE IF EXISTS MasteryTree");
            database.execSQL("DROP TABLE IF EXISTS Leaf");
            database.execSQL("DROP TABLE IF EXISTS Champion_Spell");
            database.execSQL("DROP TABLE IF EXISTS Tag_Champion");
            database.execSQL("DROP TABLE IF EXISTS Stat_Champion");
            database.execSQL("DROP TABLE IF EXISTS SpellEffect_Spell");
            database.execSQL("DROP TABLE IF EXISTS Stat_Item");
            database.execSQL("DROP TABLE IF EXISTS Tag_Item");
            database.execSQL("DROP TABLE IF EXISTS Effect_Item");
            database.execSQL("DROP TABLE IF EXISTS Item_Upgrade");
            database.execSQL("DROP TABLE IF EXISTS Stat_Rune");
            database.execSQL("DROP TABLE IF EXISTS Tag_Rune");
            database.execSQL("DROP TABLE IF EXISTS MasteryTree_Leaf");
            onCreate(database);
        }
        catch(Exception e)
        {
            Log.d("Debug:", e.getMessage());
        }
    }
}

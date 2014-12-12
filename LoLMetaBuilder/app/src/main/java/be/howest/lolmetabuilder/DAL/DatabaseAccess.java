package be.howest.lolmetabuilder.DAL;

import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.*;

/**
 * Created by Milan on 10/12/2014.
 */
public class DatabaseAccess {
    public DatabaseAccess () {}

    public static void checkDB()
    {
        boolean newDB = false;

        try {
            //Probeer DB te openen
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/databases/loldb", null,0);

            //Haal versienummer op, controleer of de db verouderd is
            Cursor resultSet = db.rawQuery("SELECT * FROM Version",null);
            resultSet.moveToFirst();
            String dbversion = resultSet.getString(0);
            //String apiversion = api_ophalen.getApiVersion();

            //Als DB verouderd is -> db.remove() en newDB = true

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

        if(newDB) {
            //DB aanmaken
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("loldb", null);

            //Hoofdtabellen
            //Champion
            db.execSQL("CREATE TABLE Champion ( " +
                    "    id          INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                        NOT NULL," +
                    "    name        TEXT    NOT NULL," +
                    "    title       TEXT    NOT NULL," +
                    "    lore        TEXT    NOT NULL," +
                    "    attack      INTEGER NOT NULL," +
                    "    defense     INTEGER NOT NULL," +
                    "    magic       INTEGER NOT NULL," +
                    "    difficulty  INTEGER NOT NULL," +
                    "    passiveName TEXT    NOT NULL," +
                    "    passiveDesc TEXT    NOT NULL," +
                    "    priceRP     INTEGER," +
                    "    priceIP     INTEGER," +
                    "    tipID       INTEGER NOT NULL" +
                    "                        REFERENCES Tip ( id )," +
                    "    tagID       INTEGER NOT NULL" +
                    "                        REFERENCES Tag ( id )," +
                    "    statID      INTEGER NOT NULL," +
                    "    spellID     INTEGER NOT NULL" +
                    "                        REFERENCES Champion_Spell ( id )," +
                    "    FOREIGN KEY ( statID ) REFERENCES Stat ( id ) );");

            //Spell
            db.execSQL("CREATE TABLE Spell ( " +
                    "    id          INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                        NOT NULL," +
                    "    name        TEXT    NOT NULL," +
                    "    description TEXT    NOT NULL," +
                    "    tooltip     TEXT    NOT NULL," +
                    "    cooldown    TEXT    NOT NULL," +
                    "    range       TEXT    NOT NULL," +
                    "    image       TEXT    NOT NULL );");

            //Effect
            db.execSQL("CREATE TABLE Effect ( " +
                    "    id    INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                  NOT NULL," +
                    "    name  TEXT    NOT NULL," +
                    "    value REAL    NOT NULL );");

            //Freechamp
            db.execSQL("CREATE TABLE Freechamp ( " +
                    "    id   INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                 NOT NULL," +
                    "    name TEXT    NOT NULL );");

            //Item
            db.execSQL("CREATE TABLE Item ( " +
                    "    id            INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                          NOT NULL," +
                    "    totalGold     INTEGER NOT NULL," +
                    "    baseGold      INTEGER NOT NULL," +
                    "    depth         INTEGER NOT NULL," +
                    "    specialRecipe INTEGER NOT NULL," +
                    "    map           INTEGER NOT NULL," +
                    "    stacks        INTEGER NOT NULL," +
                    "    purchasable   BOOLEAN NOT NULL," +
                    "    consumed      BOOLEAN NOT NULL," +
                    "    name          TEXT    NOT NULL," +
                    "    description   TEXT    NOT NULL," +
                    "    [group]       TEXT    NOT NULL," +
                    "    image         TEXT    NOT NULL," +
                    "    statID        INTEGER NOT NULL" +
                    "                          REFERENCES Stat ( id ) );");

            /*
            private ArrayList<Stat> stats = new ArrayList<Stat>();
            private ArrayList<Tag> tags = new ArrayList<Tag>();
            private ArrayList<Effect> effects = new ArrayList<Effect>();
            private ArrayList<Integer> requiresIds = new ArrayList<Integer>();
            private ArrayList<Integer> buildIntoIds = new ArrayList<Integer>();
             */
            //Rune
            db.execSQL("CREATE TABLE Rune ( " +
                    "    id          INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                        NOT NULL," +
                    "    tier        INTEGER NOT NULL," +
                    "    name        TEXT    NOT NULL," +
                    "    description TEXT    NOT NULL," +
                    "    type        TEXT    NOT NULL );");

            //Rune_Effect

            //Skin
            db.execSQL("CREATE TABLE Skin ( " +
                    "    id         INTEGER NOT NULL," +
                    "    championID INTEGER NOT NULL," +
                    "    name       TEXT    NOT NULL," +
                    "    image      TEXT    NOT NULL," +
                    "    FOREIGN KEY ( championID ) REFERENCES Champion ( id ));");

            //Spell
            db.execSQL("CREATE TABLE Spell ( " +
                    "    id          INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                        NOT NULL," +
                    "    name        TEXT    NOT NULL," +
                    "    description TEXT    NOT NULL," +
                    "    tooltip     TEXT    NOT NULL," +
                    "    cooldown    TEXT    NOT NULL," +
                    "    range       TEXT    NOT NULL," +
                    "    image       TEXT    NOT NULL );");
            //Spell_Effect

            //Stat
            db.execSQL("CREATE TABLE Stat ( " +
                    "    id    INTEGER NOT NULL," +
                    "    name  TEXT    NOT NULL," +
                    "    value REAL    NOT NULL );");

            //Tag
            db.execSQL("CREATE TABLE Tag ( " +
                    "    id   INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                 NOT NULL," +
                    "    name TEXT    NOT NULL );");

            //Tip
            db.execSQL("CREATE TABLE Tip ( " +
                    "    id      INTEGER NOT NULL," +
                    "    champID INTEGER NOT NULL," +
                    "    isAlly  BOOLEAN NOT NULL," +
                    "    content TEXT    NOT NULL," +
                    "    FOREIGN KEY ( champID ) REFERENCES Champion ( id ) );");

            //MasteryTree
            db.execSQL("CREATE TABLE masterytree ( " +
                    "    id   INTEGER NOT NULL," +
                    "    name TEXT    NOT NULL );");

            //Leaf
            db.execSQL("CREATE TABLE leaf ( " +
                    "    id          INTEGER NOT NULL," +
                    "    ranks       INTEGER NOT NULL," +
                    "    prereq      INTEGER NOT NULL," +
                    "    treeID      INTEGER NOT NULL," +
                    "    name        TEXT    NOT NULL," +
                    "    description TEXT    NOT NULL," +
                    "    FOREIGN KEY ( treeID ) REFERENCES masterytree ( id ) );");

            //Tussentabellen
            //Champion_Spell
            db.execSQL("CREATE TABLE Champion_Spell ( " +
                    "    id      INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                    NOT NULL," +
                    "    champID INTEGER NOT NULL," +
                    "    spellID INTEGER NOT NULL," +
                    "    FOREIGN KEY ( champID ) REFERENCES Champion ( id )," +
                    "    FOREIGN KEY ( spellID ) REFERENCES Spell ( id ) );");

            //Champion_Tag
            db.execSQL("CREATE TABLE Champion_Tag ( " +
                    "    id         INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                       NOT NULL," +
                    "    championID INTEGER NOT NULL," +
                    "    tagID      INTEGER NOT NULL," +
                    "    FOREIGN KEY ( championID ) REFERENCES Champion ( id )," +
                    "    FOREIGN KEY ( tagID ) REFERENCES Tag ( id ) );");

            //Effect_Spell
            db.execSQL("CREATE TABLE Effect_Spell ( " +
                    "    id       INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                     NOT NULL," +
                    "    spellID  INTEGER NOT NULL," +
                    "    effectID INTEGER NOT NULL," +
                    "    FOREIGN KEY ( spellID ) REFERENCES spell ( id )," +
                    "    FOREIGN KEY ( effectID ) REFERENCES effect ( id ) );");

            //Item_Stat
            db.execSQL("CREATE TABLE Item_Stat ( " +
                    "    id     INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                   NOT NULL," +
                    "    itemID INTEGER NOT NULL," +
                    "    statID INTEGER NOT NULL," +
                    "    FOREIGN KEY ( itemID ) REFERENCES Item ( id )," +
                    "    FOREIGN KEY ( statID ) REFERENCES Stat ( id ) );");

            //Rune_Stat
            db.execSQL("CREATE TABLE Rune_Stat ( " +
                    "    id     INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                   NOT NULL," +
                    "    runeID INTEGER NOT NULL," +
                    "    statID INTEGER NOT NULL," +
                    "    FOREIGN KEY ( runeID ) REFERENCES Rune ( id )," +
                    "    FOREIGN KEY ( statID ) REFERENCES Stat ( id ) );");

            //Item_Req
            db.execSQL("CREATE TABLE Item_Req ( " +
                    "    id        INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    itemID    INTEGER REFERENCES Item ( id )," +
                    "    otherItem INTEGER REFERENCES Item ( id )," +
                    "    required  BOOLEAN NOT NULL );");


            //MasteryTree_Leaf


            //Data ophalen van API en inserten in DB

            //Arraylists models opvragen van api
            //invullen in queries naar DB

        }
    }


}

package be.howest.lolmetabuilder.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Milan on 12/12/2014.
 */
public class Helper extends SQLiteOpenHelper {

    private static Helper INSTANCE;
    private static Object lock = new Object();

    private static final String DB_NAME = "loldb.db";
    private static final int DB_VERSION = 1;

    private Helper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static Helper getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (lock) {
                if(INSTANCE == null) {
                    INSTANCE = new Helper(context.getApplicationContext());
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { onUpgrade(db, 0, DB_VERSION); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop all tables
        //Tabellen die in tussentabellen gebruikt worden eerst
        db.execSQL("DROP TABLE Tip");
        db.execSQL("DROP TABLE Effect");
        db.execSQL("DROP TABLE Tag");
        db.execSQL("DROP TABLE Stat");

        //hoofdtabellen
        db.execSQL("DROP TABLE Champion");
        db.execSQL("DROP TABLE Spell");
        db.execSQL("DROP TABLE Freechamp");
        db.execSQL("DROP TABLE Item");
        db.execSQL("DROP TABLE Rune");
        db.execSQL("DROP TABLE Skin");
        db.execSQL("DROP TABLE Spell");
        db.execSQL("DROP TABLE Masterytree");
        db.execSQL("DROP TABLE Leaf");

        //tussentabellen
        db.execSQL("DROP TABLE Champion_Spell");
        db.execSQL("DROP TABLE Champion_Tag");
        db.execSQL("DROP TABLE Champion_Stat");
        db.execSQL("DROP TABLE Spell_Effect");
        db.execSQL("DROP TABLE Item_Stat");
        db.execSQL("DROP TABLE Item_Tag");
        db.execSQL("DROP TABLE Item_Effect");
        db.execSQL("DROP TABLE Item_Upgrade");
        db.execSQL("DROP TABLE Rune_Stat");
        db.execSQL("DROP TABLE Masterytree_Leaf");

        //TODO: in iedere createTable functie moeten de aangemaakte tabellen ingevuld worden
        //Recreate all tables
        //Tabellen die in tussentabellen gebruikt worden eerst
        createTipTableV1(db);
        createEffectTableV1(db);
        createTagTableV1(db);
        createStatTableV1(db);

        //hoofdtabellen
        createChampionTableV1(db);
        createSpellTableV1(db);
        createFreeChampTableV1(db);
        createItemTableV1(db);
        createRuneTableV1(db);
        createSkinTableV1(db);
        createSpellTableV1(db);
        createMasteryTreeTableV1(db);
        createLeafTableV1(db);

        //tussentabellen
        createChampion_SpellTableV1(db);
        createChampion_TagTableV1(db);
        createChampion_StatTableV1(db);
        createSpell_EffectTableV1(db);
        createItem_StatTableV1(db);
        createItem_TagTableV1(db);
        createItem_EffectTableV1(db);
        createItem_UpgradeTableV1(db);
        createRune_StatTableV1(db);
        createMasteryTree_LeafTableV1(db);
    }

    private void createTipTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tip ( \n" +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    NOT NULL,\n" +
                "    content TEXT    NOT NULL,\n" +
                "    isAlly  BOOLEAN NOT NULL,\n" +
                "    champID INTEGER NOT NULL,\n" +
                "    FOREIGN KEY ( champID ) REFERENCES Champion ( id ) \n" +
                ");";
        db.execSQL(sql);


    }

    private void createEffectTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Effect ( \n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                  NOT NULL,\n" +
                "    name  TEXT    NOT NULL,\n" +
                "    value REAL    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createTagTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tag ( \n" +
                "    id   INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                 NOT NULL,\n" +
                "    name TEXT    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createStatTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat ( \n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                  NOT NULL,\n" +
                "    name  TEXT    NOT NULL,\n" +
                "    value REAL    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createChampionTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Champion ( \n" +
                "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                        NOT NULL,\n" +
                "    name        TEXT    NOT NULL,\n" +
                "    title       TEXT    NOT NULL,\n" +
                "    lore        TEXT    NOT NULL,\n" +
                "    attack      INTEGER NOT NULL,\n" +
                "    defense     INTEGER NOT NULL,\n" +
                "    magic       INTEGER NOT NULL,\n" +
                "    difficulty  INTEGER NOT NULL,\n" +
                "    passiveName TEXT    NOT NULL,\n" +
                "    passiveDesc TEXT    NOT NULL,\n" +
                "    priceRP     INTEGER,\n" +
                "    priceIP     INTEGER,\n" +
                "    tagID       INTEGER NOT NULL\n" +
                "                        REFERENCES Tag ( id ),\n" +
                "    statID      INTEGER NOT NULL,\n" +
                "    spellID     INTEGER NOT NULL\n" +
                "                        REFERENCES Champion_Spell ( id ),\n" +
                "    FOREIGN KEY ( statID ) REFERENCES Stat ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createSpellTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Spell ( \n" +
                "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                        NOT NULL,\n" +
                "    name        TEXT    NOT NULL,\n" +
                "    description TEXT    NOT NULL,\n" +
                "    tooltip     TEXT    NOT NULL,\n" +
                "    cost        TEXT    NOT NULL,\n" +
                "    cooldown    TEXT    NOT NULL,\n" +
                "    range       TEXT    NOT NULL,\n" +
                "    image       TEXT    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createFreeChampTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Freechamp ( \n" +
                "    id   INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                 NOT NULL,\n" +
                "    name TEXT    NOT NULL \n" +
                ");";
        db.execSQL(sql);


    }

    private void createItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item ( \n" +
                "    id            INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    totalGold     INTEGER NOT NULL,\n" +
                "    baseGold      INTEGER NOT NULL,\n" +
                "    depth         INTEGER NOT NULL,\n" +
                "    specialRecipe INTEGER NOT NULL,\n" +
                "    map           INTEGER NOT NULL,\n" +
                "    stacks        INTEGER NOT NULL,\n" +
                "    purchasable   BOOLEAN NOT NULL,\n" +
                "    consumed      BOOLEAN NOT NULL,\n" +
                "    name          TEXT    NOT NULL,\n" +
                "    description   TEXT    NOT NULL,\n" +
                "    [group]       TEXT    NOT NULL,\n" +
                "    image         TEXT    NOT NULL,\n" +
                "    statID        INTEGER NOT NULL\n" +
                "                          REFERENCES Stat ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createRuneTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Rune ( \n" +
                "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                        NOT NULL,\n" +
                "    name        TEXT    NOT NULL,\n" +
                "    description TEXT    NOT NULL,\n" +
                "    tier        INTEGER NOT NULL,\n" +
                "    type        TEXT    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createSkinTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Skin ( \n" +
                "    id         INTEGER NOT NULL,\n" +
                "    name       TEXT    NOT NULL,\n" +
                "    image      TEXT    NOT NULL,\n" +
                "    championID INTEGER NOT NULL,\n" +
                "    FOREIGN KEY ( championID ) REFERENCES Champion ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createMasteryTreeTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE MasteryTree ( \n" +
                "    id   INTEGER NOT NULL,\n" +
                "    name TEXT    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createLeafTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE leaf ( \n" +
                "    id          INTEGER NOT NULL,\n" +
                "    ranks       INTEGER NOT NULL,\n" +
                "    prereq      INTEGER NOT NULL,\n" +
                "    treeID      INTEGER NOT NULL,\n" +
                "    name        TEXT    NOT NULL,\n" +
                "    description TEXT    NOT NULL,\n" +
                "    FOREIGN KEY ( treeID ) REFERENCES masterytree ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createChampion_SpellTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Champion_Spell ( \n" +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    NOT NULL,\n" +
                "    champID INTEGER NOT NULL,\n" +
                "    spellID INTEGER NOT NULL,\n" +
                "    FOREIGN KEY ( champID ) REFERENCES Champion ( id ),\n" +
                "    FOREIGN KEY ( spellID ) REFERENCES Spell ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createChampion_TagTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Champion_Tag ( \n" +
                "    id         INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                       NOT NULL,\n" +
                "    championID INTEGER NOT NULL,\n" +
                "    tagID      INTEGER NOT NULL,\n" +
                "    FOREIGN KEY ( championID ) REFERENCES Champion ( id ),\n" +
                "    FOREIGN KEY ( tagID ) REFERENCES Tag ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createChampion_StatTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Champion_Stat ( \n" +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    NOT NULL,\n" +
                "    champID INTEGER NOT NULL\n" +
                "                    REFERENCES Champion ( id ),\n" +
                "    statID  INTEGER NOT NULL\n" +
                "                    REFERENCES Stat ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createSpell_EffectTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Spell_Effect ( \n" +
                "    id       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     NOT NULL,\n" +
                "    spellID  INTEGER NOT NULL,\n" +
                "    effectID INTEGER NOT NULL,\n" +
                "    FOREIGN KEY ( spellID ) REFERENCES spell ( id ),\n" +
                "    FOREIGN KEY ( effectID ) REFERENCES effect ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createItem_StatTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item_Stat ( \n" +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL,\n" +
                "    itemID INTEGER NOT NULL,\n" +
                "    statID INTEGER NOT NULL\n" +
                "                   REFERENCES Stat ( id ),\n" +
                "    FOREIGN KEY ( itemID ) REFERENCES Item ( id ),\n" +
                "    FOREIGN KEY ( statID ) REFERENCES statitem ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createItem_TagTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item_Tag ( \n" +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL,\n" +
                "    itemID INTEGER NOT NULL\n" +
                "                   REFERENCES Item ( id ),\n" +
                "    tagID  INTEGER NOT NULL\n" +
                "                   REFERENCES Tag ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createItem_EffectTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item_Effect ( \n" +
                "    id       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     NOT NULL,\n" +
                "    itemID   INTEGER NOT NULL\n" +
                "                     REFERENCES Item ( id ),\n" +
                "    effectID INTEGER NOT NULL\n" +
                "                     REFERENCES Effect ( id ) \n" +
                ");";
        db.execSQL(sql);
    }

    private void createItem_UpgradeTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item_Upgrade ( \n" +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    itemID    INTEGER REFERENCES Item ( id ),\n" +
                "    otherItem INTEGER REFERENCES Item ( id ),\n" +
                "    required  BOOLEAN NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createRune_StatTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Rune_Stat ( \n" +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL,\n" +
                "    runeID INTEGER NOT NULL,\n" +
                "    statID INTEGER NOT NULL\n" +
                "                   REFERENCES Stat ( id ),\n" +
                "    FOREIGN KEY ( runeID ) REFERENCES Rune ( id ),\n" +
                "    FOREIGN KEY ( statID ) REFERENCES Stat ( id ) \n" +
                ");\n";
        db.execSQL(sql);
    }

    private void createMasteryTree_LeafTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE MasteryTree_Leaf ( \n" +
                "    id            INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    masteryTreeID INTEGER NOT NULL\n" +
                "                          REFERENCES masterytree ( id ),\n" +
                "    leafID        INTEGER NOT NULL\n" +
                "                          REFERENCES leaf ( id ) \n" +
                ");";
        db.execSQL(sql);
    }
}

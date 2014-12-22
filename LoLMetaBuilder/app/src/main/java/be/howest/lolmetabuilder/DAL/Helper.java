package be.howest.lolmetabuilder.DAL;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import be.howest.lolmetabuilder.data.models.*;
import be.howest.lolmetabuilder.json.api_ophalen;

/**
 * Created by Milan on 12/12/2014.
 */
public class Helper extends SQLiteOpenHelper {

    private static Helper INSTANCE;
    private static Object lock = new Object();

    private static final String DB_NAME = "loldb.db";
    private static final int DB_VERSION = 1;

    public static ApplicationInfo appInfo;

    private Helper(Context context, ApplicationInfo appInfo)
    {
        super(context, DB_NAME, null, DB_VERSION);
        this.appInfo = appInfo;
    }

    public static Helper getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (lock) {
                if(INSTANCE == null) {
                    INSTANCE = new Helper(context.getApplicationContext(), appInfo);
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

        //Tussentabellen
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

        //Tabellen die in tussentabellen gebruikt worden
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

        //Recreate all tables
        //Tabellen die in tussentabellen gebruikt worden eerst
        createTipTableV1(db);
        createEffectTableV1(db);
        createTagTableV1(db);
        createStatTableV1(db);

        //hoofdtabellen
        createChampionTableV1(db);
        createFreeChampTableV1(db);
        createItemTableV1(db);
        createRuneTableV1(db);
        createSkinTableV1(db);
        createSpellTableV1(db);
        createMasteryTreeTableV1(db);
        createLeafTableV1(db);

        //tussentabellen
        createChampion_SpellTableV1(db);
        createTag_ChampionTableV1(db);
        createTag_ItemTableV1(db);
        createStat_ChampionTableV1(db);
        createStat_ItemTableV1(db);
        createStat_RuneTableV1(db);
        createEffect_SpellTableV1(db);
        createEffect_ItemTableV1(db);
        createItem_UpgradeTableV1(db);
        createMasteryTree_LeafTableV1(db);

        //aangemaakte tabellen opvullen
        fillTablesV1(db);
    }
    private void fillTablesV1(SQLiteDatabase db) {
        //TODO: met foreach tabellen invullen

        ArrayList<FreeChamp> freeChamps = new ArrayList<FreeChamp>();
        ArrayList<Champion> champions = new ArrayList<Champion>();
        ArrayList<Item> items = new ArrayList<Item>();
        ArrayList<Leaf> leafs = new ArrayList<Leaf>();
        ArrayList<Rune> runes = new ArrayList<Rune>();
        ArrayList<MasteryTree> masteryTrees = new ArrayList<MasteryTree>();

        try {
            //Lijsten aanmaken en invullen via de API

            champions = api_ophalen.champions(appInfo);
            items = api_ophalen.items(appInfo);
            leafs = api_ophalen.leafs(appInfo);
            runes = api_ophalen.runes(appInfo);
            masteryTrees = api_ophalen.masteryTrees(appInfo);

            ArrayList<FreeChamp> temp = api_ophalen.freechampRotation(appInfo);

            if (champions != null) {
                for (Champion c : champions) {
                    for (FreeChamp fc : temp) {
                        if (c.getId() == fc.getId()) {
                            FreeChamp freeChamp = new FreeChamp(fc.getId());
                            freeChamp.setChampion(c);

                            freeChamps.add(freeChamp);
                        }
                    }
                }
            }

            //Freechamp
            for(FreeChamp fc : freeChamps) {
                db.rawQuery("INSERT INTO FreeChamp VALUES (?,?)", new String[] {"" + fc.getChampion().getId(), fc.getChampion().getName()});
            }

            //Champion, Stat_Champion, Tag_Champion, Spell_Champion, Tip_Champion
            for(Champion c : champions) {
                db.rawQuery("INSERT INTO Champions VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", new String[] {""+c.getId(), c.getName(), c.getTitle(), c.getLore(), ""+c.getAttack(), ""+c.getDefense(), ""+c.getMagic(), ""+c.getDifficulty(), c.getPassiveName(), c.getPassiveDesc(), ""+c.getPriceRP(), ""+c.getPriceIP()});

                //Stat_Champion, Stat
                for(Stat s : c.getStats()){
                    db.rawQuery("INSERT INTO Stat_Champion (champID, statID) VALUES (?,?)", new String[] {""+c.getId(), ""+s.getId()});
                    db.rawQuery("INSERT INTO Stat VALUES(?,?,?)", new String[]{""+s.getId(), s.getName(), ""+s.getValue()});
                }

                //Tag_Champion, Tag
                for(Tag t : c.getTags()) {
                    db.rawQuery("INSERT INTO Tag_Champion (champID, tagID)", new String[]{""+c.getId(), ""+t.getId()});
                    db.rawQuery("INSERT INTO Tag VALUES(?,?)", new String[]{""+t.getId(), t.getName()});
                }

                //Spell_Champion, Spell, Effect_Spell, Effect
                for(Spell s : c.getSpells()){
                    db.rawQuery("INSERT INTO Spell_Champion (champID, spellID)", new String[]{""+c.getId(), ""+s.getId()});
                    db.rawQuery("INSERT INTO Spell VALUES(?,?,?,?,?,?,?,?)", new String[]{""+s.getId(), s.getName(), s.getDescription(), s.getTooltip(), ""+s.getCost(), ""+s.getCooldown(), ""+s.getRange(), ""+s.getImage()});
                    for(Effect e : s.getEffects()) {
                        db.rawQuery("INSERT INTO Effect_Spell (effectID, spellID)", new String[]{""+e.getId(), ""+s.getId()});
                        db.rawQuery("INSERT INTO Effect VALUES(?,?,?)", new String[]{""+e.getId(), e.getName(), ""+e.getValue()});
                    }
                }

                for(Tip t : c.getAllyTips()){
                    db.rawQuery("INSERT INTO Tip_Champion (TipID, ChampID)", new String[]{""+t.getId(), ""+c.getId()});
                    db.rawQuery("INSERT INTO Tip VALUES(?,?,1)", new String[]{""+t.getId(), t.getContent()});
                }

                for(Tip t : c.getEnemyTips()){
                    db.rawQuery("INSERT INTO Tip_Champion (TipID, ChampID)", new String[]{""+t.getId(), ""+c.getId()});
                    db.rawQuery("INSERT INTO Tip VALUES(?,?,0)", new String[]{""+t.getId(), t.getContent()});
                }
            }



        } catch(Exception e) {
            //TODO: Error message die zegt dat je internet nodig hebt
            e.printStackTrace();
        }
    }
    private void createTipTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tip ( \n" +
                "    id      INTEGER PRIMARY KEY\n" +
                "                    NOT NULL,\n" +
                "    content TEXT    NOT NULL,\n" +
                "    isAlly  BOOLEAN NOT NULL,\n" +
                "    champID INTEGER NOT NULL,\n" +
                ");";
        db.execSQL(sql);
    }

    private void createEffectTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Effect ( \n" +
                "    id    INTEGER PRIMARY KEY\n" +
                "                  NOT NULL,\n" +
                "    name  TEXT    NOT NULL,\n" +
                "    value REAL    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createTagTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tag ( \n" +
                "    id   INTEGER PRIMARY KEY\n" +
                "                 NOT NULL,\n" +
                "    name TEXT    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createStatTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat ( \n" +
                "    id    INTEGER PRIMARY KEY\n" +
                "                  NOT NULL,\n" +
                "    name  TEXT    NOT NULL,\n" +
                "    value REAL    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createChampionTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Champion ( \n" +
                "    id          INTEGER PRIMARY KEY\n" +
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
                ");";
        db.execSQL(sql);
    }

    private void createSpellTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Spell ( \n" +
                "    id          INTEGER PRIMARY KEY\n" +
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
        String sql = "CREATE TABLE FreeChamp ( \n" +
                "    id   INTEGER PRIMARY KEY\n" +
                "                 NOT NULL,\n" +
                "    name TEXT    NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item ( \n" +
                "    id            INTEGER PRIMARY KEY\n" +
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
                ");";
        db.execSQL(sql);
    }

    private void createRuneTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Rune ( \n" +
                "    id          INTEGER PRIMARY KEY\n" +
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
                "    name        TEXT    NOT NULL,\n" +
                "    description TEXT    NOT NULL,\n" +
                ");";
        db.execSQL(sql);
    }

    private void createChampion_SpellTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Champion_Spell ( \n" +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    NOT NULL,\n" +
                "    champID INTEGER NOT NULL,\n" +
                "    spellID INTEGER NOT NULL,\n" +
                ");";
        db.execSQL(sql);
    }

    private void createTag_ChampionTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tag_Champion ( \n" +
                "    id         INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                       NOT NULL,\n" +
                "    championID INTEGER NOT NULL,\n" +
                "    tagID      INTEGER NOT NULL,\n" +
                ");";
        db.execSQL(sql);
    }

    private void createStat_ChampionTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat_Champion ( \n" +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    NOT NULL,\n" +
                "    champID INTEGER NOT NULL\n" +
                "    statID  INTEGER NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    private void createEffect_SpellTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Effect_Spell ( \n" +
                "    id       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     NOT NULL,\n" +
                "    spellID  INTEGER NOT NULL,\n" +
                "    effectID INTEGER NOT NULL,\n" +
                ");";
        db.execSQL(sql);
    }

    private void createStat_ItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat_Item ( \n" +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL,\n" +
                "    itemID INTEGER NOT NULL,\n" +
                "    statID INTEGER NOT NULL\n" +
                "                   REFERENCES Stat ( id ),\n" +
                ");";
        db.execSQL(sql);
    }

    private void createTag_ItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tag_Item ( \n" +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL,\n" +
                "    itemID INTEGER NOT NULL\n" +
                "    tagID  INTEGER NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    private void createEffect_ItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Effect_Item ( \n" +
                "    id       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     NOT NULL,\n" +
                "    itemID   INTEGER NOT NULL\n" +
                "    effectID INTEGER NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    private void createItem_UpgradeTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item_Upgrade ( \n" +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    itemID    INTEGER NOT NULL,\n" +
                "    otherItem INTEGER NOT NULL,\n" +
                "    required  BOOLEAN NOT NULL \n" +
                ");";
        db.execSQL(sql);
    }

    private void createStat_RuneTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat_Rune ( \n" +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                   NOT NULL,\n" +
                "    runeID INTEGER NOT NULL,\n" +
                "    statID INTEGER NOT NULL\n" +
                ");\n";
        db.execSQL(sql);
    }

    private void createMasteryTree_LeafTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE MasteryTree_Leaf ( \n" +
                "    id            INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    masteryTreeID INTEGER NOT NULL\n" +
                "    leafID        INTEGER NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }
}

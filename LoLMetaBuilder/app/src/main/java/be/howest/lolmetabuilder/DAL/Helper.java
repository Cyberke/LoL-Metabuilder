package be.howest.lolmetabuilder.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import be.howest.lolmetabuilder.data.models.*;
import android.util.Log;

//TODO: dit is de oude helper, sommige code valt te hergebruiken

/**
 * Created by Milan on 12/12/2014.
 */
public class Helper extends SQLiteOpenHelper {

    private static final String DB_NAME = "loldb";
    private static final int DB_VERSION = 1;

    //DEBUGGING
    private static final String TAG = "Debug:";

    public Helper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG,"constructor helper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate START!");
        //onUpgrade(db, 0, DB_VERSION);

        Log.d(TAG, "BEGIN CREATE TABLES");

        createTipTableV1(db);

        createEffectTableV1(db);
        createSpellEffectTableV1(db);
        createTagTableV1(db);
        createStatTableV1(db);
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
        createTag_RuneTableV1(db);
        createStat_ChampionTableV1(db);
        createStat_ItemTableV1(db);
        createStat_RuneTableV1(db);
        createSpellEffect_SpellTableV1(db);
        createEffect_ItemTableV1(db);
        createItem_UpgradeTableV1(db);
        createMasteryTree_LeafTableV1(db);
        createTip_ChampionTableV1(db);

        Log.d(TAG, "END CREATE TABLES");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop all tables
        //Tussentabellen
        Log.d(TAG, "BEGIN DROP TABLES");

        db.execSQL("DROP TABLE Champion_Spell");
        db.execSQL("DROP TABLE Champion_Tag");
        db.execSQL("DROP TABLE Champion_Stat");
        db.execSQL("DROP TABLE SpellEffect_Spell");
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
        db.execSQL("DROP TABLE SpellEffect");

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

        Log.d(TAG, "END DROP TABLES");

        onCreate(db);
    }

    //FreeChamp
    public ArrayList<FreeChamp> getFreeChamps() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FreeChamp> list = new ArrayList<FreeChamp>();

        Cursor Data = db.rawQuery("SELECT * FROM FreeChamp", new String[]{});
        if(Data.moveToFirst()){
            for(int i = 0; i < Data.getCount(); i++) {
                FreeChamp freechamp = new FreeChamp(Data.getInt(1));
                Cursor champs = db.rawQuery("SELECT * FROM Champion WHERE name=?", new String[]{Data.getString(0)});

                ArrayList<Tip> allyTips = new ArrayList<Tip>();
                ArrayList<Tip> enemyTips = new ArrayList<Tip>();
                ArrayList<Tag> tags = new ArrayList<Tag>();
                ArrayList<Stat> stats = new ArrayList<Stat>();
                ArrayList<Spell> spells = new ArrayList<Spell>();
                ArrayList<Skin> skins = new ArrayList<Skin>();

                //allytips
                Cursor tips = db.rawQuery("SELECT Tip.id, Tip.content FROM Tip, Tip_Champion WHERE Tip.id = Tip_Champion.TipID AND Tip_Champion.ChampID = ? AND Tip.isAlly = 1", new String[]{""+champs.getInt(0)});
                if(tips.moveToFirst()) {
                    for(int j = 0; j < tips.getCount(); j++) {
                        Tip t = new Tip(true, tips.getString(1));
                        t.setId(tips.getInt(0));
                        allyTips.add(t);
                        tips.moveToNext();
                    }
                }

                //enemyTips
                tips = db.rawQuery("SELECT Tip.id, Tip.content FROM Tip, Tip_Champion WHERE Tip.id = Tip_Champion.TipID AND Tip_Champion.ChampID = ? AND Tip.isAlly = 1", new String[]{""+champs.getInt(0)});
                if(tips.moveToFirst()) {
                    for(int k = 0; k < tips.getCount(); k++) {
                        Tip t = new Tip(false, tips.getString(1));
                        t.setId(tips.getInt(0));
                        enemyTips.add(t);
                        tips.moveToNext();
                    }
                }

                //Tags
                Cursor curstags = db.rawQuery("SELECT Tag.id, Tag.name From Tag, Tag_Champion WHERE Tag.id=Tag_Champion.TagID AND Tag_Champion.champID=?",new String[]{""+champs.getInt(0)});
                if(curstags.moveToFirst()){
                    for(int l = 0; l < curstags.getCount(); l++) {
                        Tag t = new Tag(curstags.getString(1));
                        t.setId(curstags.getInt(0));
                        tags.add(t);
                        curstags.moveToNext();
                    }
                }

                //Stats (id, name, value)
                Cursor cursstats = db.rawQuery("SELECT Stat.id, Stat.name, Stat.value FROM Stat, Stat_Champion WHERE Stat.id = Stat_Champion.statID AND Stat_Champion.champID=?", new String[]{""+champs.getInt(0)});
                if(cursstats.moveToFirst()){
                    for(int m = 0; m < cursstats.getCount(); m++) {
                        Stat s = new Stat(cursstats.getString(1), cursstats.getDouble(2));
                        s.setId(cursstats.getInt(0));
                        stats.add(s);
                        cursstats.moveToNext();
                    }
                }

                Cursor cursspells = db.rawQuery("SELECT Spell.id, Spell.name, Spell.description, Spell.tooltip, Spell.cooldown, Spell.range, Spell.image, spell.cost FROM Spell, Spell_Champion WHERE Spell.ID=Spell_Champion.SpellID AND Spell_Champion.ChampID=?", new String[]{""+champs.getInt(0)});
                if(cursspells.moveToFirst()){
                    for(int n = 0; n < cursspells.getCount(); n++){
                        Spell s = new Spell(cursspells.getString(1), cursspells.getString(2), cursspells.getString(3), cursspells.getString(4), cursspells.getString(5), cursspells.getString(6), cursspells.getString(7));
                        s.setId(cursspells.getInt(0));

                        Cursor spelleffects = db.rawQuery("SELECT Spelleffect.value FROM Spelleffect, Spelleffect_Spell WHERE Spelleffect.id=spelleffect_spell.spelleffectID AND Spell.id=?", new String[]{""+cursspells.getInt(0)});
                        ArrayList<String> effects = new ArrayList<String>();
                        if(spelleffects.moveToFirst()){
                            for(int o = 0; o < spelleffects.getCount(); o++){
                                String effect = spelleffects.getString(0);
                                effects.add(effect);
                                spelleffects.moveToNext();
                            }
                            s.setEffects(effects);
                        }
                        spells.add(s);
                        cursspells.moveToNext();
                    }
                }


                Champion c = new Champion(champs.getInt(0), champs.getString(1), champs.getString(2), champs.getString(3), champs.getInt(4), champs.getInt(5), champs.getInt(6), champs.getInt(7), champs.getString(8), champs.getString(9), champs.getString(10), allyTips, enemyTips, tags, stats, spells, skins);
                freechamp.setChampion(c);
                list.add(freechamp);

                Data.moveToNext();
            }
        }
        return list;
    }

    public ArrayList<Champion> getChampions(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Champion> list = new ArrayList<Champion>();

        Cursor champs = db.rawQuery("SELECT * FROM Champion", new String[]{});
        try {
            if (champs.moveToFirst()) {
                for (int i = 0; i < champs.getCount(); i++) {
                    ArrayList<Tip> allyTips = new ArrayList<Tip>();
                    ArrayList<Tip> enemyTips = new ArrayList<Tip>();
                    ArrayList<Tag> tags = new ArrayList<Tag>();
                    ArrayList<Stat> stats = new ArrayList<Stat>();
                    ArrayList<Spell> spells = new ArrayList<Spell>();
                    ArrayList<Skin> skins = new ArrayList<Skin>();

                    //allytips
                    Cursor tips = db.rawQuery("SELECT Tip.id, Tip.content FROM Tip, Tip_Champion WHERE Tip.id = Tip_Champion.TipID AND Tip_Champion.ChampID = ? AND Tip.isAlly = 1", new String[]{"" + champs.getInt(0)});
                    if (tips.moveToFirst()) {
                        for (int j = 0; j < tips.getCount(); j++) {
                            Tip t = new Tip(true, tips.getString(1));
                            t.setId(tips.getInt(0));
                            allyTips.add(t);
                            tips.moveToNext();
                        }
                    }

                    //enemyTips
                    tips = db.rawQuery("SELECT Tip.id, Tip.content FROM Tip, Tip_Champion WHERE Tip.id = Tip_Champion.TipID AND Tip_Champion.ChampID = ? AND Tip.isAlly = 1", new String[]{"" + champs.getInt(0)});
                    if (tips.moveToFirst()) {
                        for (int k = 0; k < tips.getCount(); k++) {
                            Tip t = new Tip(false, tips.getString(1));
                            t.setId(tips.getInt(0));
                            enemyTips.add(t);
                            tips.moveToNext();
                        }
                    }

                    //Tags
                    Cursor curstags = db.rawQuery("SELECT Tag.id, Tag.name From Tag, Tag_Champion WHERE Tag.id=Tag_Champion.TagID AND Tag_Champion.champID=?", new String[]{"" + champs.getInt(0)});
                    if (curstags.moveToFirst()) {
                        for (int l = 0; l < curstags.getCount(); l++) {
                            Tag t = new Tag(curstags.getString(1));
                            t.setId(curstags.getInt(0));
                            tags.add(t);
                            curstags.moveToNext();
                        }
                    }

                    //Stats (id, name, value)
                    Cursor cursstats = db.rawQuery("SELECT Stat.id, Stat.name, Stat.value FROM Stat, Stat_Champion WHERE Stat.id = Stat_Champion.statID AND Stat_Champion.champID=?", new String[]{"" + champs.getInt(0)});
                    if (cursstats.moveToFirst()) {
                        for (int m = 0; m < cursstats.getCount(); m++) {
                            Stat s = new Stat(cursstats.getString(1), cursstats.getDouble(2));
                            s.setId(cursstats.getInt(0));
                            stats.add(s);
                            cursstats.moveToNext();
                        }
                    }

                    Cursor cursspells = db.rawQuery("SELECT Spell.id, Spell.name, Spell.description, Spell.tooltip, Spell.cooldown, Spell.range, Spell.image, spell.cost FROM Spell, Spell_Champion WHERE Spell.ID=Spell_Champion.SpellID AND Spell_Champion.ChampID=?", new String[]{"" + champs.getInt(0)});
                    if (cursspells.moveToFirst()) {
                        for (int n = 0; n < cursspells.getCount(); n++) {
                            Spell s = new Spell(cursspells.getString(1), cursspells.getString(2), cursspells.getString(3), cursspells.getString(4), cursspells.getString(5), cursspells.getString(6), cursspells.getString(7));
                            s.setId(cursspells.getInt(0));

                            Cursor spelleffects = db.rawQuery("SELECT Spelleffect.value FROM Spelleffect, Spelleffect_Spell WHERE Spelleffect.id=spelleffect_spell.spelleffectID AND Spell.id=?", new String[]{"" + cursspells.getInt(0)});
                            ArrayList<String> effects = new ArrayList<String>();
                            if (spelleffects.moveToFirst()) {
                                for (int o = 0; o < spelleffects.getCount(); o++) {
                                    String effect = spelleffects.getString(0);
                                    effects.add(effect);
                                    spelleffects.moveToNext();
                                }
                                s.setEffects(effects);
                            }
                            spells.add(s);
                            cursspells.moveToNext();
                        }
                    }

                    Champion c = new Champion(champs.getInt(0), champs.getString(1), champs.getString(2), champs.getString(3), champs.getInt(4), champs.getInt(5), champs.getInt(6), champs.getInt(7), champs.getString(8), champs.getString(9), champs.getString(10), allyTips, enemyTips, tags, stats, spells, skins);
                    list.add(c);
                    champs.moveToNext();
                }
            }
        }catch(Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return list;
    }

    public void fillTablesV1(ArrayList<FreeChamp> fc, ArrayList<Champion> c, ArrayList<Item> i, ArrayList<Leaf> l, ArrayList<Rune> r, ArrayList<MasteryTree> m) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            //Freechamp
            for(FreeChamp fcs : fc) {
                db.rawQuery("INSERT INTO FreeChamp VALUES (?,?)", new String[] {"" + fcs.getChampion().getId(), fcs.getChampion().getName()});
                Log.d(TAG, "FreeChamp" + fcs.getChampion().getName() + "succes!");
            }

            //Champion, Stat, Stat_Champion, Tag, Tag_Champion, Spell, Spell_Champion, Tip, Tip_Champion
            for(Champion ch : c) {
                db.rawQuery("INSERT INTO Champion VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[] {""+ch.getId(), ch.getName(), ch.getTitle(), ch.getLore(), ""+ch.getAttack(), ""+ch.getDefense(), ""+ch.getMagic(), ""+ch.getDifficulty(), ch.getPassiveName(), ch.getPassiveDesc(), ""+ch.getImage(), ""+ch.getPriceRP(), ""+ch.getPriceIP()});

                //Stat_Champion, Stat
                for(Stat s : ch.getStats()){
                        db.rawQuery("INSERT INTO Stat_Champion (champID, statID) VALUES (?,?)", new String[] {""+ch.getId(), ""+s.getId()});
                        try {
                            db.rawQuery("INSERT INTO Stat VALUES(?,?,?)", new String[]{""+s.getId(), s.getName(), ""+s.getValue()});

                        } catch(Exception e) {
                            //Duplicate Entry
                            Log.d(TAG, e.getMessage());
                        }
                    }

                //Tag_Champion, Tag
                for(Tag t : ch.getTags()) {
                    db.rawQuery("INSERT INTO Tag_Champion (championID, tagID) VALUES(?,?)", new String[]{""+ch.getId(), ""+t.getId()});
                    try{
                        db.rawQuery("INSERT INTO Tag VALUES(?,?)", new String[]{""+t.getId(), t.getName()});

                    } catch(Exception e) {
                        //Duplicate entry
                        Log.d(TAG, e.getMessage());
                    }
                }

                //Spell_Champion, Spell, Effect_Spell, Effect
                for(Spell s : ch.getSpells()){
                    db.rawQuery("INSERT INTO Champion_Spell (champID, spellID) VALUES(?,?)", new String[]{""+ch.getId(), ""+s.getId()});
                    db.rawQuery("INSERT INTO Spell VALUES(?,?,?,?,?,?,?,?)", new String[]{""+s.getId(), s.getName(), s.getDescription(), s.getTooltip(), ""+s.getCost(), ""+s.getCooldown(), ""+s.getRange(), ""+s.getImage()});

                    for(String se : s.getEffects()) {

                        ContentValues values = new ContentValues();
                        values.put("value", se);
                        int spelleffectid = (int)db.insert("SpellEffect", null, values);

                        db.rawQuery("INSERT INTO SpellEffect_Spell (spelleffectID, spellID) VALUES(?,?)", new String[]{""+spelleffectid, ""+s.getId()});

                    }
                }

                for(Tip t : ch.getAllyTips()){
                    db.rawQuery("INSERT INTO Tip_Champion (TipID, ChampID) VALUES(?,?)", new String[]{""+t.getId(), ""+ch.getId()});
                    db.rawQuery("INSERT INTO Tip VALUES(?,?,1,?)", new String[]{""+t.getId(), t.getContent(), ""+ch.getId()});
                }

                for(Tip t : ch.getEnemyTips()){
                    db.rawQuery("INSERT INTO Tip_Champion (TipID, ChampID) VALUES(?,?)", new String[]{""+t.getId(), ""+ch.getId()});
                    db.rawQuery("INSERT INTO Tip VALUES(?,?,0,?)", new String[]{""+t.getId(), t.getContent(), ""+ch.getId()});
                }

                Log.d(TAG, ch.getName() + " COMPLETE");
            }

            //Item, Stat, Stat_Item, Tag, Tag_Item, Effect_Item, Item_Upgrade
            for(Item it : i){
                db.rawQuery("INSERT INTO Item VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[]{""+it.getId(), ""+it.getTotalGold(), ""+it.getBaseGold(), ""+it.getDepth(), ""+it.getSpecialRecipe(), ""+it.getMap(), ""+it.getStacks(), ""+it.isPurchasable(), ""+it.isConsumed(), ""+it.getName(), ""+it.getDescription(), ""+it.getGroup(), ""+it.getImage()});

                //Stat_Item, Stat
                for(Stat s : it.getStats()){
                    db.rawQuery("INSERT INTO Stat_Item (statID, itemID) VALUES(?,?)", new String[]{""+s.getId(), ""+it.getId()});
                    try{
                        db.rawQuery("INSERT INTO Stat VALUES(?,?,?)", new String[]{""+s.getId(), ""+s.getName(), ""+s.getValue()});

                    } catch(Exception e) {
                        //Duplicate Entry
                        Log.d(TAG, e.getMessage());
                    }
                }

                //Tag_Item, Tag
                for(Tag t : it.getTags()){
                    db.rawQuery("INSERT INTO Tag_Item (tagID, itemID) VALUES(?,?)", new String[]{""+t.getId(), ""+it.getId()});
                    try{
                        db.rawQuery("INSERT INTO Tag VALUES(?,?)", new String[]{""+t.getId(), t.getName()});

                    } catch(Exception e) {
                        //Duplicate Entry
                        Log.d(TAG, e.getMessage());
                    }
                }

                //Effect_Item, Effect
                for(Effect e : it.getEffects()){
                    db.rawQuery("INSERT INTO Effect_Item (effectID, itemID) VALUES(?,?)", new String[]{""+e.getId(), ""+it.getId()});
                    try {
                        db.rawQuery("INSERT INTO Effect VALUES(?,?,?)", new String[]{""+e.getId(), e.getName(), ""+e.getValue()});

                    } catch(Exception er) {
                        //Duplicate Entry
                        Log.d(TAG, er.getMessage());
                    }
                }

                //Item_Upgrade
                Log.d(TAG, "build into: " + it.getBuildIntos().size());
                for(Item u : it.getBuildIntos()){
                    db.rawQuery("INSERT INTO Item_Upgrade (itemID, otherItem, required) VALUES(?,?,0)", new String[]{""+it.getId(), ""+u.getId()});
                    Log.d(TAG, "Item upgrade succes!");
                }

                Log.d(TAG, "required: " + it.getRequires().size());
                for(Item u : it.getRequires()){
                    db.rawQuery("INSERT INTO Item_Upgrade (itemID, otherItem, required) VALUES(?,?,1)", new String[]{""+it.getId(), ""+u.getId()});
                    Log.d(TAG, "Item require succes!");
                }

                Log.d(TAG,it.getName() + " COMPLETE");
            }

            //Rune, Stat_Rune, Tag, Tag_Rune
            for(Rune ru : r){
                db.rawQuery("INSERT INTO Rune VALUES(?,?,?,?,?,?)", new String[]{""+ru.getId(), ru.getName(), ru.getDescription(), ""+ru.getTier(), ""+ru.getType(), ""+ru.getImage()});

                //Stat, Stat_Rune
                for(Stat s : ru.getStats()){
                    db.rawQuery("INSERT INTO Stat_Rune (runeID, statID) VALUES(?,?)", new String[]{""+ru.getId(), ""+s.getId()});
                    try{
                        db.rawQuery("INSERT INTO Stat VALUES(?,?,?)", new String[]{""+s.getId(), ""+s.getName(), ""+s.getValue()});

                    } catch(Exception e) {
                        //Duplicate Entry
                        Log.d(TAG, e.getMessage());
                    }
                }

                //Tag, Tag_Rune
                for(Tag t : ru.getTags()){
                    db.rawQuery("INSERT INTO Tag_Rune (runeID, tagID) VALUES (?,?)", new String[]{""+ru.getId(), ""+t.getId()});
                    try{
                        db.rawQuery("INSERT INTO Tag VALUES(?,?)", new String[]{""+t.getId(), t.getName()});

                    } catch(Exception e){
                        //Duplicate Entry
                        Log.d(TAG, e.getMessage());
                    }
                }

                Log.d(TAG,ru.getName() + " COMPLETE");
            }

        } catch(Exception e) {
            Log.d(TAG, e.getMessage());
        }

    }

    private void createTip_ChampionTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tip_Champion ( " +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                    NOT NULL," +
                "    champID INTEGER NOT NULL," +
                "    tipID   INTEGER NOT NULL" +
                ");";
        db.execSQL(sql);

        Log.d(TAG,"Tip_Champion OK");
    }

    private void createSpellEffectTableV1(SQLiteDatabase db) {

        String sql = "CREATE TABLE SpellEffect ( " +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                  NOT NULL," +
                "    value TEXT    NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"SpellEffect OK");
    }

    private void createTipTableV1(SQLiteDatabase db) {
        try {
            //SQLiteDatabase db = this.getWritableDatabase();
            String sql = "CREATE TABLE Tip ( " +
                    "id INTEGER PRIMARY KEY" +
                    " NOT NULL," +
                    " content TEXT NOT NULL," +
                    " isAlly BOOLEAN NOT NULL," +
                    " champID INTEGER NOT NULL" +
                    ")";
            db.execSQL(sql);
            Log.d(TAG, "Tip OK");
        } catch(Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    private void createEffectTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Effect ( " +
                "    id    INTEGER PRIMARY KEY" +
                "                  NOT NULL," +
                "    name  TEXT    NOT NULL," +
                "    value REAL    NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Effect OK");
    }

    private void createTagTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tag ( " +
                "    id   INTEGER PRIMARY KEY" +
                "                 NOT NULL," +
                "    name TEXT    NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Tag OK");
    }

    private void createStatTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat ( " +
                "    id    INTEGER PRIMARY KEY" +
                "                  NOT NULL," +
                "    name  TEXT    NOT NULL," +
                "    value REAL    NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Stat OK");
    }

    private void createChampionTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Champion ( " +
                "    id          INTEGER PRIMARY KEY" +
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
                "    image       TEXT    NOT NULL," +
                "    priceRP     INTEGER," +
                "    priceIP     INTEGER" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Champ OK");
    }

    private void createSpellTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Spell ( " +
                "    id          INTEGER PRIMARY KEY" +
                "                        NOT NULL," +
                "    name        TEXT    NOT NULL," +
                "    description TEXT    NOT NULL," +
                "    tooltip     TEXT    NOT NULL," +
                "    cost        TEXT    NOT NULL," +
                "    cooldown    TEXT    NOT NULL," +
                "    range       TEXT    NOT NULL," +
                "    image       TEXT    NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Spell OK");
    }

    private void createFreeChampTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE FreeChamp ( " +
                "    id   INTEGER PRIMARY KEY" +
                "                 NOT NULL," +
                "    name TEXT    NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"FreeChamp OK");
    }

    private void createItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item ( " +
                "    id            INTEGER PRIMARY KEY" +
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
                "    image         TEXT    NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Item OK");
    }

    private void createRuneTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Rune ( " +
                "    id          INTEGER PRIMARY KEY" +
                "                        NOT NULL," +
                "    name        TEXT    NOT NULL," +
                "    description TEXT    NOT NULL," +
                "    tier        INTEGER NOT NULL," +
                "    type        TEXT    NOT NULL," +
                "    image       TEXT    NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Rune OK");
    }

    private void createSkinTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Skin ( " +
                "    id         INTEGER NOT NULL," +
                "    name       TEXT    NOT NULL," +
                "    image      TEXT    NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Skin OK");
    }

    private void createMasteryTreeTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE MasteryTree ( " +
                "    id   INTEGER NOT NULL," +
                "    name TEXT    NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Mastery OK");
    }

    private void createLeafTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE leaf ( " +
                "    id          INTEGER NOT NULL," +
                "    ranks       INTEGER NOT NULL," +
                "    prereq      INTEGER NOT NULL," +
                "    name        TEXT    NOT NULL," +
                "    description TEXT    NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Leaf OK");
    }

    private void createChampion_SpellTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Champion_Spell ( " +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                    NOT NULL," +
                "    champID INTEGER NOT NULL," +
                "    spellID INTEGER NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Champ_Spell OK");
    }

    private void createTag_ChampionTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tag_Champion ( " +
                "    id         INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                       NOT NULL," +
                "    championID INTEGER NOT NULL," +
                "    tagID      INTEGER NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Tag_Champ OK");
    }

    private void createStat_ChampionTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat_Champion ( " +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                    NOT NULL," +
                "    champID INTEGER NOT NULL," +
                "    statID  INTEGER NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Stat_Champ OK");
    }

    private void createSpellEffect_SpellTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE SpellEffect_Spell ( " +
                "    id            INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                          NOT NULL," +
                "    spellID       INTEGER NOT NULL," +
                "    spelleffectID INTEGER NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"SpellEffect_Spell OK");
    }

    private void createStat_ItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat_Item ( " +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                   NOT NULL," +
                "    itemID INTEGER NOT NULL," +
                "    statID INTEGER NOT NULL" +
                "                   REFERENCES Stat ( id )" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Stat_Item OK");
    }

    private void createTag_ItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tag_Item ( " +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                   NOT NULL," +
                "    itemID INTEGER NOT NULL," +
                "    tagID  INTEGER NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Tag_Item OK");
    }

    private void createEffect_ItemTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Effect_Item ( " +
                "    id       INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                     NOT NULL," +
                "    itemID   INTEGER NOT NULL," +
                "    effectID INTEGER NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Effect_Item OK");
    }

    private void createItem_UpgradeTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Item_Upgrade ( " +
                "    id        INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    itemID    INTEGER NOT NULL," +
                "    otherItem INTEGER NOT NULL," +
                "    required  BOOLEAN NOT NULL " +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Item_Upgrade OK");
    }

    private void createStat_RuneTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Stat_Rune ( " +
                "    id     INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                   NOT NULL," +
                "    runeID INTEGER NOT NULL," +
                "    statID INTEGER NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Stat_Rune OK");
    }

    private void createTag_RuneTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE Tag_Rune ( " +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT" +
                "                    NOT NULL," +
                "    runeID INTEGER NOT NULL," +
                "    tagID INTEGER NOT NULL" +
                ");";
        db.execSQL(sql);
        Log.d(TAG,"Tag_Rune OK");
    }

    private void createMasteryTree_LeafTableV1(SQLiteDatabase db) {
        try {
            String sql = "CREATE TABLE MasteryTree_Leaf ( " +
                    "    id            INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "                          NOT NULL," +
                    "    masteryTreeID INTEGER NOT NULL," +
                    "    leafID        INTEGER NOT NULL" +
                    ");";
            db.execSQL(sql);
            Log.d(TAG, "Mastery_Leaf OK");
        }catch(Exception e){
            Log.d(TAG,e.getMessage());
        }
    }
}

package be.howest.lolmetabuilder.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import be.howest.lolmetabuilder.data.models.*;

/**
 * Created by Milan on 20/01/2015.
 */

public class champDB {
    private DbHelper helper;
    private SQLiteDatabase database;

    public champDB(Context context)
    {
        helper = new DbHelper(context);
    }

    public void createRecords(Champion champ)
    {
        database = helper.getWritableDatabase();
        long debugValue;
        ContentValues values = new ContentValues();

        values.put("id", champ.getId());
        values.put("name", champ.getName());
        values.put("title", champ.getTitle());
        values.put("lore", champ.getLore());
        values.put("attack", champ.getAttack());
        values.put("defense", champ.getDefense());
        values.put("magic", champ.getMagic());
        values.put("difficulty", champ.getDifficulty());
        values.put("passiveName", champ.getPassiveName());
        values.put("passiveDesc", champ.getPassiveDesc());
        values.put("image",champ.getImage());
        values.put("PriceRP", champ.getPriceRP());
        values.put("PriceIP", champ.getPriceIP());

        debugValue = database.insert("Champion", null, values);
        //Log.d("Debug:", "insert champ " + debugValue);

        for(Stat s : champ.getStats())
        {
            values = new ContentValues();
            values.put("champID", champ.getId());
            values.put("statID", s.getId());
            debugValue = database.insert("Stat_Champion", null, values);
            //Log.d("Debug:", "insert Stat_Champion "+debugValue);

            values = new ContentValues();
            values.put("id",s.getId());
            values.put("name",s.getName());
            values.put("value",s.getValue());
            debugValue = database.insert("Stat",null,values);
            //Log.d("Debug:", "insert Stat "+debugValue);
        }

        for(Tag t: champ.getTags())
        {
            values = new ContentValues();
            values.put("champID", champ.getId());
            values.put("tagID", t.getId());
            debugValue = database.insert("Tag_Champion", null, values);
            //Log.d("Debug:", "insert Tag_Champion "+debugValue);

            values = new ContentValues();
            values.put("id", t.getId());
            values.put("name",t.getName());
            debugValue = database.insert("Tag", null, values);
            //Log.d("Debug:", "insert Tag "+debugValue);
        }

        for(Spell s : champ.getSpells())
        {
            values = new ContentValues();
            values.put("champID", champ.getId());
            values.put("spellID", s.getId());
            debugValue = database.insert("Champion_Spell", null, values);
            //Log.d("Debug:", "insert Champion_Spell "+debugValue);

            values = new ContentValues();
            values.put("id", s.getId());
            values.put("name", s.getName());
            values.put("description", s.getDescription());
            values.put("tooltip", s.getTooltip());
            values.put("cost", s.getCost());
            values.put("cooldown", s.getCooldown());
            values.put("range", s.getRange());
            values.put("image", s.getImage());
            debugValue = database.insert("Spell", null, values);
            //Log.d("Debug:", "insert Spell "+debugValue);

            for(String se : s.getEffects())
            {
                values = new ContentValues();
                values.put("value", se);
                int spelleffectid = (int)database.insert("Spelleffect", null, values);
                //Log.d("Debug:", "insert spelleffect "+spelleffectid);

                values = new ContentValues();
                values.put("spelleffectID", spelleffectid);
                values.put("spellID", s.getId());
                debugValue = database.insert("SpellEffect_Spell", null, values);
                //Log.d("Debug:", "insert SpellEffect_Spell "+debugValue);
            }
        }

        for(Tip t : champ.getAllyTips())
        {
            values = new ContentValues();
            values.put("TipID", t.getId());
            values.put("ChampID", champ.getId());
            debugValue = database.insert("Tip_Champion", null, values);
            //Log.d("Debug:", "insert Tip_Champion "+debugValue);

            values = new ContentValues();
            values.put("id", t.getId());
            values.put("content", t.getContent());
            values.put("isAlly", t.isAlly());
            values.put("champID", t.getChampID());
            debugValue = database.insert("Tip", null, values);
            //Log.d("Debug:", "insert Tip "+debugValue);
        }

        for(Tip t : champ.getEnemyTips())
        {
            values = new ContentValues();
            values.put("TipID", t.getId());
            values.put("ChampID", champ.getId());
            debugValue = database.insert("Tip_Champion", null, values);
            //Log.d("Debug:", "insert Tip_Champion "+debugValue);

            values = new ContentValues();
            values.put("id", t.getId());
            values.put("content", t.getContent());
            values.put("isAlly", t.isAlly());
            values.put("champID", t.getChampID());
            debugValue = database.insert("Tip", null, values);
            //Log.d("Debug:", "insert Tip "+debugValue);
        }

        Log.d("Debug:", champ.getName() + " COMPLETE");
    }

    public ArrayList<Champion> getChampions() {
        database = helper.getReadableDatabase();
        ArrayList<Champion> list = new ArrayList<Champion>();

        Cursor champs = database.rawQuery("SELECT * FROM Champion", new String[]{});
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
                    Cursor tips = database.rawQuery("SELECT Tip.id, Tip.content FROM Tip, Tip_Champion WHERE Tip.id = Tip_Champion.TipID AND Tip_Champion.ChampID = ? AND Tip.isAlly = 1", new String[]{"" + champs.getInt(0)});
                    if (tips.moveToFirst()) {
                        for (int j = 0; j < tips.getCount(); j++) {
                            Tip t = new Tip(true, tips.getString(1));
                            t.setId(tips.getInt(0));
                            allyTips.add(t);
                            tips.moveToNext();
                        }
                        tips.close();
                    }

                    //enemyTips
                    tips = database.rawQuery("SELECT Tip.id, Tip.content FROM Tip, Tip_Champion WHERE Tip.id = Tip_Champion.TipID AND Tip_Champion.ChampID = ? AND Tip.isAlly = 1", new String[]{"" + champs.getInt(0)});
                    if (tips.moveToFirst()) {
                        for (int k = 0; k < tips.getCount(); k++) {
                            Tip t = new Tip(false, tips.getString(1));
                            t.setId(tips.getInt(0));
                            enemyTips.add(t);
                            tips.moveToNext();
                        }
                        tips.close();
                    }

                    //Tags
                    Cursor curstags = database.rawQuery("SELECT Tag.id, Tag.name From Tag, Tag_Champion WHERE Tag.id=Tag_Champion.TagID AND Tag_Champion.champID=?", new String[]{"" + champs.getInt(0)});
                    if (curstags.moveToFirst()) {
                        for (int l = 0; l < curstags.getCount(); l++) {
                            Tag t = new Tag(curstags.getString(1));
                            t.setId(curstags.getInt(0));
                            tags.add(t);
                            curstags.moveToNext();
                        }
                        curstags.close();
                    }

                    //Stats (id, name, value)
                    Cursor cursstats = database.rawQuery("SELECT Stat.id, Stat.name, Stat.value FROM Stat, Stat_Champion WHERE Stat.id = Stat_Champion.statID AND Stat_Champion.champID=?", new String[]{"" + champs.getInt(0)});
                    if (cursstats.moveToFirst()) {
                        for (int m = 0; m < cursstats.getCount(); m++) {
                            Stat s = new Stat(cursstats.getString(1), cursstats.getDouble(2));
                            s.setId(cursstats.getInt(0));
                            stats.add(s);
                            cursstats.moveToNext();
                        }
                        cursstats.close();
                    }

                    Cursor cursspells = database.rawQuery("SELECT Spell.id, Spell.name, Spell.description, Spell.tooltip, Spell.cooldown, Spell.range, Spell.image, spell.cost FROM Spell, Champion_Spell WHERE Spell.ID=Champion_Spell.SpellID AND Champion_Spell.ChampID=?", new String[]{"" + champs.getInt(0)});
                    if (cursspells.moveToFirst()) {
                        for (int n = 0; n < cursspells.getCount(); n++) {
                            Spell s = new Spell(cursspells.getString(1), cursspells.getString(2), cursspells.getString(3), cursspells.getString(4), cursspells.getString(5), cursspells.getString(6), cursspells.getString(7));
                            s.setId(cursspells.getInt(0));

                            Cursor spelleffects = database.rawQuery("SELECT Spelleffect.value FROM Spell, Spelleffect, Spelleffect_Spell WHERE Spelleffect.id=spelleffect_spell.spelleffectID AND Spell.id=?", new String[]{"" + cursspells.getInt(0)});
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
                            spelleffects.close();
                            cursspells.close();
                        }
                    }

                    Champion c = new Champion(champs.getInt(0), champs.getString(1), champs.getString(2), champs.getString(3), champs.getInt(4), champs.getInt(5), champs.getInt(6), champs.getInt(7), champs.getString(8), champs.getString(9), champs.getString(10), allyTips, enemyTips, tags, stats, spells, skins);
                    list.add(c);
                    Log.d("Debug:", c.getName());
                    champs.moveToNext();
                }
                champs.close();
            }
        }catch(Exception e) {
            Log.d("Debug:", e.getMessage());
        }
        return list;
    }
}

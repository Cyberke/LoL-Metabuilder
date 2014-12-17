package be.howest.lolmetabuilder.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jelle on 25/11/2014.
 * Edited by manuel on 28/11/2014.
 */
public class Champion {
    private int id = 0,
        attack = 0,
        defense = 0,
        magic = 0,
        difficulty = 0,
        statID = 0,
        priceIP = 0,
        priceRP = 0;
    private String name = "",
        title = "",
        lore = "",
        passiveName = "",
        passiveDesc = "",
        image = "";
    private ArrayList<Tip> allyTips = new ArrayList<Tip>();
    private ArrayList<Tip> enemyTips = new ArrayList<Tip>();
    private ArrayList<Tag> tags = new ArrayList<Tag>();
    private ArrayList<Stat> stats = new ArrayList<Stat>();
    private ArrayList<Spell> spells = new ArrayList<Spell>();
    private ArrayList<Skin> skins = new ArrayList<Skin>();

    public Champion(
            int id, String name, String title,
            String lore, int attack, int defense,
            int magic, int difficulty, String passiveName,
            String passiveDesc, String image, ArrayList<Tip> allyTips,
            ArrayList<Tip> enemyTips, ArrayList<Tag> tags,
            ArrayList<Stat> stats, ArrayList<Spell> spells,
            ArrayList<Skin> skins) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.lore = lore;
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
        this.difficulty = difficulty;
        this.passiveName = passiveName;
        this.passiveDesc = passiveDesc;
        this.image = image;
        this.allyTips = allyTips;
        this.enemyTips = enemyTips;
        this.tags = tags;
        this.stats = stats;
        this.spells = spells;
        this.skins = skins;
    }

    public ArrayList<Skin> getSkins() {
        return skins;
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }

    public ArrayList<Tip> getAllyTips() {
        return allyTips;
    }

    public ArrayList<Tip> getEnemyTips() {
        return enemyTips;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    public String getImage() {
        return image;
    }

    public void setStatID(int statID) {
        this.statID = statID;
    }

    public int getPriceIP() {
        return priceIP;
    }

    public void setPriceIP(int priceIP) {
        this.priceIP = priceIP;
    }

    public int getPriceRP() {
        return priceRP;
    }

    public void setPriceRP(int priceRP) {
        this.priceRP = priceRP;
    }

    public int getId() {
        return id;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getMagic() {
        return magic;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getStatID() {
        return statID;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getLore() {
        return lore;
    }

    public String getPassiveName() {
        return passiveName;
    }

    public String getPassiveDesc() {
        return passiveDesc;
    }
}

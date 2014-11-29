package be.howest.lolmetabuilder.data;

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
        passiveDesc = "";

    public Champion(
            int id, String name, String title,
            String lore, int attack, int defense,
            int magic, int difficulty, int statID,
            String passiveName, String passiveDesc) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.lore = lore;
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
        this.difficulty = difficulty;
        this.statID = statID;
        this.passiveName = passiveName;
        this.passiveDesc = passiveDesc;
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

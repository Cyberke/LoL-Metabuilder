package be.howest.lolmetabuilder.data.models;

/**
 * Created by manuel on 11/28/14.
 */
public class FreeChamp {
    private int id = 0;
    private Champion champion = null;

    public FreeChamp(int id) {
        this.id = id;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    public int getId() {
        return id;
    }

    public Champion getChampion() {
        return champion;
    }
}

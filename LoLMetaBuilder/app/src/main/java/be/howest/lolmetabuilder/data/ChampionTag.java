package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 12/3/14.
 */
public class ChampionTag {
    private int id = 0;
    private String name = "";

    public ChampionTag(
            int id, String name
    ) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

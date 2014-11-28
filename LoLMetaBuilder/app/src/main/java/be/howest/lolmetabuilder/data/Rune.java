package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class Rune {
    private int id = 0,
        tier = 0;
    private String name = "",
        description = "",
        type = "";

    public Rune(
            int id, String name, String description,
            int tier, String type
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getTier() {
        return tier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}

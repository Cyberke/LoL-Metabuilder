package be.howest.lolmetabuilder.data.models;

import java.util.ArrayList;

/**
 * Created by manuel on 11/28/14.
 */
public class Rune {
    private int id = 0,
        tier = 0;
    private String name = "",
        description = "",
        type = "",
        image = "";
    private ArrayList<Tag> tags = new ArrayList<Tag>();
    private ArrayList<Stat> stats = new ArrayList<Stat>();

    public Rune(
            int id, String name, String description,
            int tier, String type, String image
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.type = type;
        this.image = image;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    public void setStats(ArrayList<Stat> stats) {
        this.stats = stats;
    }

    public String getImage() {
        return image;
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

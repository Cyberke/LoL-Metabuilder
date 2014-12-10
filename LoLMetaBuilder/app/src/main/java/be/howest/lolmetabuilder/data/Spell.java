package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class Spell {
    private int id = 0;
    private String name = "",
        description = "",
        tooltip = "",
        cooldown = "",
        range = "",
        image = "";

    public Spell(String name, String description,
            String tooltip, String cooldown, String range,
            String image
    ) {
        this.name = name;
        this.description = description;
        this.tooltip = tooltip;
        this.cooldown = cooldown;
        this.range = range;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getCooldown() {
        return cooldown;
    }

    public String getRange() {
        return range;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTooltip() {
        return tooltip;
    }
}

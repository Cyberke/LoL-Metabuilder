package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class ItemTag {
    private int id = 0;
    private String name = "";

    public ItemTag(String name) {
        this.name = name;
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
}

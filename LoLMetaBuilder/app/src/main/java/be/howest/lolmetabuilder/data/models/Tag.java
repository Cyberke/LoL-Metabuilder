package be.howest.lolmetabuilder.data.models;

/**
 * Created by manuel on 12/3/14.
 */
public class Tag {
    private int id = 0;
    private String name = "";

    public Tag(String name) {
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

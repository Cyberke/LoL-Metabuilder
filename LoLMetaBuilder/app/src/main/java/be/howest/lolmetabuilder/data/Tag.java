package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class Tag {
    private int id = 0;
    private String name = "";

    public Tag(
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

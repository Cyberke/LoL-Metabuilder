package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class MasteryTree {
    private int id = 0;
    private String name = "";

    public MasteryTree(int id, String name) {
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

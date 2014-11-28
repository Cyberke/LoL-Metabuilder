package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class FreeChamp {
    private int id = 0;
    private String name = "";

    public FreeChamp(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
}

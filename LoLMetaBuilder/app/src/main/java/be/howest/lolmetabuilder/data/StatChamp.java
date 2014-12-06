package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class StatChamp {
    private int id = 0;
    private String name = "";
    private double value = 0.0;

    public StatChamp(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}

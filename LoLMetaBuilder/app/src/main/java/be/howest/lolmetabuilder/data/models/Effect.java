package be.howest.lolmetabuilder.data.models;

/**
 * Created by manuel on 11/28/14.
 */
public class Effect {
    private int id = 0;
    private String name = "";
    private double value = 0.0;

    public Effect(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }
}

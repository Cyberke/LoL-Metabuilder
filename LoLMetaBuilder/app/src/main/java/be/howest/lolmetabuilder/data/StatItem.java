package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class StatItem {
    private int id = 0;
    private String name = "";
    private double value = 0.0;

    public StatItem(String name, double value) {
        this.name = name;
        this.value = value;
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

    public double getValue() {
        return value;
    }
}

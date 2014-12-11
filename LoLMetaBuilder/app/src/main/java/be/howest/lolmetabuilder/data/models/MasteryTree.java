package be.howest.lolmetabuilder.data.models;

import java.util.ArrayList;

/**
 * Created by manuel on 11/28/14.
 */
public class MasteryTree {
    private int id = 0;
    private String name = "";
    private ArrayList<Integer> masteryItemIds = new ArrayList<Integer>();
    private ArrayList<Leaf> masteries = new ArrayList<Leaf>();

    public MasteryTree(String name, ArrayList<Integer> masteryItemIds) {
        this.name = name;
        this.masteryItemIds = masteryItemIds;
    }

    public ArrayList<Integer> getMasteryItemIds() {
        return masteryItemIds;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Leaf> getMasteries() {
        return masteries;
    }

    public void setMasteries(ArrayList<Leaf> masteries) {
        this.masteries = masteries;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

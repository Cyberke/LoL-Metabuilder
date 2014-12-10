package be.howest.lolmetabuilder.data.models;

import java.util.ArrayList;

/**
 * Created by manuel on 11/28/14.
 */
public class Leaf {
    private int id = 0,
        ranks = 0,
        prereq = 0,
        treeID = 0;
    private String name = "";
    private ArrayList<Description> descriptions = new ArrayList<Description>();

    public Leaf(
            int id, String name, ArrayList<Description> descriptions,
            int ranks, int prereq
    ) {
        this.id = id;
        this.name = name;
        this.descriptions = descriptions;
        this.ranks = ranks;
        this.prereq = prereq;
    }

    public void setTreeID(int treeID) {
        this.treeID = treeID;
    }

    public int getId() {
        return id;
    }

    public int getRanks() {
        return ranks;
    }

    public int getPrereq() {
        return prereq;
    }

    public int getTreeID() {
        return treeID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Description> getDescriptions() {
        return descriptions;
    }
}

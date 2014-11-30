package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class Leaf {
    private int id = 0,
        ranks = 0,
        prereq = 0,
        treeID = 0;
    private String name = "",
        description = "";

    public Leaf(
            int id, String name, String description,
            int ranks, int prereq
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}

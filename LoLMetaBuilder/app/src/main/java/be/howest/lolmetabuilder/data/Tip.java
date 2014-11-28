package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class Tip {
    private int id = 0,
        champID = 0;
    private boolean isAlly = true;
    private String content = "";

    public Tip(
            int id, int champID, boolean isAlly,
            String content
    ) {
        this.id = id;
        this.champID = champID;
        this.isAlly = isAlly;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public int getChampID() {
        return champID;
    }

    public boolean isAlly() {
        return isAlly;
    }

    public String getContent() {
        return content;
    }
}

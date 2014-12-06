package be.howest.lolmetabuilder.data;

/**
 * Created by manuel on 11/28/14.
 */
public class Tip {
    private int id = 0,
        champID = 0;
    private boolean isAlly = true;
    private String content = "";

    public Tip(boolean isAlly, String content) {
        this.isAlly = isAlly;
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChampID(int champID) {
        this.champID = champID;
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

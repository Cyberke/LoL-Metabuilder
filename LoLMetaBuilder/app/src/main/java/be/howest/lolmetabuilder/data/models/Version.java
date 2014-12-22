package be.howest.lolmetabuilder.data.models;

/**
 * Created by manuel on 12/22/14.
 */
public class Version {
    private String name = "", value = "";

    public Version(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}

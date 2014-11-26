package be.howest.lolmetabuilder.data;

/**
 * Created by jelle on 25/11/2014.
 */
public class Champion {
    private String name = "";
    private String image = "";
    private int price = 0;

    public Champion(String name, String image, int price)
    {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public String getName(){return name;}
    public String getImage(){return image;}
    public int getPrice(){return price;}
}

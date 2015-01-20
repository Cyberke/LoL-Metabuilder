package be.howest.lolmetabuilder.data.models;

import java.util.ArrayList;

/**
 * Created by Milan on 20/01/2015.
 */
public class buildResult {
    private ArrayList<Item> items = new ArrayList<Item>();
    private double totalATK = 0;
    private double totalAP = 0;
    private double totalARM = 0;
    private double totalMR = 0;
    private double totalLS = 0;
    private double totalSV = 0;
    private int totalGold = 0;

    public ArrayList<Item> getItems() {return items;}
    public double getTotalATK() {return totalATK;}
    public double getTotalAP() {return totalAP;}
    public double getTotalARM() {return totalARM;}
    public double getTotalMR() {return totalMR;}
    public double getTotalLS() {return totalLS;}
    public double getTotalSV() {return totalSV;}
    public int getTotalGold() {return totalGold;}

    public void setItems(ArrayList<Item> value) {items = value;}
    public void setTotalATK(double value) {totalATK = value;}
    public void setTotalAP(double value) {totalAP = value;}
    public void setTotalARM(double value) {totalARM = value;}
    public void setTotalMR(double value) {totalMR = value;}
    public void setTotalLS(double value) {totalLS = value;}
    public void setTotalSV(double value) {totalSV = value;}
    public void setTotalGold(int value) {totalGold = value;}

    public static buildResult setBuildResult(ArrayList<Item> itemlist)
    {
        buildResult temp = new buildResult();
        temp.setItems(itemlist);
        for(Item i : itemlist)
        {
            temp.setTotalGold(temp.getTotalGold() + i.getTotalGold());
            for(Stat s : i.getStats())
            {
                if(s.getName().equals("FlatPhysicalDamageMod"))
                    temp.setTotalATK(temp.getTotalATK() + s.getValue());
                else if(s.getName().equals("FlatMagicDamageMod"))
                    temp.setTotalAP(temp.getTotalAP() + s.getValue());
                else if(s.getName().equals("FlatArmorMod"))
                    temp.setTotalARM(temp.getTotalARM() + s.getValue());
                else if(s.getName().equals("FlatSpellBlockMod"))
                    temp.setTotalMR(temp.getTotalMR() + s.getValue());
                else if(s.getName().equals("PercentLifeStealMod"))
                    temp.setTotalLS(temp.getTotalLS() + s.getValue());
                else if(s.getName().equals("PercentSpellVampMod"))
                    temp.setTotalSV(temp.getTotalSV() + s.getValue());
            }
        }

        return temp;
    }
}

package be.howest.lolmetabuilder.data.models;

import java.util.ArrayList;

/**
 * Created by jelle on 22/12/2014.
 */
public class Build {
    private Champion champion;
    private Item[] items;
    private int limitGold;
    private ArrayList<String> prioriteit = new ArrayList<String>();

    public Build(Champion champion){
        this.champion = champion;
        items = new Item[6];
    }

    public Champion getChampion() {
        return champion;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public void setItemAt(Item item, int position) {this.items[position] = item;}

    public int getLimitGold() {
        return limitGold;
    }

    public void setLimitGold(int limitGold) {
        this.limitGold = limitGold;
    }

    public ArrayList<String> getPrioriteit() {
        return prioriteit;
    }

    public void setPrioriteit(ArrayList<String> prioriteit) {
        this.prioriteit = prioriteit;
    }

    public void addPrioriteit(String value) { this.prioriteit.add(value);}

    public void removePrioriteit(String value) {this.prioriteit.remove(value);}
}

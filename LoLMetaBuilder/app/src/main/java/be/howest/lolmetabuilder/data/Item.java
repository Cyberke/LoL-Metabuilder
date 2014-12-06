package be.howest.lolmetabuilder.data;

import java.util.ArrayList;

/**
 * Created by manuel on 11/28/14.
 */
public class Item {
    private int id = 0,
        totalGold = 0,
        baseGold = 0,
        depth = 0,
        specialRecipe = 0,
        map = 10,
        stacks = 0;
    private boolean purchasable = true,
        consumed = false;
    private String name = "",
        description = "",
        group = "";
    private ArrayList<StatItem> stats = new ArrayList<StatItem>();
    private ArrayList<ItemTag> tags = new ArrayList<ItemTag>();
    private ArrayList<Effect> effects = new ArrayList<Effect>();

    public Item(
            int id, int totalGold, int baseGold,
            boolean purchasable, boolean consumed,
            int depth, int specialRecipe, int map,
            String name, String description, String group,
            int stacks
    ) {
        this.id = id;
        this.totalGold = totalGold;
        this.baseGold = baseGold;
        this.purchasable = purchasable;
        this.consumed = consumed;
        this.depth = depth;
        this.specialRecipe = specialRecipe;
        this.map = map;
        this.name = name;
        this.description = description;
        this.group = group;
        this.stacks = stacks;
    }

    public void setStats(ArrayList<StatItem> stats) {
        this.stats = stats;
    }

    public void setTags(ArrayList<ItemTag> tags) {
        this.tags = tags;
    }

    public void setEffects(ArrayList<Effect> effects) {
        this.effects = effects;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public ArrayList<ItemTag> getTags() {
        return tags;
    }

    public ArrayList<StatItem> getStats() {
        return stats;
    }

    public int getStacks() {
        return stacks;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public int getBaseGold() {
        return baseGold;
    }

    public int getDepth() {
        return depth;
    }

    public int getSpecialRecipe() {
        return specialRecipe;
    }

    public int getMap() {
        return map;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public boolean isConsumed() {
        return consumed;
    }
}

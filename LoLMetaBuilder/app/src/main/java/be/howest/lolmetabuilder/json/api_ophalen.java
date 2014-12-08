package be.howest.lolmetabuilder.json;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import be.howest.lolmetabuilder.data.Champion;
import be.howest.lolmetabuilder.data.ChampionTag;
import be.howest.lolmetabuilder.data.Effect;
import be.howest.lolmetabuilder.data.FreeChamp;
import be.howest.lolmetabuilder.data.Item;
import be.howest.lolmetabuilder.data.ItemTag;
import be.howest.lolmetabuilder.data.Leaf;
import be.howest.lolmetabuilder.data.MasteryTree;
import be.howest.lolmetabuilder.data.Rune;
import be.howest.lolmetabuilder.data.Spell;
import be.howest.lolmetabuilder.data.StatChamp;
import be.howest.lolmetabuilder.data.StatItem;
import be.howest.lolmetabuilder.data.Tip;

/**
 * Created by manuel on 11/19/14.
 */
public class api_ophalen {
    public api_ophalen() {}

    public static ArrayList<FreeChamp> freechampRotation(ApplicationInfo appInfo) {
        ArrayList<FreeChamp> champRotation = new ArrayList<FreeChamp>();

        try {
            Bundle bundle = appInfo.metaData;

            String webservice = bundle.getString("webservice.freeRotation");
            String apiKey = bundle.getString("auth.client_secret");

            InputStream input = new URL(webservice + apiKey).openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
            reader.beginObject();

            int champID = 0;

            reader.nextName(); // skip --> champions

            reader.beginArray();

            while (reader.hasNext()) {
                reader.beginObject();

                while (reader.hasNext()) {
                    String key = reader.nextName();

                    if (key.equals("id")) {
                        champID = reader.nextInt();

                        FreeChamp freeChamp = new FreeChamp(champID);
                        champRotation.add(freeChamp);
                    }
                    else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
            }

            reader.endArray();
            reader.endObject();
            reader.close();
            input.close();

            return champRotation;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Champion> champions(ApplicationInfo appInfo) {
        ArrayList<Champion> champions = null;

        try {
            Bundle bundle = appInfo.metaData;

            String webservice = bundle.getString("webservice.static-data");
            String apiKey = bundle.getString("auth.client_secret");
            String params = "champion?locale=en_US&dataById=false&champData=all";

            InputStream input = new URL(webservice + params + apiKey).openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
            reader.beginObject(); // {

            String championName = reader.nextName(),
                    title = "", lore = "", passiveName = "", passiveDesc = "", image = "";
            int id = 0, attack = 0, defence = 0, magic = 0, difficulty = 0;
            ArrayList<Tip> allyTips = new ArrayList<Tip>();
            ArrayList<Tip> enemyTips = new ArrayList<Tip>();
            ArrayList<ChampionTag> championTags = new ArrayList<ChampionTag>();
            ArrayList<StatChamp> statChamps = new ArrayList<StatChamp>();
            ArrayList<Spell> championSpells = new ArrayList<Spell>();

            while (!championName.equals("data")) {
                reader.skipValue();

                championName = reader.nextName();
            }

            if (championName.equals("data")) { // data
                champions = new ArrayList<Champion>();
                reader.beginObject(); // {

                while (reader.hasNext()) {
                    reader.nextName(); // Thresh
                    reader.beginObject(); // {

                    while (reader.hasNext()) {
                        String key = reader.nextName();

                        if (key.equals("id")) {
                            id = reader.nextInt();
                        } else if (key.equals("name")) {
                            championName = reader.nextString();
                        } else if (key.equals("title")) {
                            title = reader.nextString();
                        }
                        else if (key.equals("image")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                if (key.equals("full")) {
                                    // .png verwijderen voor de drawables
                                    String[] parts = reader.nextString().split("\\.");
                                    image = parts[0];
                                }
                                else {
                                    reader.skipValue();
                                }
                            }

                            reader.endObject(); // }
                        }
                        else if (key.equals("lore")) {
                            lore = reader.nextString();
                        }
                        else if (key.equals("allytips")) {
                            reader.beginArray(); // [

                            while (reader.hasNext()) {
                                Tip allyTip = new Tip(true, reader.nextString());

                                allyTips.add(allyTip);
                            }

                            reader.endArray(); // ]
                        }
                        else if (key.equals("enemytips")) {
                            reader.beginArray(); // [

                            while (reader.hasNext()) {
                                Tip enemyTip = new Tip(false, reader.nextString());

                                enemyTips.add(enemyTip);
                            }

                            reader.endArray(); // ]
                        }
                        else if (key.equals("tags")) {
                            reader.beginArray(); // [

                            while (reader.hasNext()) {
                                ChampionTag tag = new ChampionTag(reader.nextString());

                                championTags.add(tag);
                            }

                            reader.endArray(); // ]
                        }
                        else if (key.equals("info")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                if (key.equals("attack")) {
                                    attack = reader.nextInt();
                                } else if (key.equals("defense")) {
                                    defence = reader.nextInt();
                                } else if (key.equals("magic")) {
                                    magic = reader.nextInt();
                                } else if (key.equals("difficulty")) {
                                    difficulty = reader.nextInt();
                                } else {
                                    reader.skipValue();
                                }
                            }

                            reader.endObject(); // }
                        }
                        else if (key.equals("stats")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                StatChamp stat = new StatChamp(key, reader.nextDouble());

                                statChamps.add(stat);
                            }

                            reader.endObject(); // }
                        }
                        else if (key.equals("spells")) {
                            reader.beginArray(); // [

                            String spellName = "", spellDescription = "",
                            spellTooltip = "";

                            while (reader.hasNext()) {
                                reader.beginObject(); // {

                                while (reader.hasNext()) {
                                    key = reader.nextName();

                                    if (key.equals("name")) {
                                        spellName = reader.nextString();
                                    }
                                    else if (key.equals("sanitizedDescription")) {
                                        spellDescription = reader.nextString();
                                    }
                                    else if (key.equals("sanitizedTooltip")) {
                                        spellTooltip = reader.nextString();
                                    }
                                    else {
                                        reader.skipValue();
                                    }
                                }

                                Spell spell = new Spell(spellName, spellDescription, spellTooltip);

                                championSpells.add(spell);

                                reader.endObject(); // }
                            }

                            reader.endArray(); // ]
                        }
                        else if (key.equals("passive")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                if (key.equals("name")) {
                                    passiveName = reader.nextString();
                                } else if (key.equals("sanitizedDescription")) {
                                    passiveDesc = reader.nextString();
                                } else {
                                    reader.skipValue();
                                }
                            }

                            reader.endObject(); // }
                        } else {
                            reader.skipValue();
                        }
                    }

                    Champion champion = new Champion(id, championName, title,
                            lore, attack, defence, magic, difficulty, passiveName,
                            passiveDesc, image, allyTips, enemyTips, championTags,
                            statChamps, championSpells);

                    champions.add(champion);

                    allyTips = new ArrayList<Tip>();
                    enemyTips = new ArrayList<Tip>();
                    championTags = new ArrayList<ChampionTag>();
                    statChamps = new ArrayList<StatChamp>();
                    championSpells = new ArrayList<Spell>();

                    reader.endObject(); // }
                }

                reader.endObject(); // }
            }

            reader.endObject(); // }
            reader.close();
            input.close();

            return champions;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return champions;
    }

    public static ArrayList<Item> items(ApplicationInfo appInfo) {
        ArrayList<Item> items = null;

        try {
            Bundle bundle = appInfo.metaData;

            String webservice = bundle.getString("webservice.static-data");
            String apiKey = bundle.getString("auth.client_secret");
            String params = "item?locale=en_US&itemListData=all";

            InputStream input = new URL(webservice + params + apiKey).openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
            reader.beginObject(); // {

            String itemName = reader.nextName(), description = "", group = "";
            int id = 0, totalGold = 0, baseGold = 0, depth = 0,
                    specialRecipe = 0, map = 10, stacks = 0;
            boolean purchasable = true, consumed = false;
            ArrayList<StatItem> statItems = new ArrayList<StatItem>();
            ArrayList<ItemTag> itemTags = new ArrayList<ItemTag>();
            ArrayList<Effect> effects = new ArrayList<Effect>();

            while (!itemName.equals("data")) {
                reader.skipValue();

                itemName = reader.nextName();
            }

            if (itemName.equals("data")) {
                items = new ArrayList<Item>();
                reader.beginObject(); // {

                while (reader.hasNext()) {
                    reader.nextName(); // i1001
                    reader.beginObject(); // {

                    while (reader.hasNext()) {
                        String key = reader.nextName();

                        if (key.equals("id")) {
                            id = reader.nextInt();
                        }
                        else if (key.equals("name")) {
                            itemName = reader.nextString();
                        }
                        else if (key.equals("gold")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                if (key.equals("base")) {
                                    baseGold = reader.nextInt();
                                } else if (key.equals("total")) {
                                    totalGold = reader.nextInt();
                                }
                                else if (key.equals("purchasable")) {
                                    purchasable = reader.nextBoolean();
                                }
                                else {
                                    reader.skipValue();
                                }
                            }

                            reader.endObject(); // }
                        }
                        else if (key.equals("group")) {
                            group = reader.nextString();
                        }
                        else if (key.equals("sanitizedDescription")) {
                            description = reader.nextString();
                        }
                        else if (key.equals("depth")) {
                            depth = reader.nextInt();
                        }
                        else if (key.equals("consumed")) {
                            consumed = true;

                            reader.skipValue();
                        }
                        else if (key.equals("stacks")) {
                            stacks = reader.nextInt();
                        }
                        else if (key.equals("maps")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                if (key.equals("10")) {
                                    map = -1;
                                }
                                else {
                                    map = 10;
                                }

                                reader.skipValue();
                            }

                            reader.endObject(); // }
                        }
                        else if (key.equals("tags")) {
                            reader.beginArray(); // [

                            while (reader.hasNext()) {
                                ItemTag itemTag = new ItemTag(reader.nextString());

                                itemTags.add(itemTag);
                            }

                            reader.endArray(); // ]
                        }
                        else if (key.equals("stats")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                StatItem statItem = new StatItem(key, reader.nextDouble());

                                statItems.add(statItem);
                            }

                            reader.endObject(); // }
                        }
                        else if (key.equals("effect")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                Effect effect = new Effect(key, Double.parseDouble(reader.nextString()));

                                effects.add(effect);
                            }

                            reader.endObject(); // }
                        }
                        else {
                            reader.skipValue();
                        }
                    }

                    if (map != -1) {
                        Item item = new Item(id, totalGold, baseGold, purchasable, consumed, depth,
                                specialRecipe, map, itemName, description, group, stacks);

                        if (itemTags.size() > 0) {
                            item.setTags(itemTags);
                        }

                        if (statItems.size() > 0) {
                            item.setStats(statItems);
                        }

                        if (effects.size() > 0) {
                            item.setEffects(effects);
                        }

                        items.add(item);

                        consumed = false;

                        itemTags = new ArrayList<ItemTag>();
                        statItems = new ArrayList<StatItem>();
                        effects = new ArrayList<Effect>();
                    }

                    reader.endObject(); // }
                }

                reader.endObject(); // }
            }

            reader.endObject(); // }
            reader.close();
            input.close();

            return items;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    // TODO: SanitizedDescription ga een object worden
    public static ArrayList<Leaf> leafs(ApplicationInfo appInfo) {
        ArrayList<Leaf> leafs = null;

        try {
            Bundle bundle = appInfo.metaData;

            String webservice = bundle.getString("webservice.static-data");
            String apiKey = bundle.getString("auth.client_secret");
            String params = "mastery?locale=en_US&masteryListData=all";

            InputStream input = new URL(webservice + params + apiKey).openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
            reader.beginObject(); // {

            String leafName = reader.nextName(), description = "";
            int id = 0, ranks = 0, prereq = 0;

            while (!leafName.equals("data")) {
                reader.skipValue();

                leafName = reader.nextName();
            }

            if (leafName.equals("data")) {
                leafs = new ArrayList<Leaf>();
                reader.beginObject(); // {

                while (reader.hasNext()) {
                    reader.nextName(); // 4111
                    reader.beginObject(); // {

                    while (reader.hasNext()) {
                        String key = reader.nextName();

                        if (key.equals("id")) {
                            id = reader.nextInt();
                        }
                        else if (key.equals("name")) {
                            leafName = reader.nextString();
                        }
                        else if (key.equals("sanitizedDescription")) {
                            reader.beginArray(); // [

                            while (reader.hasNext()) {
                                // Kan 1 of meer descriptions bevatten
                                // Ik gebruik ';' als delimiter
                                description += reader.nextString() + ";";
                            }

                            reader.endArray(); // ]
                        }
                        else if (key.equals("ranks")) {
                            ranks = reader.nextInt();
                        }
                        else if (key.equals("prereq")) {
                            // Blijkbaar zijn het strings in den API
                            // Maar het zijn "leaf" IDs
                            prereq = Integer.parseInt(reader.nextString());
                        }
                        else {
                            reader.skipValue();
                        }
                    }

                    reader.endObject(); // }

                    Leaf leaf = new Leaf(id, leafName, description, ranks, prereq);

                    leafs.add(leaf);

                    description = ""; // anders klopt het totaal niet!
                }

                reader.endObject(); // }
            }
            reader.endObject(); // }
            reader.close();
            input.close();

            return leafs;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return leafs;
    }

    public static ArrayList<Rune> runes(ApplicationInfo appInfo) {
        ArrayList<Rune> runes = null;

        try {
            Bundle bundle = appInfo.metaData;

            String webservice = bundle.getString("webservice.static-data");
            String apiKey = bundle.getString("auth.client_secret");
            String params = "rune?locale=en_US&runeListData=all";

            InputStream input = new URL(webservice + params + apiKey).openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
            reader.beginObject(); // {

            String runeName = reader.nextName(), description = "", type = "";
            int id = 0, tier = 0;

            while (!runeName.equals("data")) {
                reader.skipValue();

                runeName = reader.nextName();
            }

            if (runeName.equals("data")) {
                runes = new ArrayList<Rune>();
                reader.beginObject(); // {

                while (reader.hasNext()) {
                    reader.nextName(); // 5001
                    reader.beginObject(); // {

                    while (reader.hasNext()) {
                        String key = reader.nextName();

                        if (key.equals("id")) {
                            id = reader.nextInt();
                        }
                        else if (key.equals("name")) {
                            runeName = reader.nextString();
                        }
                        else if (key.equals("sanitizedDescription")) {
                            description = reader.nextString();
                        }
                        else if (key.equals("rune")) {
                            reader.beginObject(); // {

                            while (reader.hasNext()) {
                                key = reader.nextName();

                                if (key.equals("tier")) {
                                    // In den API is tier een string
                                    tier = Integer.parseInt(reader.nextString());
                                }
                                else if (key.equals("type")) {
                                    type = reader.nextString();
                                }
                                else {
                                    reader.skipValue();
                                }
                            }

                            reader.endObject(); // }
                        }
                        else {
                            reader.skipValue();
                        }
                    }

                    reader.endObject(); // }

                    Rune rune = new Rune(id, runeName, description, tier, type);

                    runes.add(rune);
                }

                reader.endObject(); // }
            }

            reader.endObject(); // }
            reader.close();
            input.close();

            return runes;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return runes;
    }

    public static ArrayList<MasteryTree> masteryTrees(ApplicationInfo appInfo) {
        ArrayList<MasteryTree> masteryTrees = null;

        try {
            Bundle bundle = appInfo.metaData;

            String webservice = bundle.getString("webservice.static-data");
            String apiKey = bundle.getString("auth.client_secret");
            String params = "mastery?locale=en_US&masteryListData=all";

            InputStream input = new URL(webservice + params + apiKey).openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
            reader.beginObject(); // {

            String treeName = reader.nextName();

            while (!treeName.equals("tree")) {
                reader.skipValue();

                treeName = reader.nextName();
            }

            if (treeName.equals("tree")) {
                masteryTrees = new ArrayList<MasteryTree>();
                reader.beginObject(); // {

                while (reader.hasNext()) {
                    treeName = reader.nextName(); // Defense

                    reader.skipValue();

                    MasteryTree masteryTree = new MasteryTree(treeName);

                    masteryTrees.add(masteryTree);
                }

                reader.endObject(); // }
            }

            reader.endObject(); // }
            reader.close();
            input.close();

            return masteryTrees;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return masteryTrees;
    }
}

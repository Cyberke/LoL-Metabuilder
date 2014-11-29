package be.howest.lolmetabuilder.json;

import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import be.howest.lolmetabuilder.data.Champion;
import be.howest.lolmetabuilder.data.FreeChamp;

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
                    title = "", lore = "", passiveName = "", passiveDesc = "";
            int id = 0, attack = 0, defence = 0, magic = 0, difficulty = 0;

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
                        } else if (key.equals("lore")) {
                            lore = reader.nextString();
                        } else if (key.equals("info")) {
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
                        } else if (key.equals("passive")) {
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

                    reader.endObject(); // }

                    Champion champion = new Champion(id, championName, title,
                            lore, attack, defence, magic, difficulty, passiveName, passiveDesc);

                    champions.add(champion);
                }

                reader.endObject(); // }
            }
            else {
                reader.skipValue();
            }

            reader.endObject(); // }
            reader.close();

            return champions;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return champions;
    }
}

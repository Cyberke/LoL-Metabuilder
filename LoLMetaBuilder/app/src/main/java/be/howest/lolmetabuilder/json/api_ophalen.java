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

/**
 * Created by manuel on 11/19/14.
 */
public class api_ophalen {
    public api_ophalen() {}

    public static ArrayList<String> freechampRotation(ApplicationInfo appInfo) {
        ArrayList<String> champRotation = new ArrayList<String>();

        try {
            Bundle bundle = appInfo.metaData;

            String webservice = bundle.getString("webservice.freeRotation");
            String apiKey = bundle.getString("auth.client_secret");

            InputStream input = new URL(webservice + apiKey).openStream();
            JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
            reader.beginObject();

            String champID = reader.nextName();

            reader.beginArray();

            while (reader.hasNext()) {
                reader.beginObject();

                while (reader.hasNext()) {
                    String key = reader.nextName();

                    if (key.equals("id")) {
                        champID = reader.nextString();

                        champRotation.add(champID);
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

        champRotation.add("Nothing");

        return champRotation;
    }
}

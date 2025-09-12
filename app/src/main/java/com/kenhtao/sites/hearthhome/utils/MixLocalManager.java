package com.kenhtao.sites.hearthhome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kenhtao.sites.hearthhome.models.LayerSound;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MixLocalManager {
    private static final String PREF_NAME = "MIX_PREF";

    private static String keyMixFull(long categoryId) {
        return "mix_full_" + categoryId;
    }

    private static String keyCreatedAt(long categoryId) {
        return "createdAt_" + categoryId;
    }


    public static void saveMixFull(Context context, long categoryId, List<LayerSound> layers) {
        if (layers == null) return;

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(layers);

        prefs.edit()
                .putString(keyMixFull(categoryId), json)
                .putLong(keyCreatedAt(categoryId), System.currentTimeMillis())
                .apply();
    }


    public static List<LayerSound> loadMixFull(Context context, long categoryId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(keyMixFull(categoryId), "");
        if (json.isEmpty()) return new ArrayList<>();

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<LayerSound>>() {}.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static long getCreatedAt(Context context, long categoryId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getLong(keyCreatedAt(categoryId), 0);
    }


    public static void clearAllMixes(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}

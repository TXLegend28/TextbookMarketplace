package com.example.textbookmarketplace.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsManager {
    private static final String PREFS = "marketplace_settings";
    private static final String KEY_THEME = "theme_mode";
    private static final String KEY_SELLER_NAME = "seller_name";
    private static final String KEY_SELLER_EMAIL = "seller_email";
    private static final String KEY_BANK_NAME = "bank_name";
    private static final String KEY_ACCOUNT = "account_number";

    public static final int MODE_LIGHT = AppCompatDelegate.MODE_NIGHT_NO;
    public static final int MODE_DARK = AppCompatDelegate.MODE_NIGHT_YES;
    public static final int MODE_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

    private static SharedPreferences getPrefs(Context ctx) {
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void applyTheme(Context ctx) {
        int mode = getPrefs(ctx).getInt(KEY_THEME, MODE_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public static void setThemeMode(Context ctx, int mode) {
        getPrefs(ctx).edit().putInt(KEY_THEME, mode).apply();
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public static int getThemeMode(Context ctx) {
        return getPrefs(ctx).getInt(KEY_THEME, MODE_SYSTEM);
    }

    public static void saveProfile(Context ctx, String name, String email, String bank, String account) {
        getPrefs(ctx).edit()
                .putString(KEY_SELLER_NAME, name)
                .putString(KEY_SELLER_EMAIL, email)
                .putString(KEY_BANK_NAME, bank)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    public static String getSellerName(Context ctx) {
        return getPrefs(ctx).getString(KEY_SELLER_NAME, "");
    }

    public static String getSellerEmail(Context ctx) {
        return getPrefs(ctx).getString(KEY_SELLER_EMAIL, "");
    }

    public static String getBankName(Context ctx) {
        return getPrefs(ctx).getString(KEY_BANK_NAME, "");
    }

    public static String getAccountNumber(Context ctx) {
        return getPrefs(ctx).getString(KEY_ACCOUNT, "");
    }

    public static boolean hasProfile(Context ctx) {
        return !getSellerEmail(ctx).isEmpty();
    }
}
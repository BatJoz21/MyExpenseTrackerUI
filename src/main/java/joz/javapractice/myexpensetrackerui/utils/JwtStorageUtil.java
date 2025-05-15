package joz.javapractice.myexpensetrackerui.utils;

import java.util.prefs.Preferences;

public class JwtStorageUtil {
    private static final String JWT_KEY = "jwtToken";
    private static final Preferences pref = Preferences.userNodeForPackage(JwtStorageUtil.class);

    public static void saveToken(String token){
        pref.put(JWT_KEY, token);
    }

    public static String getToken(){
        return pref.get(JWT_KEY, null);
    }

    public static void clearToken(){
        pref.remove(JWT_KEY);
    }
}

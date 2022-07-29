package fun.norbit.multiplayerlib.json;

import com.google.gson.Gson;

public class GsonFormat {
    private final static Gson gson;

    static {
        gson = new Gson();
    }

    public static String objectToJson(Object o){
        return gson.toJson(o);
    }

    public static <T> T objectFromString(String jsonString, Class<T> c){

        return gson.fromJson(jsonString, c);
    }
}

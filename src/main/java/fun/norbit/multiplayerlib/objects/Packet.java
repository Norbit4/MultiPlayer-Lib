package fun.norbit.multiplayerlib.objects;

import fun.norbit.multiplayerlib.json.GsonFormat;

import java.io.Serializable;

public class Packet implements Serializable {

    private final String jsonString;
    private final String channel;

    public Packet(String channel, Object object) {
        this.jsonString = GsonFormat.objectToJson(object);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public Object getObject(Class<?> c) {
        return GsonFormat.objectFromString(jsonString, c);
    }
}

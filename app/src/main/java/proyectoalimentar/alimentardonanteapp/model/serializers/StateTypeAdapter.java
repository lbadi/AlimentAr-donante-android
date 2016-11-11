package proyectoalimentar.alimentardonanteapp.model.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import proyectoalimentar.alimentardonanteapp.model.Status;

public class StateTypeAdapter implements JsonSerializer<Status>,JsonDeserializer<Status> {

    @Override
    public Status deserialize(JsonElement state, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        switch (state.getAsString()){
            case "open":
                return Status.OPEN;
            case "finished":
                return Status.FINISHED;
            case "cancelled":
                return Status.CANCELLED;
            case "active":
                return Status.ACTIVE;
            default:
                return Status.UNKOWN;
        }
    }

    @Override
    public JsonElement serialize(Status status, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(status.getJsonValue());
    }
}

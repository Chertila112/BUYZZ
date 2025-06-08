import com.google.gson.*
import java.lang.reflect.Type
import java.sql.Timestamp

class TimestampAdapter : JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
    override fun serialize(
        src: Timestamp,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.time)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Timestamp {
        return Timestamp(json.asLong)
    }
}
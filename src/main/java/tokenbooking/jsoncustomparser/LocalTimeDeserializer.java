package tokenbooking.jsoncustomparser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Time;

public class LocalTimeDeserializer extends StdDeserializer<Time> {
    protected LocalTimeDeserializer() {
        super(Time.class);
    }

    @Override
    public Time deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String temp = parser.readValueAs(String.class);
        if (StringUtils.isEmpty(temp)) {
            return null;
        }
        return Time.valueOf(temp + ":00");
    }
}

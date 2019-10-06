package tokenbooking.jsoncustomparser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;

public class LocalTimeDeserializer extends StdDeserializer<LocalTime> {
    protected LocalTimeDeserializer() {
        super(LocalTime.class);
    }

    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String temp = parser.readValueAs(String.class);
        if (StringUtils.isEmpty(temp)) {
            return null;
        }
        LocalTime lt = LocalTime.parse(temp);
        return LocalTime.parse(temp);
    }
}

package tokenbooking.jsoncustomparser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Time;

public class LocalTimeSerializer extends StdSerializer<Time> {
    public LocalTimeSerializer() {
        super(Time.class);
    }

    @Override
    public void serialize(Time value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if(!StringUtils.isEmpty(value)){
            generator.writeString(value.toString());
        }
    }
}

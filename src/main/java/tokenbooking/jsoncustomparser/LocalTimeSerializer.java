package tokenbooking.jsoncustomparser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;

public class LocalTimeSerializer extends StdSerializer<LocalTime> {
    public LocalTimeSerializer() {
        super(LocalTime.class);
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if(!StringUtils.isEmpty(value)){
            String temp = value.toString();
            generator.writeString(temp);
        }
    }
}

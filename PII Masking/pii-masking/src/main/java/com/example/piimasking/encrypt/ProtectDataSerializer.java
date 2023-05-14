package com.example.piimasking.encrypt;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Heshan Karunaratne
 */
public class ProtectDataSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String pii = value.toString().replaceAll("\\w(?=\\w{2})", "x");
        gen.writeString(pii);
    }
}

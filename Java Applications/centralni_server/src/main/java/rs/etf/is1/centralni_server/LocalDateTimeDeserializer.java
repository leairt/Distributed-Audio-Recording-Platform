/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.etf.is1.centralni_server;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;

/**
 *
 * @author sergej
 */
public class LocalDateTimeDeserializer implements JsonbDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext dc, Type type) {
        JsonParser.Event e = p.next();
        
        if (e == JsonParser.Event.VALUE_STRING)
            return LocalDateTime.parse(p.getString()); // ISO-8601

        int[] a = new int[7];
        int i = 0;
        int value = p.getInt();
        System.out.println(value);
        a[i++] = value;
        while(p.hasNext()) {
            e = p.next();
            if (e == JsonParser.Event.END_ARRAY)
                break;
            if (e == JsonParser.Event.VALUE_NUMBER) {
                value = p.getInt();
                System.out.println(value);
                a[i++] = value;
            }
        }
        int year = a[0], mon = a[1], day = a[2], h = a[3], m = a[4], s = (i > 5 ? a[5] : 0), n = (i > 6 ? a[6] : 0);
        return LocalDateTime.of(year, mon, day, h, m, s, n);
    }
}

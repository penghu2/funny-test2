package org.clearfuny.funnytest.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

public class VelocityUtil {

    public static String parse(VelocityContext context, String from){
        if (context.getKeys().length<=1) return from;
        if (!from.contains("$")) return from;
        StringWriter sw = new StringWriter();

        Velocity.init();
        Velocity.evaluate(context,sw,"", from);
        String ret = sw.toString();

        return ret;
    }

    public static void parse(VelocityContext context, Map map){
        if (context.getKeys().length<=1) return;

        Iterator it_d = map.entrySet().iterator();
        while (it_d.hasNext()){
            Map.Entry entry_d = (Map.Entry) it_d.next();
            Object key = entry_d.getKey();
            Object value = entry_d.getValue();
            if (value instanceof String)
                map.put(key, parse(context, (String) value));
        }
    }
}

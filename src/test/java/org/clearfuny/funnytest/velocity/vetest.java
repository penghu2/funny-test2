package org.clearfuny.funnytest.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.testng.annotations.Test;

import java.io.StringWriter;
import java.util.Properties;

public class vetest {

    public boolean equal(String a){
        return "1".equals(a);
    }

    @Test
    public void test() {
        Properties properties=new Properties();
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        VelocityContext context=new VelocityContext();
        context.put("this", this);
        StringWriter sw = new StringWriter();

        Velocity.init();
        Velocity.evaluate(context,sw,"", "$this.equal(\"1\")");
        System.out.println(sw.toString());
    }
}

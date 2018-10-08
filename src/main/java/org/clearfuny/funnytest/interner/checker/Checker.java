package org.clearfuny.funnytest.interner.checker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.handler.Hander;
import org.clearfuny.funnytest.interner.model.Constant;
import org.junit.Assert;
import java.io.StringWriter;

import static org.clearfuny.funnytest.interner.model.Constant.HAND_TYPE_CHECKER;

public class Checker extends Assert implements Hander {

    public void check(String value, JSONObject mainContext,
                             JSONObject stepContext) throws RunFailException {

        VelocityContext context=new VelocityContext();

        context.put("this", mainContext.get(Constant.THIS));
        context.put("res", stepContext.get("res"));
        context.put("Assert", this);
        StringWriter sw = new StringWriter();

        Velocity.init();
        Velocity.evaluate(context,sw,"", value);
        String ret = sw.toString();
        if ("".equals(ret)) return;

        Assert.assertEquals(ret, "true");
    }

    public static void JsonCheckStringEqual(String val, String jsonPath, String expect) {
        Assert.assertEquals(expect, JSONPath.eval(JSON.parse(val), jsonPath));
    }

    public static void JsonCheckSizeEqual(String val, String jsonPath, int size) {
        Assert.assertEquals(size, JSONPath.size(JSON.parse(val),jsonPath));
    }

    @Override
    public void handle(JSONObject handleObject, JSONObject mainContext, JSONObject stepContext) throws RunFailException {
        String value = handleObject.getString("value");
        check(value, mainContext, stepContext);
    }

    @Override
    public String getType() {
        return HAND_TYPE_CHECKER;
    }
}

package org.clearfuny.funnytest.interner.checker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.velocity.VelocityContext;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.handler.Hander;
import org.clearfuny.funnytest.interner.model.Constant;
import org.clearfuny.funnytest.util.VelocityUtil;
import org.junit.Assert;

import java.util.Map;

public class HttpJsonChecker extends Assert implements Hander {

    public void check(String value, JSONObject mainContext,
                             JSONObject stepContext) throws RunFailException {

        VelocityContext context=new VelocityContext();
        context.put("this", mainContext.get(Constant.THIS));

        Map res = (Map)(stepContext.get("res"));
        context.put("res", res);
        context.put("content", JSON.parse((String) res.get("content")));
        context.put("Assert", this);
        String ret = VelocityUtil.parse(context, value);
        if ("".equals(ret)) return;
    }

    @Override
    public void handle(JSONObject handleObject, JSONObject mainContext, JSONObject stepContext) throws RunFailException {
        String value = handleObject.getString("value");
        check(value, mainContext, stepContext);
    }

    @Override
    public String getType() {
        return Constant.HAND_TYPE_JSON_CHECKER;
    }
}

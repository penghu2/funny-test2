package org.clearfuny.funnytest.interner.handler;

import com.alibaba.fastjson.JSONObject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.model.Constant;
import org.clearfuny.funnytest.util.VelocityUtil;


public class RegisterHander implements Hander {

    @Override
    public void handle(JSONObject handleObject, JSONObject mainContext, JSONObject stepContext) throws RunFailException {
        String value = handleObject.getString("value");
        String key = handleObject.getString("key");

        VelocityContext context=new VelocityContext();
        context.put("this", mainContext.get(Constant.THIS));
        context.put("res", stepContext.get("res"));

        Velocity.init();
        String ret = VelocityUtil.parse(context, value);

        mainContext.put(key, ret);
    }

    @Override
    public String getType() {
        return Constant.HAND_TYPE_REGISTER;
    }
}

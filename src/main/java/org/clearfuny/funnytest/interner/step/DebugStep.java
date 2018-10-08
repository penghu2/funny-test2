package org.clearfuny.funnytest.interner.step;

import com.alibaba.fastjson.JSONObject;
import org.apache.velocity.VelocityContext;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.runner.TestStepRunner;
import org.clearfuny.funnytest.util.VelocityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugStep extends TestStepRunner {

    public static final String METHOD_PRINT = "print";

    private static Logger log = LoggerFactory.getLogger(DebugStep.class);


    @Override
    public Object execute(JSONObject mainContext) throws RunFailException {
        this.getParams();
        String method = params.getString("method").toLowerCase();

        switch(method) {
            case METHOD_PRINT:
                print(params);
                break;
        }

        return null;
    }

    @Override
    public void replaceParams(JSONObject mainContext) {
        if (params.containsKey("value")) {
            String newVal = VelocityUtil.parse(new VelocityContext(mainContext), params.getString("value"));
            params.put("value", newVal);
        }

    }

    private void print(JSONObject params) {
        String value = params.getString("value");
        log.info(value);
    }
}

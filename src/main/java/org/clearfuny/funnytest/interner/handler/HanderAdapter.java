package org.clearfuny.funnytest.interner.handler;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.checker.Checker;
import org.clearfuny.funnytest.interner.checker.HttpJsonChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HanderAdapter implements Hander {

    private static Logger log = LoggerFactory.getLogger(HanderAdapter.class);

    private Map<String, Hander> handlerMap = null;

    public HanderAdapter() {

        handlerMap = new HashMap<>();

        /* 插入普通check类 */
        Checker checker = new Checker();
        handlerMap.put(checker.getType(), checker);

        /* 插入json checker类 */
        HttpJsonChecker httpJsonChecker = new HttpJsonChecker();
        handlerMap.put(httpJsonChecker.getType(), httpJsonChecker);

        /* 插入RegisterHander */
        RegisterHander registerHander = new RegisterHander();
        handlerMap.put(registerHander.getType(), registerHander);
    }

    @Override
    public void handle(JSONObject handleObject, JSONObject mainContext, JSONObject stepContext) throws RunFailException {
        String type = handleObject.getString("type");
        Hander handler = handlerMap.get(type);
        if (handler!=null) {
            handler.handle(handleObject, mainContext, stepContext);
        } else {
            log.error("不存在的handler类型[%s]", type);
        }
    }

    @Override
    public String getType() {
        return "Adapter";
    }

}

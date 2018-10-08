package org.clearfuny.funnytest.interner.handler;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;

public interface Hander {

    /**
     * 后处理
     * @param handleObject handle map对象
     * @param mainContext 主上下文， 贯穿整个case
     * @param stepContext step上下文，贯穿整个step，但是与mainContext上下文隔离
     * @throws RunFailException
     */
    public void handle(JSONObject handleObject, JSONObject mainContext,
                       JSONObject stepContext) throws RunFailException;

    /**
     * 获取类型
     * @return 类型
     */
    public String getType();
}

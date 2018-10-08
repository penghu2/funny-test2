package org.clearfuny.funnytest.interner;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;

public interface JsonRunner {

    /**
     * runner 测试用例核心类
     * @param context 测试上下文
     * @throws RunFailException
     */
    public void run(JSONObject context) throws RunFailException;

}

package org.clearfuny.funnytest.interner.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.handler.HanderAdapter;
import org.clearfuny.funnytest.interner.model.TestStep;
import org.clearfuny.funnytest.interner.model.TestStepResult;

public abstract class TestStepRunner extends TestStep {

    protected JSONObject context;

    private static HanderAdapter handerAdapter;

    /**
     * 执行
     * @return
     * @throws RunFailException
     */
    public abstract Object execute(JSONObject mainContext) throws RunFailException;

    /**
     * 替换变量
     * @param mainContext 主上下文
     */
    public abstract void replaceParams(JSONObject mainContext);

    /**
     * 句柄回调
     * @param handle
     * @return
     * @throws RunFailException
     */
    public void handle(JSONArray handle, JSONObject mainContext) throws RunFailException{
        int size = handle.size();
        for (int i=0; i<size; i++) {
            JSONObject item = handle.getJSONObject(i);
            getHanderAdapter().handle(item, mainContext, this.context);
        }
    }

    @Override
    public Object run(JSONObject mainContext) throws RunFailException {
        this.context = new JSONObject();

        // 加一个预处理，支持变量替换
        parseParams(mainContext);

        Object res = execute(mainContext);

        if (res!=null) {
            context.put("res", res);
        }

        try {
            if (this.getHandle()!=null) handle(this.getHandle(), mainContext);
        } catch (RunFailException e) {
            return new TestStepResult(false, res);
        }
        this.context = null;
        return new TestStepResult(true, null);

    }

    /**
     * 变量解析和替换
     * @param mainContext 主上下文
     */
    private void parseParams(JSONObject mainContext) {
        //默认有个this指针在里面
        if (mainContext.size()<=1) return;

        replaceParams(mainContext);
    }

    /**
     * 获取适配器，如果初始为null则赋值一枚
     * @return
     */
    private HanderAdapter getHanderAdapter() {
        if (handerAdapter == null) {
            handerAdapter = new HanderAdapter();
        }

        return handerAdapter;
    }
}

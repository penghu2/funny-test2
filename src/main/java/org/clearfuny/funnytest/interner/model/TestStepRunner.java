package org.clearfuny.funnytest.interner.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.checker.Checker;
import org.clearfuny.funnytest.interner.checker.HttpJsonChecker;


public abstract class TestStepRunner extends TestStep{

    protected JSONObject context;

    public TestStepRunner() {
        super();
    }

    /**
     * 执行
     * @param params
     * @return
     * @throws RunFailException
     */
    abstract Object execute(JSONObject params) throws RunFailException;

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
            String type = item.getString("type");
            switch (type) {
                case "check":
                    Checker.check(item.getString("value"), mainContext, context);
                    break;
                case "jsonCheck":
                    HttpJsonChecker.check(item.getString("value"), mainContext, context);
                    break;
            }
        }
    }


    @Override
    public Object run(JSONObject mainContext) throws RunFailException {
        this.context = new JSONObject();
        mainContext.put(this.getId(), this.context);

        Object res = execute(this.getParams());
        context.put("res", res);
        try {
            handle(this.getHandle(), mainContext);
        } catch (RunFailException e) {
            return new TestStepResult(false, res);
        }

        return new TestStepResult(true, res);
    }

}

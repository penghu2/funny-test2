package org.clearfuny.funnytest.interner.runner;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.interner.model.TestStep;

public interface StepBuilder {

    public TestStep build(JSONObject object);
}

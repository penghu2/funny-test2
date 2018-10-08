package org.clearfuny.funnytest.interner.runner;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;

public interface StepRun {

    public Object run(JSONObject context) throws RunFailException;
}

package org.clearfuny.funnytest.interner.model;

import com.alibaba.fastjson.JSONObject;

public class TestStepResult extends JSONObject{

    public TestStepResult(boolean success, Object res) {
        this.put("success", success);
        this.put("res", res);
    }

    public void setSuccess(boolean success){
        this.put("success", success);
    }
}

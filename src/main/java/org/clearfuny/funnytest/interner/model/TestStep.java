package org.clearfuny.funnytest.interner.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.interner.runner.StepRun;

/**
 * 测试步骤
 */
public abstract class TestStep implements StepRun {

    private String id;

    private String note;

    private String type;

    protected JSONObject params;

    private JSONArray handle;

    private JSONArray register;

    /*==================================[getter && setter]=================================*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public JSONArray getHandle() {
        return handle;
    }

    public void setHandle(JSONArray handle) {
        this.handle = handle;
    }

    public JSONArray getRegister() {
        return register;
    }

    public void setRegister(JSONArray register) {
        this.register = register;
    }
}

package org.clearfuny.funnytest.interner.model;


import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class TestCase {

    private String id;
    /* 描述case */
    private String note;

    /* 执行步骤 */
    private List<TestStep> postSteps;

    /* 预处理步骤 */
    private List<TestStep> preSteps;

    /* 后处理步骤 */
    private List<TestStep> afterStep;

    private JSONObject context;


    public void addPostStep(TestStep step) {

        if (step == null) return;
        if (postSteps==null) {
            postSteps = new LinkedList<>();
        }
        postSteps.add(step);
    }

    public void addPreStep(TestStep step) {
        if (step == null) return;
        if (preSteps==null) {
            preSteps = new LinkedList<>();
        }
        preSteps.add(step);
    }

    public void addAfterStep(TestStep step) {
        if (step == null) return;
        if (afterStep==null) {
            afterStep = new LinkedList<>();
        }
        afterStep.add(step);
    }



    /*===============================[getter && setter]===============================*/


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

    public List<TestStep> getPostSteps() {
        return postSteps;
    }

    public void setPostSteps(List<TestStep> postSteps) {
        this.postSteps = postSteps;
    }

    public List<TestStep> getPreSteps() {
        return preSteps;
    }

    public void setPreSteps(List<TestStep> preSteps) {
        this.preSteps = preSteps;
    }

    public List<TestStep> getAfterStep() {
        return afterStep;
    }

    public void setAfterStep(List<TestStep> afterStep) {
        this.afterStep = afterStep;
    }

    public JSONObject getContext() {
        return context;
    }

    public void setContext(JSONObject context) {
        this.context = context;
    }
}

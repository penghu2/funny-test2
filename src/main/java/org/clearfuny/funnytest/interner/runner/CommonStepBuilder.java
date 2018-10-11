package org.clearfuny.funnytest.interner.runner;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.interner.model.StepType;
import org.clearfuny.funnytest.interner.model.TestStep;
import org.clearfuny.funnytest.interner.step.DbUnitStep;
import org.clearfuny.funnytest.interner.step.DebugStep;
import org.clearfuny.funnytest.interner.step.HttpTestStep;

public class CommonStepBuilder implements StepBuilder{

    private static CommonStepBuilder instance;

    public static CommonStepBuilder getInstance() {
        if (instance == null) {
            instance = new CommonStepBuilder();
        }
        return instance;
    }

    public TestStep build(JSONObject object) {
        String type = object.getString("type");
        StepType stepType = StepType.getByValue(type);
        if (stepType==null) return null;
        switch (stepType) {
            case HTTP:
                return buildHttpStep(object);
            case DBUNIT:
                return buildDbUnitStep(object);
            case DEBUG:
                return buildDebugStep(object);

        }
        return null;
    }

    private HttpTestStep buildHttpStep(JSONObject object) {
        HttpTestStep step = new HttpTestStep();
        buildStep(step, object);
        return step;
    }

    private DbUnitStep buildDbUnitStep(JSONObject object) {
        DbUnitStep step = new DbUnitStep();
        buildStep(step, object);
        return step;
    }

    private DebugStep buildDebugStep(JSONObject object) {
        DebugStep step = new DebugStep();
        buildStep(step, object);
        return step;
    }

    private void buildStep(TestStep step, JSONObject object) {
        step.setId(object.getString("id"));
        step.setNote(object.getString("note"));
        step.setType(object.getString("type"));
        step.setParams(object.getJSONObject("params"));
        step.setHandle(object.getJSONArray("handle"));
    }
}
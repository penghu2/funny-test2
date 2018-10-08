package org.clearfuny.funnytest.interner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.model.*;
import org.clearfuny.funnytest.interner.step.HttpTestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class TestCaseRunnerEngine implements CaseRun, CaseParser {

    private static Logger log = LoggerFactory.getLogger(TestCaseRunnerEngine.class);

    private Object testInstance;

    @Override
    public TestCase parse(JSONObject caseInfo) {

        TestCase testCase = new TestCase();
        testCase.setId(caseInfo.getString("id"));
        testCase.setNote(caseInfo.getString("note"));

        initSteps(testCase, caseInfo);

        return testCase;
    }

    private void initSteps(TestCase testCase, JSONObject caseInfo){
        String[] keys = {"postSteps", "preSteps", "afterSteps"};
        for (String key : keys){
            if (caseInfo.containsKey(key)) {
                JSONArray array = caseInfo.getJSONArray(key);
                int len = array.size();

                for (int i=0; i<len; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    switch (key) {
                        case "postSteps":
                            testCase.addPostStep(buildStep(obj));
                            break;
                        case "preSteps":
                            testCase.addPreStep(buildStep(obj));
                            break;
                        case "afterSteps":
                            testCase.addAfterStep(buildStep(obj));
                            break;
                    }

                }
            }
        }
    }

    private TestStep buildStep(JSONObject object) {
        String type = object.getString("type");
        switch (type) {
            case "http":
                return buildHttpStep(object);

        }
        return null;
    }

    private HttpTestStep buildHttpStep(JSONObject object) {
        HttpTestStep step = new HttpTestStep();
        step.setId(object.getString("id"));
        step.setNote(object.getString("note"));
        step.setType(object.getString("type"));
        step.setParams(object.getJSONObject("params"));
        step.setHandle(object.getJSONArray("handle"));
        return step;
    }

    @Override
    public void run(TestCase testCase) throws RunFailException {
        log.info("==========================begain[%s]==========================", testCase.getId());
        List<TestStep> preSteps = testCase.getPreSteps();
        List<TestStep> postSteps = testCase.getPostSteps();
        try {

            JSONObject mainContext = new JSONObject();

            // pre steps
            runSteps(preSteps, mainContext);

            // post steps
            runSteps(postSteps, mainContext);


        } catch (RunFailException e) {
            log.info("======================Failed[%s][%s]======================", testCase.getId(), e.getMessage());
            throw e;
        }

    }

    private void runSteps(List<TestStep> steps, JSONObject mainContext) throws RunFailException {
        if (!CollectionUtils.isEmpty(steps)) {
            for (TestStep item : steps) {
                item.run(mainContext);
            }
        }
    }

    public static void run(JSONObject caseInfo) throws RunFailException {
        TestCaseRunnerEngine engine = new TestCaseRunnerEngine();
        //从JSON中解析出TestCase对象
        TestCase testCase = engine.parse(caseInfo);
        engine.setTestInstance(caseInfo.get(Constant.THIS));
        engine.run(testCase);
    }

    public Object getTestInstance() {
        return testInstance;
    }

    public void setTestInstance(Object testInstance) {
        this.testInstance = testInstance;
    }
}

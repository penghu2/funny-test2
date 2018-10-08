package org.clearfuny.funnytest.interner.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.model.CaseParser;
import org.clearfuny.funnytest.interner.model.Constant;
import org.clearfuny.funnytest.interner.model.TestCase;
import org.clearfuny.funnytest.interner.model.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class TestCaseRunnerEngine implements CaseRun, CaseParser {

    private static Logger log = LoggerFactory.getLogger(TestCaseRunnerEngine.class);

    private Object testInstance;

    private StepBuilder stepBuilder = new CommonStepBuilder();

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
                            testCase.addPostStep(stepBuilder.build(obj));
                            break;
                        case "preSteps":
                            testCase.addPreStep(stepBuilder.build(obj));
                            break;
                        case "afterSteps":
                            testCase.addAfterStep(stepBuilder.build(obj));
                            break;
                    }

                }
            }
        }
    }

    @Override
    public void run(TestCase testCase) throws RunFailException {
        log.info("==========================begain[%s]==========================", testCase.getId());
        List<TestStep> preSteps = testCase.getPreSteps();
        List<TestStep> postSteps = testCase.getPostSteps();
        try {

            JSONObject mainContext = new JSONObject();

            //初始化上下文
            initContext(mainContext);

            // pre steps
            runSteps(preSteps, mainContext);

            // post steps
            runSteps(postSteps, mainContext);

            //上下文重置为空，释放对象
            mainContext = null;

        } catch (RunFailException e) {
            log.info("======================Failed[%s][%s]======================", testCase.getId(), e.getMessage());
            throw e;
        }

    }

    /**
     * 初始化上下文，将常用实例塞到上下文中
     * @param mainContext 主上下文
     */
    private void initContext(JSONObject mainContext) {
        mainContext.put(Constant.THIS, this.testInstance);
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
        //设置case实例
        engine.setTestInstance(caseInfo.get(Constant.THIS));
        //引擎调度
        engine.run(testCase);
    }

    public Object getTestInstance() {
        return testInstance;
    }

    public void setTestInstance(Object testInstance) {
        this.testInstance = testInstance;
    }
}

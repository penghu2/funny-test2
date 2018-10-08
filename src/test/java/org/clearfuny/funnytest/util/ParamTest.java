package org.clearfuny.funnytest.util;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.BaseTest;
import org.clearfuny.funnytest.interner.runner.TestCaseRunnerEngine;
import org.testng.annotations.Test;

public class ParamTest extends BaseTest {

    @Test(dataProvider="JsonDataProvider")
    public void test(String caseId, JSONObject caseInfo) throws Exception {
        TestCaseRunnerEngine.run(caseInfo);
    }
}

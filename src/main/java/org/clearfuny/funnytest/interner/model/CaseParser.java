package org.clearfuny.funnytest.interner.model;

import com.alibaba.fastjson.JSONObject;

public interface CaseParser {

    /**
     * case 解析器， 从caseInfo中解析出TestCase
     * @param caseInfo case信息
     * @return
     */
    public TestCase parse(JSONObject caseInfo);
}

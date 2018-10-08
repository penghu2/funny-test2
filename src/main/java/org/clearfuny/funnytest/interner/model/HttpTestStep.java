package org.clearfuny.funnytest.interner.model;

import com.alibaba.fastjson.JSONObject;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.util.HttpUtil;
import org.clearfuny.funnytest.interner.runner.TestStepRunner;

import java.io.IOException;
import java.util.Map;


public class HttpTestStep extends TestStepRunner {

    public Object execute(JSONObject params, JSONObject mainContext) throws RunFailException {
        String type = params.getString("method").toLowerCase();
        try {
            switch (type){
                case "get":
                    return runGet(params);
                case "post":
                    return runPost(params);
            }
        } catch (IOException e) {
            throw RunFailException.build(this.getId(), "IO异常", e);
        }

        return null;
    }

    private Object runGet(JSONObject params) throws RunFailException, IOException {

        String url = params.getString("url");


        return HttpUtil.get(url, getHeaders(params));
    }

    private Object runPost(JSONObject params) throws RunFailException, IOException {
        String url = params.getString("url");

        Map data = null;
        if (params.containsKey("data")) {
            data = params.getJSONObject("data");
        }
        return HttpUtil.post(url, data, getHeaders(params));
    }

    private Map getHeaders(JSONObject params) {
        Map headers = null;
        if (params.containsKey("headers")) {
            headers = params.getJSONObject("headers");
        }
        return headers;
    }
}

package org.clearfuny.funnytest.interner.step;

import com.alibaba.fastjson.JSONObject;
import org.apache.velocity.VelocityContext;
import org.clearfuny.funnytest.exception.RunFailException;
import org.clearfuny.funnytest.interner.runner.TestStepRunner;
import org.clearfuny.funnytest.util.HttpUtil;
import org.clearfuny.funnytest.util.LogUtil;
import org.clearfuny.funnytest.util.VelocityUtil;

import java.io.IOException;
import java.util.Map;

/**
 * http 测试 step
 */
public class HttpTestStep extends TestStepRunner {

    private static final String TYPE_GET = "get";

    private static final String TYPE_POST = "post";

    private static final String TYPE_POST_JSON = "postjson";

    public Object execute(JSONObject mainContext) throws RunFailException {
        JSONObject params = this.getParams();
        String type = params.getString("method").toLowerCase();
        Map<String, Object> RES = null;
        try {
            switch (type){
                case TYPE_GET:
                    RES = runGet();
                    break;
                case TYPE_POST:
                    RES = runPost();
                    break;
                case TYPE_POST_JSON:
                    RES = runPostJson();
                    break;
            }

            LogUtil.info("  status_code: %s, response_time: %d ms", RES.get(HttpUtil.RET_CODE), RES.get(HttpUtil.RET_DURATION));
        } catch (IOException e) {
            throw RunFailException.build(this.getId(), "IO异常", e);
        }

        return RES;
    }

    /**
     * 执行变量替换
     * @param mainContext 主上下文
     */
    @Override
    public void replaceParams(JSONObject mainContext) {
        /* url 支持变量替换 */
        if (params.containsKey("url")) {
            String newUrl = VelocityUtil.parse(new VelocityContext(mainContext), params.getString("url"));
            params.put("url", newUrl);
        }

        /* headers 支持变量替换 */
        if (params.containsKey("headers")) {
            Map headers = params.getJSONObject("headers");
            VelocityUtil.parse(new VelocityContext(mainContext), headers);
        }

        if (params.containsKey("data")) {
            Map data = params.getJSONObject("data");
            VelocityUtil.parse(new VelocityContext(mainContext), data);
        }
    }

    /**
     * get 请求
     * @return
     * @throws RunFailException
     * @throws IOException
     */
    private Map<String, Object> runGet() throws RunFailException, IOException {

        String url = params.getString("url");
        LogUtil.info("  GET %s", url);
        return HttpUtil.get(url, getHeaders());
    }

    /**
     * post 请求
     * @return
     * @throws RunFailException
     * @throws IOException
     */
    private Map<String, Object> runPost() throws RunFailException, IOException {
        String url = params.getString("url");
        LogUtil.info("  POST %s", url);
        Map data = null;
        if (params.containsKey("data")) {
            data = params.getJSONObject("data");
        }
        return HttpUtil.post(url, data, getHeaders());
    }

    private Map<String, Object> runPostJson() throws RunFailException, IOException {
        String url = params.getString("url");
        LogUtil.info("  POST-JSON %s", url);
        if (params.containsKey("data")) {
            String data = params.getJSONObject("data").toJSONString();
            return HttpUtil.postJSON(url,data, getHeaders());
        }
        throw RunFailException.build(this.getId(), "postJson 获取data失败", null);
    }


    /**
     * 获取headers
     * @return
     */
    private Map getHeaders() {
        Map headers = null;
        if (params.containsKey("headers")) {
            headers = params.getJSONObject("headers");
        }
        return headers;
    }
}
